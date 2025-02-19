package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.GameConfigRecord;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;

public class GameConfigReader extends ConfigReader {

    private final GameConfigRecord data;

    public GameConfigReader() throws IOException {
        super(Path.of("gameConfig.dat"));

        boolean y = byteData().length == 0;

        var projectName = y ? "no data" : unpacker().unpackString();
        var projectVersion = y ? "no data" : unpacker().unpackString();
        var projectBuild = y ? BigInteger.ZERO : unpacker().unpackBigInteger();

        var projectAuthors = new ArrayList<String>();

        // Reconstruct projectAuthor Array
        {
            var authorHeader = y ? 0 : unpacker().unpackArrayHeader();

            for (var a = 0; a < authorHeader; a++) {
                projectAuthors.add(unpacker().unpackString());
            }
        }

        var gameVersion = y ? "1.21.4" : unpacker().unpackString();
        var gameProtocol = y ? 769 : unpacker().unpackInt();
        var maxPlayers = y ? -1 : unpacker().unpackInt();

        this.data = new GameConfigRecord(
                projectName, projectVersion, projectBuild, projectAuthors,
                gameVersion, gameProtocol, maxPlayers
        );
    }

    public GameConfigRecord data() {
        return data;
    }
}
