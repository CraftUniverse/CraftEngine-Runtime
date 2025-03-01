package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.GameConfigRecord;
import dev.craftengine.runtime.configs.records.ProjectAuthor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class GameConfigReader extends ConfigReader {

    private final GameConfigRecord data;

    public GameConfigReader() throws IOException {
        super(Path.of("gameConfig.dat"));

        var magic = byteData().length == 0 ? -1 : unpacker().unpackInt();

        if (magic != 0xCE6C1) {
            data = null;
            return;
        }

        var projectName = unpacker().unpackString();
        var projectVersion = unpacker().unpackString();
        var projectBuild = unpacker().unpackBigInteger();

        var projectAuthors = new ArrayList<ProjectAuthor>();

        // Reconstruct projectAuthor Array and Object
        {
            var authorHeader = unpacker().unpackArrayHeader();

            for (var a = 0; a < authorHeader; a++) {
                unpacker().unpackMapHeader();

                // Name
                unpacker().unpackString();
                String name = unpacker().unpackString();

                // Website
                unpacker().unpackString();
                String website = unpacker().unpackString();

                // E-Mail
                unpacker().unpackString();
                String email = unpacker().unpackString();

                projectAuthors.add(new ProjectAuthor(name, website, email));
            }
        }

        var gameVersion = unpacker().unpackString();
        var gameProtocol = unpacker().unpackInt();
        var maxPlayers = unpacker().unpackInt();
        var downloadIcon = unpacker().unpackBoolean();
        var iconFetchInterval = -1;

        if (downloadIcon) {
            iconFetchInterval = unpacker().unpackInt();
        }

        var serverIcon = unpacker().unpackString();

        // Creates the Data Record
        this.data = new GameConfigRecord(
                projectName, projectVersion, projectBuild, projectAuthors,
                gameVersion, gameProtocol, maxPlayers, downloadIcon,
                iconFetchInterval, serverIcon
        );
    }

    public GameConfigRecord data() {
        return data;
    }
}
