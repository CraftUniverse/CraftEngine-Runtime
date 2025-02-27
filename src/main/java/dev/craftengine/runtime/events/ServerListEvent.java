package dev.craftengine.runtime.events;

import dev.craftengine.runtime.Runtime;
import dev.craftengine.runtime.misc.ServerIconHandler;
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
            data.setMaxPlayer(config.maxPlayers());
            data.setProtocol(config.gameProtocol());
            data.setVersion(config.gameVersion());
            data.setOnline(MinecraftServer.getConnectionManager().getOnlinePlayerCount());
            data.setFavicon(ServerIconHandler.SERVER_ICON);

            e.setResponseData(data);
        });
    }
}
