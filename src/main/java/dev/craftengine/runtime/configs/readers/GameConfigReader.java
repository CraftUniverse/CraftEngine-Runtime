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

        boolean y = byteData().length == 0;

        var projectName = y ? "no data" : unpacker().unpackString();
        var projectVersion = y ? "no data" : unpacker().unpackString();
        var projectBuild = y ? BigInteger.ZERO : unpacker().unpackBigInteger();
        var maxPlayers = y ? -1 : unpacker().unpackInt();

        this.data = new GameConfigRecord(projectName, projectVersion, projectBuild, maxPlayers);
    }

    public GameConfigRecord data() {
        return data;
    }
}
