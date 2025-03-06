package dev.craftengine.runtime.commands.buildin;

import net.minestom.server.command.builder.Command;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");

        setDefaultExecutor(((sender, context) -> {

        }));
    }
}
