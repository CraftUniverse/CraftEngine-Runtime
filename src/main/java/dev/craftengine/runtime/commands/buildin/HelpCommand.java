package dev.craftengine.runtime.commands.buildin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;

public class HelpCommand extends Command {
    private final MiniMessage mm = MiniMessage.miniMessage();

    public HelpCommand() {
        super("help");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(mm.deserialize("<red>Not implemented yet!"));
        });
    }
}
