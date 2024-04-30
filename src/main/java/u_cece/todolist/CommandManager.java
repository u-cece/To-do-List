package u_cece.todolist;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.minecraft.commands.Commands.*;

public class CommandManager {

    public CommandManager() {}

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("todo")
                .executes(this::displayModInfo)
                .then(literal("add")
                        .then(argument("task", StringArgumentType.word())
                                .executes(this::add)))
                .then(literal("remove")
                        .then(argument("task", StringArgumentType.word())
                                .executes(this::remove)))
                .then(literal("join")
                        .then(argument("task", StringArgumentType.word())
                                .executes(this::join)))
                .then(literal("leave")
                        .then(argument("task", StringArgumentType.word())
                                .executes(this::leave)))
                .then(literal("info")
                        .then(argument("task", StringArgumentType.word())
                                .executes(this::info)))
                .then(literal("modify")
                        .then(argument("task", StringArgumentType.word())
                                .then(literal("status")
                                        .then(literal("not_started")
                                                .executes(this::modifyStatusNotStarted))
                                        .then(literal("started")
                                                .executes(this::modifyStatusStarted))
                                        .then(literal("finished")
                                                .executes(this::modifyStatusFinished)))
                                .then(literal("priority")
                                        .then(argument("priority", IntegerArgumentType.integer(1))
                                                .executes(this::modifyPriority)))
                                .then(literal("tags")
                                        .then(literal("add")
                                                .then(argument("tag", StringArgumentType.word())
                                                        .executes(this::modifyTagsAdd)))
                                        .then(literal("remove")
                                                .then(argument("tag", StringArgumentType.word())
                                                        .executes(this::modifyTagsRemove))))))
                .then(literal("list")
                        .executes(this::list))
        );
    }

    private static final int INFO_BORDER_COLOR = 0xB266FF;
    private static final int INFO_TEXT_COLOR = 0x66B2FF;

    public int displayModInfo(CommandContext<CommandSourceStack> context) {
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
        return Command.SINGLE_SUCCESS;
    }

    public int add(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int remove(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int join(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int leave(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyStatusNotStarted(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyStatusStarted(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyStatusFinished(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyPriority(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyTagsAdd(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int modifyTagsRemove(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }

    public int info(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> {
            MutableComponent result = Component.empty();
            result.append("info ");
            result.append(StringArgumentType.getString(context, "task"));
            return result;
        }, false);
        return Command.SINGLE_SUCCESS;
    }

    public int list(CommandContext<CommandSourceStack> context) {
        return Command.SINGLE_SUCCESS;
    }
}
