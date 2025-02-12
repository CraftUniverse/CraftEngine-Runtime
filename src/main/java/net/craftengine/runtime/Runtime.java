package net.craftengine.runtime;

import net.craftengine.runtime.debug.Git;
import net.craftengine.runtime.ipc.IPCLogicCommunication;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runtime {
    public static int MINECRAFT_PORT = 25565;
    public static boolean OFFLINE_MODE = false;
    public static String SHM_NAME = "CELogic";
    public static int SHM_SIZE = 1024;

    private static Logger logger = LoggerFactory.getLogger(Runtime.class);

    public static void main(String[] args) {
        int argIndex = 0;

        // Parse command line arguments
        for (var arg : args) {
            if (arg.equalsIgnoreCase("--help") || arg.equalsIgnoreCase("-h")) {
                System.out.println("Arguments:");
                System.out.println("--help | -h");
                System.out.println("    Display's this message");
                System.out.println("--minecraft_port | -mp <int32>");
                System.out.println("    Defines the Minecraft Server port (default: 25565)");
                System.out.println("--offline | -o");
                System.out.println("    Start's the Server in Offline Mode (NOT RECOMMENDED!)");
                System.out.println("--shm_name | -shmn <string>");
                System.out.println("    Set's the Shared Memory Name (default: CELogic)");
                System.out.println("--shm_size | -shms <int32>");
                System.out.println("    Set's the Shared Memory Size (default: 1024)");
                return;
            } else if (arg.equalsIgnoreCase("--minecraft_port") || arg.equalsIgnoreCase("-mp")) {
                MINECRAFT_PORT = Integer.parseInt(args[argIndex + 1]);
            } else if (arg.equalsIgnoreCase("--offline") || arg.equalsIgnoreCase("-o")) {
                OFFLINE_MODE = true;
            } else if (arg.equalsIgnoreCase("--shm_name") || arg.equalsIgnoreCase("-shmn")) {
                SHM_NAME = args[argIndex + 1];
            } else if (arg.equalsIgnoreCase("--shm_size") || arg.equalsIgnoreCase("-shms")) {
                SHM_SIZE = Integer.parseInt(args[argIndex + 1]);
            }

            argIndex++;
        }

        IPCLogicCommunication.init();

        var shortCommit = Git.commit() == null ? null : Git.commit().substring(0, 6);

        logger.info("Server Port: {}", MINECRAFT_PORT);
        logger.info("Runtime commit \"{}\" on branch \"{}\"", shortCommit, Git.branch());

        MinecraftServer server = MinecraftServer.init();

        MinecraftServer.setBrandName("CraftEngine Runtime " + shortCommit + "@" + Git.branch());

        // Check if the application is not in offline mode
        if (!OFFLINE_MODE) {
            MojangAuth.init();
        } else {
            logger.warn("Server is Running in offline mode!");
        }

        logger.info("Minecraft Version: {} | P: {} | D: {}", MinecraftServer.VERSION_NAME, MinecraftServer.PROTOCOL_VERSION, MinecraftServer.DATA_VERSION);

        server.start("0.0.0.0", MINECRAFT_PORT);
    }
}
