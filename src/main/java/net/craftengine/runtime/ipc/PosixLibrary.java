package net.craftengine.runtime.ipc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface PosixLibrary extends Library {
    PosixLibrary INSTANCE = Native.load("c", PosixLibrary.class);

    int shm_open(String name, int oflag, int mode);
    int ftruncate(int fd, int size);
    Pointer mmap(Pointer addr, int length, int prot, int flags, int fd, int offset);
    int munmap(Pointer addr, int length);
    int close(int fd);
    int shm_unlink(String name);
}