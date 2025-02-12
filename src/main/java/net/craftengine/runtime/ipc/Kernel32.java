package net.craftengine.runtime.ipc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Kernel32 extends Library {
    Kernel32 INSTANCE = Native.load("Kernel32", Kernel32.class);

    Pointer CreateFileMappingA(Pointer hFile, Pointer lpFileMappingAttributes, int flProtect, int dwMaximumSizeHigh, int dwMaximumSizeLow, String lpName);
    Pointer MapViewOfFile(Pointer hFileMappingObject, int dwDesiredAccess, int dwFileOffsetHigh, int dwFileOffsetLow, int dwNumberOfBytesToMap);
    boolean UnmapViewOfFile(Pointer lpBaseAddress);
    boolean CloseHandle(Pointer hObject);
}
