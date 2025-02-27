package dev.craftengine.runtime.misc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import static dev.craftengine.runtime.Runtime.GAME_CONFIG;

public class ServerIconHandler {
    public static String SERVER_ICON;

    // This method loads the server icon and stores it in the SERVER_ICON variable
    public static void load() throws IOException {
        // Check if the server icon should be downloaded
        if (GAME_CONFIG.downloadIcon()) {
            var schMgr = MinecraftServer.getSchedulerManager();

            // Read the original image from the URL
            var originalImage = ImageIO.read(URI.create(GAME_CONFIG.serverIcon()).toURL());
            var resizedIcon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            var g = resizedIcon.createGraphics();

            // Rescale the Server Icon
            g.drawImage(originalImage, 0, 0, 64, 64, null);
            g.dispose();

            var outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedIcon, "png", outputStream);

            SERVER_ICON = "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());

            // If the icon should be downloaded every X minutes, schedule a task to do so
            if (GAME_CONFIG.iconFetchInterval() >= 1) {
                schMgr.scheduleTask(() -> {
                    try {
                        load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, TaskSchedule.minutes(GAME_CONFIG.iconFetchInterval()), TaskSchedule.stop());
            }
        } else {
            // If the server icon should not be downloaded, store the Base64 in the SERVER_ICON variable
            SERVER_ICON = GAME_CONFIG.serverIcon();
        }
    }
}
