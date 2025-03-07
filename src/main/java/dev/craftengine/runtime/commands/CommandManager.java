package dev.craftengine.runtime.commands;

import dev.craftengine.runtime.Runtime;
import dev.craftengine.runtime.commands.buildin.HelpCommand;
import dev.craftengine.runtime.commands.buildin.LevelCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CommandManager {
    private final MiniMessage mm = MiniMessage.miniMessage();

    public CommandManager(net.minestom.server.command.CommandManager commandManager) {
        commandManager.setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage(mm.deserialize("<red>Unknown command: /" + command));
        });

        if (Runtime.DEVELOPER_MODE) {
            commandManager.register(new LevelCommand());
            commandManager.register(new HelpCommand());
        }
    }
}
