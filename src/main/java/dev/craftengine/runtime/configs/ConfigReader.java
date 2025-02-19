package dev.craftengine.runtime.configs;

import dev.craftengine.runtime.Runtime;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigReader {

    private final MessageUnpacker unpacker;
    private final byte[] byteData;

    public ConfigReader(Path path) throws IOException {
        path = Path.of(Runtime.PROJECT_DIR, path.toString());

        byte[] data = new byte[0];

        if (Files.exists(path)) {
            data = Files.readAllBytes(path);
        }

        this.byteData = data;

        unpacker = MessagePack.newDefaultUnpacker(data);
    }

    public MessageUnpacker unpacker() {
        return unpacker;
    }

    public byte[] byteData() {
        return byteData;
    }
}
