package dev.craftengine.runtime;

import dev.craftengine.runtime.debug.Git;
import dev.craftengine.runtime.ipc.TCPServer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Runtime {
    public static String LOGIC_HOST = "0.0.0.0";
    public static int LOGIC_PORT = 64111;
    public static int MINECRAFT_PORT = 25565;
    public static boolean OFFLINE_MODE = false;
    public static Thread TCP_THREAD;
    public static TCPServer TCP_SERVER;

    private static final Logger logger = LoggerFactory.getLogger(Runtime.class);

    public static void main(String[] args) throws IOException {
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
                System.out.println("--logic_host | -lh <string>");
                System.out.println("    Set's the Logic TCP Server Host (default: 0.0.0.0)");
                System.out.println("--logic_port | -lp <int32>");
                System.out.println("    Set's the Logic TCP Server Start-Port (default: 64111)");
                System.out.println("    from there it will be counting upwards");
                return;
            } else if (arg.equalsIgnoreCase("--minecraft_port") || arg.equalsIgnoreCase("-mp")) {
                MINECRAFT_PORT = Integer.parseInt(args[argIndex + 1]);
            } else if (arg.equalsIgnoreCase("--offline") || arg.equalsIgnoreCase("-o")) {
                OFFLINE_MODE = true;
            } else if (arg.equalsIgnoreCase("--logic_host") || arg.equalsIgnoreCase("-lh")) {
                LOGIC_HOST = args[argIndex + 1].replaceAll("\"", "").replaceAll("'", "");
            } else if (arg.equalsIgnoreCase("--logic_port") || arg.equalsIgnoreCase("-lp")) {
                LOGIC_PORT = Integer.parseInt(args[argIndex + 1]);
            }

            argIndex++;
        }

        TCP_THREAD = new Thread(() -> {
            try {
                TCP_SERVER = new TCPServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        var shortCommit = Git.commit() == null ? null : Git.commit().substring(0, 6);

        logger.info("Server Port: {}", MINECRAFT_PORT);
        logger.info("Runtime commit \"{}\" on branch \"{}\"", shortCommit, Git.branch());
        logger.info("Logic Host: {} | Logic Ports: {}", LOGIC_HOST, LOGIC_PORT);

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
        TCP_THREAD.start();
    }
}
