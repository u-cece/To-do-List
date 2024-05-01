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

    public CommandManager() {}

    @FunctionalInterface
    private interface CommandSourceStackAcceptor
    {
        void accept(CommandSourceStack source) throws Exception;
    }

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("todo")
                .executes((context) -> {
                    displayModInfo(context.getSource());
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("add")
                        .then(argument("task", StringArgumentType.word())
                                .executes((context) -> executeServerRequest(context, this::add))))
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
                                     CommandSourceStackAcceptor requestFunc) {
        new Thread(() -> {
            CommandSourceStack source = context.getSource();
            try {
                requestFunc.accept(source);
            } catch (Exception e) {
                logger.error("Exception in server API request", e);
                source.sendFailure(Component.literal(e.getMessage()));
            }
        }).start();
        return Command.SINGLE_SUCCESS;
    }

    public void displayModInfo(CommandSourceStack source) {
        source.sendSuccess(() -> {
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

    public void add(CommandSourceStack source) throws Exception {
        Thread.sleep(1000);
        throw new Exception("meow");
    }

    public void remove(CommandSourceStack source) {
    }

    public void join(CommandSourceStack source) {
    }

    public void leave(CommandSourceStack source) {
    }

    public void modifyStatusNotStarted(CommandSourceStack source) {
    }

    public void modifyStatusStarted(CommandSourceStack source) {
    }

    public void modifyStatusFinished(CommandSourceStack source) {
    }

    public void modifyPriority(CommandSourceStack source) {
    }

    public void modifyTagsAdd(CommandSourceStack source) {
    }

    public void modifyTagsRemove(CommandSourceStack source) {
    }

    public void info(CommandSourceStack source) {
    }

    public void listAll(CommandSourceStack source) {
    }

    public void listTagged(CommandSourceStack source) {
    }
}
