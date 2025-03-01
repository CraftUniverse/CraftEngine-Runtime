package dev.craftengine.runtime.configs.readers;

import dev.craftengine.runtime.configs.ConfigReader;
import dev.craftengine.runtime.configs.records.MethodMappingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MeMapReader extends ConfigReader {

    private final Logger log = LoggerFactory.getLogger(MeMapReader.class);
    private final ArrayList<MethodMappingRecord> data = new ArrayList<>();

    public MeMapReader() throws IOException {
        super(Path.of("memap.dat"));

        var magic = byteData().length == 0 ? -1 : unpacker().unpackInt();

        if (magic != 0xCE3431) {
            return;
        }

        var length = unpacker().unpackArrayHeader();

        // Iterates through the data
        for (int i = 0; i < length; i++) {
            unpacker().unpackMapHeader();

            // Logic Server
            unpacker().unpackString();
            var logicServer = unpacker().unpackInt();

            // Method HASH
            unpacker().unpackString();
            var hash = unpacker().unpackString();

            // Expect Return?
            unpacker().unpackString();
            var expectReturn = unpacker().unpackBoolean();

            data.add(new MethodMappingRecord(logicServer, hash, expectReturn));
        }

        log.info("Successfully read Memory Mappings. {} methods mapped.", data.size());
    }

    public ArrayList<MethodMappingRecord> data() {
        return data;
    }
}
