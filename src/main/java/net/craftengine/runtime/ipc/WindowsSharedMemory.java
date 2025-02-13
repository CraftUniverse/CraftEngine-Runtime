package net.craftengine.runtime.ipc;

import com.sun.jna.Pointer;
import org.msgpack.core.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class WindowsSharedMemory {
    private static final int PAGE_READWRITE = 0x04;
    private static final int FILE_MAP_ALL_ACCESS = 0xF001F;
    private static final Logger log = LoggerFactory.getLogger(WindowsSharedMemory.class);
    private static byte[] lastData;

    private final String SHM_NAME;
    private final int SHM_SIZE;
    private final Pointer hMap;
    private final Pointer ptr;
    private final ByteBuffer buffer;

    public WindowsSharedMemory(String name, int size) {
        SHM_NAME = name;
        SHM_SIZE = size;

        hMap = Kernel32.INSTANCE.CreateFileMappingA(Pointer.NULL, Pointer.NULL, PAGE_READWRITE, 0, size, SHM_NAME);
        if (hMap == null) {
            throw new RuntimeException("CreateFileMapping failed!");
        }

        ptr = Kernel32.INSTANCE.MapViewOfFile(hMap, FILE_MAP_ALL_ACCESS, 0, 0, size);
        if (ptr == null) {
            throw new RuntimeException("MapViewOfFile failed!");
        }

        buffer = ptr.getByteBuffer(0, SHM_SIZE);

        lastData = new byte[SHM_SIZE];
    }

    public void write(byte[] data) {
        ptr.write(0, data, 0, data.length);
    }

    public byte[] read() {
        byte[] buffer = new byte[SHM_SIZE];
        ptr.read(0, buffer, 0, SHM_SIZE);

        return buffer;
    }

    public void watch() {
        log.info("Shared Memory watcher running!");

        new Thread(() -> {
            while (true) {
                try {
                    buffer.position(0);
                    byte[] data = new byte[SHM_SIZE];
                    buffer.get(data);

                    // TODO: Handle data

                    if (!Arrays.equals(lastData, data)) {
                        lastData = data;

                        var dataLength = Byte.toUnsignedInt(data[0]);
                        var dataDest = Byte.toUnsignedInt(data[1]);

                        if (dataLength == 0 || dataDest != 0) {
                            continue;
                        }

                        byte[] packedData = new byte[(SHM_SIZE - 2) / IPCLogicCommunication.SHM_QUEUE_LENGTH];

                        int x = 0;
                        while (x <= packedData.length - 1) {
                            packedData[x] = data[x + 2];
                            x++;
                        }

                        var unpacker = MessagePack.newDefaultUnpacker(packedData);

                        log.info(unpacker.unpackString());

                        byte[] newData = new byte[SHM_SIZE];

                        for (var i = IPCLogicCommunication.SHM_SIZE + 1; i < SHM_SIZE; i++) {
                            newData[i - IPCLogicCommunication.SHM_SIZE + 1] = data[i];
                        }

                        newData[0] = (byte) (dataLength - 1);

                        write(newData);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public Pointer ptr() {
        return ptr;
    }

    public void close() {
        Kernel32.INSTANCE.UnmapViewOfFile(ptr);
        Kernel32.INSTANCE.CloseHandle(hMap);
    }
}
