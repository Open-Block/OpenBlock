package org.block.network.common;

import org.block.network.ConnectionMeans;
import org.block.network.common.event.NetworkEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.Packet;
import org.block.network.common.packets.PacketValue;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Connection {

    interface Direct extends Connection {

        Socket getTargetSocket();
        void connect();
        BufferedReader getConnectionReader();
        PrintWriter getConnectionWriter();

    }

    Set<PacketValue> getPacketValues();
    void registerPacketValues(PacketValue... values);
    void unregisterPacketValue(PacketValue... values);
    void registerNetworkListener(NetworkListener listener);
    void unregisterNetworkListener(NetworkListener listener);
    void callEvent(NetworkEvent event);
    ConnectionMeans getMeans();
    void sendMessage(String message);
    void sendMessage(Packet.PacketBuilder builder);
    void onReceive(Consumer<String> consumer);

    default <T> Set<T> getPacketValue(Class<T> clazz){
        return (Set<T>)this.getPacketValues().parallelStream().filter(p -> clazz.isInstance(p)).collect(Collectors.toSet());
    }

    default <T> Set<T> getPacketValue(String key){
        return (Set<T>)this.getPacketValues().parallelStream().filter(p -> p.getKey().equals(key)).collect(Collectors.toSet());
    }
}