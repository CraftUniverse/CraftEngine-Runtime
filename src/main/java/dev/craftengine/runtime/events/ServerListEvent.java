package dev.craftengine.runtime.events;

import dev.craftengine.runtime.Runtime;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;

public class ServerListEvent {

    private final MiniMessage mm = MiniMessage.miniMessage();

    public ServerListEvent() {
        MinecraftServer.getGlobalEventHandler().addListener(ServerListPingEvent.class, (e) -> {
            ResponseData data = new ResponseData();

            var config = Runtime.GAME_CONFIG;

            data.setDescription(mm.deserialize(config.projectName() + " " + config.projectVersion() + " (" + config.projectBuild() + ")"));
            data.setMaxPlayer(Runtime.GAME_CONFIG.maxPlayers());
            data.setOnline(MinecraftServer.getConnectionManager().getOnlinePlayerCount());

            e.setResponseData(data);
        });
    }
}
