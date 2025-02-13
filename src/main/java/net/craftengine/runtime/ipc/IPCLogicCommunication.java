package net.craftengine.runtime.ipc;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPCLogicCommunication {
    private static final Logger log = LoggerFactory.getLogger(IPCLogicCommunication.class);
    private static final int EXTRA_BYTES = 1;
    private static METHOD CURRENT_METHOD;

    private static @Nullable WindowsSharedMemory windowsSharedMemory;
    private static @Nullable PosixSharedMemory posixSharedMemory;

    public static String SHM_NAME = "CELogic";
    public static int SHM_SIZE = 1024;
    public static int SHM_QUEUE_LENGTH = 3;

    public static void init() {
        String os = System.getProperty("os.name").toLowerCase();

        int size = ((SHM_SIZE + 1) * SHM_QUEUE_LENGTH) + EXTRA_BYTES;

        if (os.contains("win")) { // Windows
            log.info("Enabling \"CreateFileMapping()\" IPC Communication");
            CURRENT_METHOD = METHOD.CREATE_FILE_MAPPING;

            windowsSharedMemory = new WindowsSharedMemory(SHM_NAME, size);
            windowsSharedMemory.watch();
        } else { // Linux & macOS
            log.info("Enabling \"shm_open()\" (POSIX) IPC Communication");
            CURRENT_METHOD = METHOD.SHM_OPEN_POSIX;

            posixSharedMemory = new PosixSharedMemory(SHM_NAME, size);
        }

        log.info("Created shared memory: {}, {} bytes", CURRENT_METHOD == METHOD.CREATE_FILE_MAPPING ? windowsSharedMemory.ptr() : posixSharedMemory.ptr(), size);
    }

    public enum METHOD {
        CREATE_FILE_MAPPING,
        SHM_OPEN_POSIX
    }
}
