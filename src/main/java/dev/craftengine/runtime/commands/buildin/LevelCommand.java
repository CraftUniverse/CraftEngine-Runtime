package dev.craftengine.runtime.commands.buildin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;

public class LevelCommand extends Command {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public LevelCommand() {
        super("level");

        var helpCmd = new HelpSubCommand();

        setDefaultExecutor(helpCmd.getDefaultExecutor());

        addSubcommand(new JoinSubCommand());
        addSubcommand(new ReloadSubCommand());
        addSubcommand(new InfoSubCommand());
        addSubcommand(helpCmd);
    }

    static class HelpSubCommand extends Command {
        public HelpSubCommand() {
            super("help");

            setDefaultExecutor(((sender, context) -> {
                var split = context.getInput().split(" ");
                if (split.length >= 2 && !split[1].equalsIgnoreCase("help")) return;

                sender.sendMessage(mm.deserialize("HELP"));
            }));
        }
    }

    static class InfoSubCommand extends Command {
        public InfoSubCommand() {
            super("info");
        }
    }

    static class JoinSubCommand extends Command {
        public JoinSubCommand() {
            super("join");
        }
    }

    static class ReloadSubCommand extends Command {
        public ReloadSubCommand() {
            super("reload");
        }
    }
}
