package dev.craftengine.runtime.configs.records;

public record LGCSRVRecord(int portOffset, String language,
                           int sdkVersion, String hash) {
}
