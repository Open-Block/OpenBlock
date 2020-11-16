package org.block.network.server;

import org.block.network.ConnectionMeans;
import org.block.network.common.Connection;
import org.block.network.common.event.NetworkEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.Packet;
import org.block.network.common.packets.PacketValue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class ServerConnection implements Connection {

    private final ServerSocket socket;
    private final Set<ServerClientInfo> clients = new HashSet<>();
    private final Set<PacketValue> packetValues = new HashSet<>();
    private final Set<NetworkListener> networkListeners = new HashSet<>();
    private boolean stopConnections;

    public ServerConnection(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void sendMessage(String message) {
        this.clients.parallelStream().forEach(ci -> ci.sendMessage(message));
    }

    @Override
    public void sendMessage(Packet.PacketBuilder builder) {
        this.clients.parallelStream().forEach(ci -> ci.sendMessage(builder));
    }

    @Override
    public void onReceive(Consumer<String> consumer) {
        this.clients.parallelStream().forEach(ci -> ci.onReceive(consumer));
    }

    public ServerSocket getTargetSocket(){
        return this.socket;
    }

    public void shutdownConnections() {
        this.clients.forEach(c -> {
            try {
                c.disconnect();
                c.getTargetSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.clients.clear();
        this.stopConnections = true;
    }

    public void acceptConnections() {
        while (!this.stopConnections) {
            try {
                Socket socket = this.socket.accept();
                new Thread(() -> {
                    try {
                        ServerClientInfo info = new ServerClientInfo(this, socket);
                        this.networkListeners.forEach(info::registerNetworkListener);
                        this.packetValues.forEach(info::registerPacketValues);
                        clients.add(info);
                        info.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                if(!this.stopConnections) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Set<PacketValue> getPacketValues() {
        Set<PacketValue> set = new HashSet<>(this.packetValues);
        Iterator<ServerClientInfo> iter = this.clients.iterator();
        while(iter.hasNext()){
            set.addAll(iter.next().getPacketValues());
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public void registerPacketValues(PacketValue... values) {
        this.packetValues.addAll(Arrays.asList(values));
        this.clients.forEach(c -> c.registerPacketValues(values));
    }

    @Override
    public void unregisterPacketValue(PacketValue... values) {
        for(PacketValue value : values){
            this.packetValues.remove(value);
        }
        this.clients.forEach(c -> c.registerPacketValues(values));
    }

    @Override
    public void registerNetworkListener(NetworkListener listener) {
        this.networkListeners.add(listener);
        this.clients.parallelStream().forEach(c -> c.registerNetworkListener(listener));
    }

    @Override
    public void unregisterNetworkListener(NetworkListener listener) {
        this.networkListeners.remove(listener);
        this.clients.parallelStream().forEach(c -> c.unregisterNetworkListener(listener));
    }

    @Override
    public void callEvent(NetworkEvent event) {
        this.clients.forEach(c -> c.callEvent(event));
    }

    @Override
    public ConnectionMeans getMeans() {
        return ConnectionMeans.HOST;
    }
}
