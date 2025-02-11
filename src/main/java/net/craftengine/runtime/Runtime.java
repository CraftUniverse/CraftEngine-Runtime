package net.craftengine.runtime;

import net.minestom.server.MinecraftServer;

public class Runtime {
    public static int SERVER_PORT = 25565;

    public static void main(String[] args) {
        int argIndex = 0;

        // Parse command line arguments
        for(var arg : args) {
            if(arg.equalsIgnoreCase("--port") || arg.equalsIgnoreCase("-p")) {
                SERVER_PORT = Integer.parseInt(args[argIndex + 1]);
            }

            argIndex++;
        }

        MinecraftServer server = MinecraftServer.init();

        server.start("0.0.0.0", SERVER_PORT);
    }
}
