package dev.craftengine.runtime.ipc;

import dev.craftengine.runtime.Runtime;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer extends ServerSocket {
    public TCPServer() throws IOException {
        super(Runtime.LOGIC_PORT);

        // 120 Seconds
        this.setSoTimeout(1000 * 60 * 2);
    }
}
