package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.LgcSrvRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class LgcSrvCFReader extends ConfigReader {

    private final ArrayList<LgcSrvRecord> data = new ArrayList<>();

    public LgcSrvCFReader() throws IOException {
        super(Path.of("logic", "lgcsrvcf.dat"));

        var magic = byteData().length == 0 ? -1 : unpacker().unpackInt();

        if (magic != 0xCE1631) {
            return;
        }

        var length = unpacker().unpackArrayHeader();

        // Iterate through the data
        for (int x = 0; x < length; x++) {
            unpacker().unpackMapHeader();

            // SDK Version
            unpacker().unpackString();
            var sdkVersion = unpacker().unpackInt();

            // File Hash
            unpacker().unpackString();
            var hash = unpacker().unpackString();

            data.add(new LgcSrvRecord(sdkVersion, hash));
        }
    }

    public ArrayList<LgcSrvRecord> data() {
        return data;
    }
}
