package dev.craftengine.runtime.debug;

import net.minestom.server.MinecraftServer;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleInput {
    private final Thread consoleThread;
    private volatile boolean running = true;
    private final PrintStream originalOut = System.out;

    public ConsoleInput() {
        consoleThread = new Thread(this::handleInput, "ConsoleInput");
    }

    public void start() {
        consoleThread.start();
    }

    public void stop() {
        running = false;
        consoleThread.interrupt();
    }

    private void handleInput() {
        final var scanner = new Scanner(System.in);
        final var consoleSender = MinecraftServer.getCommandManager().getConsoleSender();

        while (running) {
            System.out.print("\033[2K\r> ");

            if (!scanner.hasNextLine()) {
                continue;
            }

            String command = scanner.nextLine().trim();
            if (!command.isEmpty()) {
                System.out.print("\033[2K\r");
                MinecraftServer.getCommandManager().execute(consoleSender, command.replaceFirst("/", ""));
            }
        }

        scanner.close();
    }
}
