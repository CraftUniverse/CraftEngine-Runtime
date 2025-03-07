package dev.craftengine.runtime;

import dev.craftengine.runtime.commands.CommandManager;
import dev.craftengine.runtime.configs.readers.GameConfigReader;
import dev.craftengine.runtime.configs.readers.LgcSrvCFReader;
import dev.craftengine.runtime.configs.readers.MeMapReader;
import dev.craftengine.runtime.configs.records.GameConfigRecord;
import dev.craftengine.runtime.configs.records.LgcSrvRecord;
import dev.craftengine.runtime.configs.records.MethodMappingRecord;
import dev.craftengine.runtime.debug.ConsoleInput;
import dev.craftengine.runtime.debug.Git;
import dev.craftengine.runtime.events.PlayerJoinEvent;
import dev.craftengine.runtime.events.ServerListEvent;
import dev.craftengine.runtime.ipc.TCPServer;
import dev.craftengine.runtime.level.LevelManager;
import dev.craftengine.runtime.misc.ServerIconHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Runtime {
    public static String LOGIC_HOST = "0.0.0.0";
    public static String PROJECT_DIR = "./";
    public static int LOGIC_PORT = 64111;
    public static int MINECRAFT_PORT = 25565;
    public static boolean OFFLINE_MODE = false;
    public static Thread TCP_THREAD;
    public static TCPServer TCP_SERVER;
    public static GameConfigRecord GAME_CONFIG;
    public static ArrayList<MethodMappingRecord> METHOD_MAPPINGS;
    public static ArrayList<LgcSrvRecord> LOGIC_SERVERS;
    public static boolean DEVELOPER_MODE = false;

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final Logger log = LoggerFactory.getLogger(Runtime.class);
    private static final ConsoleInput consoleInput = new ConsoleInput();

    public static void main(String[] args) throws IOException {
        int argIndex = 0;

        // Parse command line arguments
        for (var arg : args) {
            if (arg.equalsIgnoreCase("--help") || arg.equalsIgnoreCase("-h")) {
                System.out.println("Arguments:");
                System.out.println("--help | -h");
                System.out.println("    Display's this message");
                System.out.println("--dev | -d");
                System.out.println("    Enables developer mode");
                System.out.println("--project_dir | -pd <string>");
                System.out.println("    Set's the Path to the Project (Not Recommended!) (default: ./)");
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
            } else if (arg.equalsIgnoreCase("--dev") || arg.equalsIgnoreCase("-d")) {
                DEVELOPER_MODE = true;
            } else if (arg.equalsIgnoreCase("--project_dir") || arg.equalsIgnoreCase("-pd")) {
                PROJECT_DIR = args[argIndex + 1].replaceAll("\"", "").replaceAll("'", "");
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
                if (DEVELOPER_MODE) {
                    for (var p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        p.sendMessage(mm.deserialize("<dark_red>[Exception] " + TCPServer.class.getCanonicalName() + " | " + e.getMessage()));
                    }
                }

                throw new RuntimeException(e);
            }
        });

        GAME_CONFIG = new GameConfigReader().data();
        METHOD_MAPPINGS = new MeMapReader().data();
        LOGIC_SERVERS = new LgcSrvCFReader().data();

        var shortCommit = Git.commit() == null ? null : Git.commit().substring(0, 6);

        log.info("Project Directory: {}", PROJECT_DIR);
        log.info("Server Port: {}", MINECRAFT_PORT);
        log.info("Runtime commit \"{}\" on branch \"{}\"", shortCommit, Git.branch());
        log.info("Logic Host: {} | Logic Ports: {}", LOGIC_HOST, LOGIC_PORT);
        log.info("{} - {} - {}", GAME_CONFIG.projectName(), GAME_CONFIG.projectVersion(), GAME_CONFIG.projectBuild());

        MinecraftServer server = MinecraftServer.init();

        ServerIconHandler.load();
        LevelManager.initVoidWorld();

        if (DEVELOPER_MODE) {
            log.warn("Running in developer mode!");
        }

        // EVENTS
        {
            new ServerListEvent();
            new PlayerJoinEvent();
        }

        MinecraftServer.setBrandName("CraftEngine Runtime " + shortCommit + "@" + Git.branch());

        // Initialize command manager
        new CommandManager(MinecraftServer.getCommandManager());

        // Check if the application is not in offline mode
        if (!OFFLINE_MODE) {
            MojangAuth.init();
        } else {
            log.warn("Server is Running in offline mode!");
        }

        log.info("Minecraft Version: {} | P: {} | D: {}", MinecraftServer.VERSION_NAME, MinecraftServer.PROTOCOL_VERSION, MinecraftServer.DATA_VERSION);

        server.start("0.0.0.0", MINECRAFT_PORT);

        TCP_THREAD.start();
        consoleInput.start();
    }
}
