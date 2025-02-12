package net.craftengine.runtime;

import net.craftengine.runtime.debug.Git;
import net.craftengine.runtime.ipc.IPCLogicCommunication;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runtime {
    public static int MINECRAFT_PORT = 25565;
    public static int LOGIC_PORT = 48528;
    public static boolean OFFLINE_MODE = false;

    private static Logger logger = LoggerFactory.getLogger(Runtime.class);

    public static void main(String[] args) {
        int argIndex = 0;

        // Parse command line arguments
        for (var arg : args) {
            if (arg.equalsIgnoreCase("--minecraft_port") || arg.equalsIgnoreCase("-mp")) {
                MINECRAFT_PORT = Integer.parseInt(args[argIndex + 1]);
            } else if (arg.equalsIgnoreCase("--logic_port") || arg.equalsIgnoreCase("-lp")) {
                LOGIC_PORT = Integer.parseInt(args[argIndex + 1]);
            } else if (arg.equalsIgnoreCase("--offline") || arg.equalsIgnoreCase("-o")) {
                OFFLINE_MODE = true;
            }

            argIndex++;
        }

        IPCLogicCommunication.init();

        logger.info("Server Port: {}", MINECRAFT_PORT);
        logger.info("Runtime commit \"{}\" on branch \"{}\"", Git.commit(), Git.branch());

        MinecraftServer server = MinecraftServer.init();

        // Check if the application is not in offline mode
        if (!OFFLINE_MODE) {
            MojangAuth.init();
        } else {
            logger.warn("Server is Running in offline mode!");
        }

        server.start("0.0.0.0", MINECRAFT_PORT);
    }
}
