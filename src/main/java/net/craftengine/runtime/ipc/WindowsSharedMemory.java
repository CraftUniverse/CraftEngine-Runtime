package net.craftengine.runtime.ipc;

import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WindowsSharedMemory {
    private static final int PAGE_READWRITE = 0x04;
    private static final int FILE_MAP_ALL_ACCESS = 0xF001F;

    private final String EVENT_NAME;
    private final String SHM_NAME;
    private final int SHM_SIZE;
    private final Pointer hMap;
    private final Pointer ptr;
    private final Pointer hEvent;
    private final ByteBuffer buffer;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public WindowsSharedMemory(String name, int size) {
        EVENT_NAME = name + "_CreateEvent";
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

        hEvent = Kernel32.INSTANCE.CreateEventA(null, false, false, EVENT_NAME);
        if (hEvent == null) {
            throw new RuntimeException("CreateEvent failed");
        }
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
        new Thread(() -> {
            while (true) {
                try {
                    int result = Kernel32.INSTANCE.WaitForSingleObject(hEvent, 1000) ? 0 : 1;

                    if (result == 0) {
                        lock.readLock().lock();
                        buffer.position(0);
                        byte[] data = new byte[SHM_SIZE];
                        buffer.get(data);

                        // TODO: Handle data

                        lock.readLock().unlock();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void signalChange() {
        Kernel32.INSTANCE.SetEvent(hEvent);
    }

    public Pointer ptr() {
        return ptr;
    }

    public void close() {
        Kernel32.INSTANCE.UnmapViewOfFile(ptr);
        Kernel32.INSTANCE.CloseHandle(hMap);
        Kernel32.INSTANCE.CloseHandle(hEvent);
        Kernel32.INSTANCE.ResetEvent(hEvent);
    }
}
