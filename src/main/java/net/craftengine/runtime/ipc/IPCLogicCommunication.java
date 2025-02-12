package net.craftengine.runtime.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPCLogicCommunication {
    private static final Logger log = LoggerFactory.getLogger(IPCLogicCommunication.class);

    public static void init() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) { // Windows
            log.info("Enabling \"CreateFileMapping()\" IPC Communication");
        } else { // Linux & macOS
            log.info("Enabling \"shm_open()\" (POSIX) IPC Communication");
        }
    }
}
