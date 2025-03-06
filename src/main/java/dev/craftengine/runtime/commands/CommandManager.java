package dev.craftengine.runtime.commands;

import dev.craftengine.runtime.Runtime;
import dev.craftengine.runtime.commands.buildin.HelpCommand;
import dev.craftengine.runtime.commands.buildin.LevelCommand;

public class CommandManager {

    public CommandManager(net.minestom.server.command.CommandManager commandManager) {
        if (Runtime.DEVELOPER_MODE) {
            commandManager.register(new LevelCommand());
            commandManager.register(new HelpCommand());
        }
    }
}
