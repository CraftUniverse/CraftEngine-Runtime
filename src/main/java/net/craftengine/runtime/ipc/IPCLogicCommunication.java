package net.craftengine.runtime.ipc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPCLogicCommunication {
    private static final Logger log = LoggerFactory.getLogger(IPCLogicCommunication.class);
    private static METHOD METHOD;

    private static WindowsSharedMemory windowsSharedMemory = null;

    public static void init() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) { // Windows
            log.info("Enabling \"CreateFileMapping()\" IPC Communication");
            METHOD = IPCLogicCommunication.METHOD.CREATE_FILE_MAPPING;

            windowsSharedMemory = new WindowsSharedMemory("CELogic", 1024);

            log.info("Created shared memory: {}", windowsSharedMemory.ptr());
        } else { // Linux & macOS
            log.info("Enabling \"shm_open()\" (POSIX) IPC Communication");
            METHOD = IPCLogicCommunication.METHOD.SHM_OPEN_POSIX;
        }
    }

    public enum METHOD {
        CREATE_FILE_MAPPING,
        SHM_OPEN_POSIX
    }
}
