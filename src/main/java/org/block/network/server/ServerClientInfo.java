package org.block.network.server;

import org.block.network.ConnectionMeans;
import org.block.network.common.AbstractConnection;

import java.io.IOException;
import java.net.Socket;

public class ServerClientInfo extends AbstractConnection {

    private final ServerConnection connection;

    public ServerClientInfo(ServerConnection connection, Socket socket) throws IOException {
        super(socket);
        this.connection = connection;
    }

    public ServerConnection getServer() {
        return this.connection;
    }

    @Override
    public ConnectionMeans getMeans() {
        return ConnectionMeans.HOST;
    }
}
