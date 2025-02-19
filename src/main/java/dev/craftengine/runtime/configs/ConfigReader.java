package dev.craftengine.runtime.configs;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigReader {

    private final MessageUnpacker unpacker;

    public ConfigReader(Path path) throws IOException {
        byte[] data = Files.readAllBytes(path);

        unpacker = MessagePack.newDefaultUnpacker(data);
    }

    public MessageUnpacker unpacker() {
        return unpacker;
    }
}
