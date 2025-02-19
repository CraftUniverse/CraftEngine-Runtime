package dev.craftengine.runtime.configs.records;

import java.math.BigInteger;

public record GameConfigRecord(String projectName,
                               String projectVersion,
                               BigInteger projectBuild,
                               int maxPlayers) {
}
