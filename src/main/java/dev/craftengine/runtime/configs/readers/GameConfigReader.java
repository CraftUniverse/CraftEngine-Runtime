package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.GameConfigRecord;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

public class GameConfigReader extends ConfigReader {

    private final GameConfigRecord data;

    public GameConfigReader() throws IOException {
        super(Path.of("gameConfig.dat"));

        String projectName = unpacker().unpackString();
        String projectVersion = unpacker().unpackString();
        BigInteger projectBuild = unpacker().unpackBigInteger();

        this.data = new GameConfigRecord(projectName, projectVersion, projectBuild);
    }

    public GameConfigRecord data() {
        return data;
    }
}
