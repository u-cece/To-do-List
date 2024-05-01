package u_cece.todolist;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CommandManager {

    private final ToDoList toDoList = ToDoList.INSTANCE;
    private final Logger logger = toDoList.getLogger();
    private final ServerAPI serverAPI = toDoList.getServerAPI();

    public CommandManager() {}

    @FunctionalInterface
    private interface ContextAcceptor
    {
        void accept(CommandContext<CommandSourceStack> context) throws Exception;
    }

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("todo")
                .executes((context) -> {
                    displayModInfo(context);
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("host")
                        .requires((source) -> source.hasPermission(3))
                        .executes((context) -> {
                            displayHost(context);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(argument("host", StringArgumentType.string())
                                .executes((context) -> {
                                    setHost(context);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(literal("add")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::add))
                                .then(argument("description", StringArgumentType.greedyString())
                                        .executes((context) -> executeServerRequest(context, this::addWithDesc)))))
                .then(literal("remove")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::remove))))
                .then(literal("join")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::join))))
                .then(literal("leave")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::leave))))
                .then(literal("info")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::info))))
                .then(literal("modify")
                        .then(argument("task", StringArgumentType.word())
                                .then(literal("description")
                                        .then(argument("description", StringArgumentType.greedyString())
                                                .executes((context) -> executeServerRequest(context, this::modifyDescription))))
                                .then(literal("status")
                                        .then(literal("not_started")
                                                .executes((context) -> executeServerRequest(context, this::modifyStatusNotStarted)))
                                        .then(literal("started")
                                                .executes((context) -> executeServerRequest(context, this::modifyStatusStarted)))
                                        .then(literal("finished")
                                                .executes((context) -> executeServerRequest(context, this::modifyStatusFinished))))
                                .then(literal("priority")
                                        .then(argument("priority", IntegerArgumentType.integer(1))
                                                .executes((context) -> executeServerRequest(context, this::modifyPriority))))
                                .then(literal("tags")
                                        .then(literal("add")
                                                .then(argument("tag", StringArgumentType.word())
                                                        .executes((context) -> executeServerRequest(context, this::modifyTagsAdd))))
                                        .then(literal("remove")
                                                .then(argument("tag", StringArgumentType.word())
                                                        .executes((context) -> executeServerRequest(context, this::modifyTagsRemove)))))))
                .then(literal("list")
                        .executes((context) -> executeServerRequest(context, this::listAll))
                        .then(argument("tag", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::listTagged))))
        );
    }

    private static final int INFO_BORDER_COLOR = 0xB266FF;
    private static final int INFO_TEXT_COLOR = 0x66B2FF;

    private int executeServerRequest(CommandContext<CommandSourceStack> context,
                                     ContextAcceptor requestFunc) {
        new Thread(() -> {
            try {
                requestFunc.accept(context);
            } catch (Exception e) {
                logger.error("Exception in server API request", e);
                context.getSource().sendFailure(Component.literal(e.getMessage()));
            }
        }).start();
        return Command.SINGLE_SUCCESS;
    }

    public void displayModInfo(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> {
            MutableComponent result = Component.empty();
            result.append(Component.literal("============ To-Do List ============\n").withColor(INFO_BORDER_COLOR));
            result.append(Component.literal("""
                    A mod that implements a to-do list
                    that can be accessed via commands.
                    Made by u_cece.
                    """).withColor(INFO_TEXT_COLOR));
            result.append(Component.literal("==================================").withColor(INFO_BORDER_COLOR));
            return result;
        }, false);
    }

    public void displayHost(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("The current host is " + serverAPI.getHost()), false);
    }

    public void setHost(CommandContext<CommandSourceStack> context) {
        serverAPI.setHost(StringArgumentType.getString(context, "host"));
    }

    public void add(CommandContext<CommandSourceStack> context) throws Exception {
        serverAPI.addTask(
                StringArgumentType.getString(context, "task"), "");
    }

    public void addWithDesc(CommandContext<CommandSourceStack> context) throws Exception {
        serverAPI.addTask(
                StringArgumentType.getString(context, "task"),
                StringArgumentType.getString(context, "description"));
    }

    public void remove(CommandContext<CommandSourceStack> context) throws Exception {
        serverAPI.removeTask(
                StringArgumentType.getString(context, "task"));
    }

    public void join(CommandContext<CommandSourceStack> context) {
    }

    public void leave(CommandContext<CommandSourceStack> context) {
    }

    public void modifyDescription(CommandContext<CommandSourceStack> context) {
    }

    public void modifyStatusNotStarted(CommandContext<CommandSourceStack> context) {
    }

    public void modifyStatusStarted(CommandContext<CommandSourceStack> context) {
    }

    public void modifyStatusFinished(CommandContext<CommandSourceStack> context) {
    }

    public void modifyPriority(CommandContext<CommandSourceStack> context) {
    }

    public void modifyTagsAdd(CommandContext<CommandSourceStack> context) {
    }

    public void modifyTagsRemove(CommandContext<CommandSourceStack> context) {
    }

    public void info(CommandContext<CommandSourceStack> context) {
    }

    public void listAll(CommandContext<CommandSourceStack> context) {
    }

    public void listTagged(CommandContext<CommandSourceStack> context) {
    }
}
