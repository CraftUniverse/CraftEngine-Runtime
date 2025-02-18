package dev.craftengine.runtime.ipc;

import com.sun.jna.Pointer;

import java.nio.ByteBuffer;

public class PosixSharedMemory {
    private final String SHM_NAME;
    private final int SHM_SIZE;
    private final int shmFd;
    private final Pointer ptr;
    private final ByteBuffer buffer;

    public PosixSharedMemory(String name, int size) {
        SHM_NAME = "/"+name.toLowerCase();
        SHM_SIZE = size;

        shmFd = PosixLibrary.INSTANCE.shm_open(SHM_NAME, 0x02 | 0x200, 0666); // O_RDWR | O_CREAT
        if (shmFd < 0) {
            throw new RuntimeException("shm_open failed");
        }

        PosixLibrary.INSTANCE.ftruncate(shmFd, SHM_SIZE);

        ptr = PosixLibrary.INSTANCE.mmap(null, SHM_SIZE, 0x1 | 0x2, 0x01, shmFd, 0); // PROT_READ | PROT_WRITE, MAP_SHARED
        if (ptr == null) {
            throw new RuntimeException("mmap failed");
        }

        buffer = ptr.getByteBuffer(0, SHM_SIZE);
    }

    public void write(byte[] data) {
        buffer.clear();
        buffer.put(data);
        buffer.put((byte) 0); // Null-Termination
    }

    public byte[] read() {
        byte[] data = new byte[SHM_SIZE];
        buffer.position(0);
        buffer.get(data);

        return data;
    }

    public Pointer ptr() {
        return ptr;
    }

    public void close() {
        PosixLibrary.INSTANCE.munmap(ptr, SHM_SIZE);
        PosixLibrary.INSTANCE.close(shmFd);
        PosixLibrary.INSTANCE.shm_unlink(SHM_NAME);
    }
}
