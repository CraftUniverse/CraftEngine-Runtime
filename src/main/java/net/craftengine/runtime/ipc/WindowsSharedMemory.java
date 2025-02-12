package net.craftengine.runtime.ipc;

import com.sun.jna.*;

public class WindowsSharedMemory {
    private static final int PAGE_READWRITE = 0x04;
    private static final int FILE_MAP_ALL_ACCESS = 0xF001F;

    private final int CFM_SIZE;
    private final Pointer hMap;
    private final Pointer ptr;

    public WindowsSharedMemory(String name, int size) {
        CFM_SIZE = size;

        hMap = Kernel32.INSTANCE.CreateFileMappingA(Pointer.NULL, Pointer.NULL, PAGE_READWRITE, 0, size, name);
        if (hMap == null) {
            throw new RuntimeException("CreateFileMapping failed!");
        }

        ptr = Kernel32.INSTANCE.MapViewOfFile(hMap, FILE_MAP_ALL_ACCESS, 0, 0, size);
        if (ptr == null) {
            throw new RuntimeException("MapViewOfFile failed!");
        }
    }

    public void write(byte[] data) {
        ptr.write(0, data, 0, data.length);
    }

    public byte[] read() {
        byte[] buffer = new byte[CFM_SIZE];
        ptr.read(0, buffer, 0, CFM_SIZE);

        return buffer;
    }

    public Pointer ptr() {
        return ptr;
    }

    public void close() {
        Kernel32.INSTANCE.UnmapViewOfFile(ptr);
        Kernel32.INSTANCE.CloseHandle(hMap);
    }
}
