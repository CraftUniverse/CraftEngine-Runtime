package dev.craftengine.runtime.misc;

import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook {
    private static final Logger log = LoggerFactory.getLogger(ShutdownHook.class);

    public ShutdownHook() {
        log.info("Shutdown server...");
        MinecraftServer.stopCleanly();
    }
}
