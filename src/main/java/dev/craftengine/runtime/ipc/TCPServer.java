package dev.craftengine.runtime.ipc;

import dev.craftengine.runtime.Runtime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends ServerSocket {

    private static final Logger log = LoggerFactory.getLogger(TCPServer.class);

    private final Socket clientSocket;

    public TCPServer() throws IOException {
        super(Runtime.LOGIC_PORT);

        // 120 Seconds
        this.setSoTimeout(1000 * 60 * 2);

        this.clientSocket = this.accept();
        clientSocket.setTcpNoDelay(true);
        clientSocket.setKeepAlive(true);

        var clientSocketIP = clientSocket.getInetAddress().toString();
        var clientSocketPort = clientSocket.getPort();

        log.info("Host Post: {}, IP: {}:{}  Connection Successful!", Runtime.LOGIC_PORT, clientSocketIP, clientSocketPort);
    }

    public Socket clientSocket() {
        return clientSocket;
    }
}
