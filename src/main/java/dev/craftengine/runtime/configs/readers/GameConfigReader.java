package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.GameConfigRecord;

import java.io.IOException;
import java.nio.file.Path;

public class GameConfigReader extends ConfigReader {

    private final GameConfigRecord data;

    public GameConfigReader() throws IOException {
        super(Path.of("gameConfig.dat"));

        var projectName = unpacker().unpackString();
        var projectVersion = unpacker().unpackString();
        var projectBuild = unpacker().unpackBigInteger();
        var maxPlayers = unpacker().unpackInt();

        this.data = new GameConfigRecord(projectName, projectVersion, projectBuild, maxPlayers);
    }

    public GameConfigRecord data() {
        return data;
    }
}
