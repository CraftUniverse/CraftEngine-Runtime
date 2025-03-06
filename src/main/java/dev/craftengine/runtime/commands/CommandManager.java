package dev.craftengine.runtime.commands;

import dev.craftengine.runtime.Runtime;
import dev.craftengine.runtime.commands.buildin.LoadLevelCommand;

public class CommandManager {

    public CommandManager(net.minestom.server.command.CommandManager commandManager) {
        if (Runtime.DEVELOPER_MODE) {
            commandManager.register(new LoadLevelCommand());
        }
    }
}
