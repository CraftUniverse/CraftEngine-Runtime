package dev.craftengine.runtime.level;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.tag.Tag;

import java.util.HashMap;

public class LevelManager {
    public static Instance VOID_WORLD;
    public HashMap<String, Object> loadedLevels;

    public LevelManager() {

    }

    public static void initVoidWorld() {
        VOID_WORLD = MinecraftServer.getInstanceManager().createInstanceContainer();

        VOID_WORLD.setTime(18000);
        VOID_WORLD.setTimeRate(0);
        VOID_WORLD.enableAutoChunkLoad(false);
        VOID_WORLD.loadChunk(0, 0);
        VOID_WORLD.setTag(Tag.String("level_name"), "INTERNAL_VOID_WORLD");
    }
}
