package org.block.network.client;

import org.block.network.ConnectionMeans;
import org.block.network.common.AbstractConnection;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends AbstractConnection {

    public ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public ConnectionMeans getMeans() {
        return ConnectionMeans.CLIENT;
    }
}
