package dev.craftengine.runtime.events;

import dev.craftengine.runtime.Runtime;
import dev.craftengine.runtime.level.LevelManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.FeatureFlag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.tag.Tag;

public class PlayerJoinEvent {
    private final MiniMessage mm = MiniMessage.miniMessage();

    public PlayerJoinEvent() {
        var eventHandler = MinecraftServer.getGlobalEventHandler();

        eventHandler.addListener(AsyncPlayerPreLoginEvent.class, (e) -> {
            // Skips Version check for Developers
            if (Runtime.DEVELOPER_MODE) return;

            // Kicks player if the Version of the Notchian Client
            // is Unsupported by the Server.
            if (e.getConnection().getProtocolVersion() < Runtime.GAME_CONFIG.gameProtocol()) {
                e.getConnection().kick(mm.deserialize("<red>You're using an unsupported version<br>of Minecraft! Please use "
                        + Runtime.GAME_CONFIG.gameVersion() + " or higher!"));
            }
        });

        eventHandler.addListener(AsyncPlayerConfigurationEvent.class, (e) -> {
            e.setClearChat(true);
            e.setSpawningInstance(LevelManager.VOID_WORLD);

            e.addFeatureFlag(FeatureFlag.MINECART_IMPROVEMENTS);
            e.addFeatureFlag(FeatureFlag.REDSTONE_EXPERIMENTS);
        });

        eventHandler.addListener(PlayerSpawnEvent.class, (e) -> {
            final var player = e.getPlayer();
            final var levelName = player.getInstance().getTag(Tag.String("level_name"));

            if (levelName.equals("INTERNAL_VOID_WORLD")) {
                player.teleport(new Pos(0, 0, 0));
                player.setGameMode(GameMode.SPECTATOR);
                player.setFlyingSpeed(0);
            }
        });
    }
}
