package u_cece.todolist;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ToDoList {
    INSTANCE;

    private Logger logger;

    private CommandManager commandProcessor;
    private ServerAPI serverAPI;

    public void init() {
        logger = LoggerFactory.getLogger("to-do-list");

        commandProcessor = new CommandManager();
        serverAPI = new ServerAPI();
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    Logger getLogger() {
        return logger;
    }

    public ServerAPI getServerAPI() {
        return serverAPI;
    }

    void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                          CommandBuildContext registryAccess,
                          Commands.CommandSelection environment) {
        commandProcessor.registerCommands(dispatcher);
    }
}
