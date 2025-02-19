package dev.craftengine.runtime.configs.records;

import java.math.BigInteger;
import java.util.ArrayList;

public record GameConfigRecord(String projectName,
                               String projectVersion,
                               BigInteger projectBuild,
                               ArrayList<String> projectAuthors,
                               String gameVersion,
                               int gameProtocol,
                               int maxPlayers) {
}
