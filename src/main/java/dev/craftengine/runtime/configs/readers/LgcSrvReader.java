package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.LgcSrvRecord;

import java.io.IOException;
import java.nio.file.Path;

public class LgcSrvReader extends ConfigReader {

    private final LgcSrvRecord data;

    public LgcSrvReader(int id) throws IOException {
        super(Path.of("logic", "lgcsrv_" + id + ".dat"));

        var magic = byteData().length == 0 ? -1 : unpacker().unpackInt();

        if (magic != 0xCE1631) {
            data = null;
            return;
        }

        var portOffset = unpacker().unpackInt();
        var language = unpacker().unpackString();
        var sdkVersion = unpacker().unpackInt();
        var hash = unpacker().unpackString();

        // Creates the Data Record
        this.data = new LgcSrvRecord(
                portOffset, language,
                sdkVersion, hash
        );
    }

    public LgcSrvRecord data() {
        return data;
    }
}
