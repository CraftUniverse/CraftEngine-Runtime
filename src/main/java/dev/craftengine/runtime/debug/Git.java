package dev.craftengine.runtime.debug;

public final class Git {
    private static final String COMMIT = "%COMMIT%";
    private static final String BRANCH = "%BRANCH%";

    public static String commit() { return COMMIT; }
    public static String branch() { return BRANCH; }
}