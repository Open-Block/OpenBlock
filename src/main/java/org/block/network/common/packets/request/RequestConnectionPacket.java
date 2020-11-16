package org.block.network.common.packets.request;

import org.block.network.ConnectionMeans;
import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;

import java.time.LocalTime;

public class RequestConnectionPacket implements Packet {

    @Override
    public String getId() {
        return "Establish";
    }

    @Override
    public void onReceive(Connection.Direct direct) {
        direct.onReceive((m) -> {
            RequestJoinConnectionPacketBuilder builder = RequestJoinConnectionPacketBuilder.build(m);
            RequestPacketValue packetValue;
            RequestJoinEvent joinEvent;
            switch (direct.getMeans()){
                case CLIENT:
                    packetValue = new RequestPacketValue(direct, builder.getTime(), builder.getIdentifier());
                    if(builder.getTimeout() != null){
                        packetValue.setTimeout(builder.getTimeout());
                    }
                    if(builder.isAccepting() != null){
                        packetValue.setHasAccepted(builder.isAccepting());
                    }
                    direct.registerPacketValues(packetValue);
                    joinEvent = new RequestJoinEvent(direct, packetValue);
                    direct.callEvent(joinEvent);
                    break;
                case HOST:
                    //send request to user front
                    packetValue = new RequestPacketValue(direct, builder.getTime(), builder.getIdentifier());
                    if(builder.getTimeout() != null){
                        packetValue.setTimeout(builder.getTimeout());
                    }
                    direct.registerPacketValues(packetValue);
                    joinEvent = new RequestJoinEvent(direct, packetValue);
                    direct.callEvent(joinEvent);
                    break;
                default: throw new IllegalArgumentException("Unknown ConnectionsMeans of '" + direct.getMeans() + "'");
            }
            direct.onReceive(null);
        });
    }

    @Override
    public void onSend(Connection.Direct direct, PacketBuilder builder) {
        if(!(builder instanceof RequestJoinConnectionPacketBuilder)){
            throw new IllegalArgumentException("Packet builder of '" + builder.getClass().getName() + "' does not relate in EstablishConnectionPacket");
        }
        RequestJoinConnectionPacketBuilder packet = (RequestJoinConnectionPacketBuilder) builder;
        RequestPacketValue value = new RequestPacketValue(direct, packet.getTime(), packet.getIdentifier());
        if(packet.getTimeout() != null){
            value.setTimeout(packet.getTimeout());
        }
        if(packet.isAccepting() != null){
            value.setHasAccepted(packet.isAccepting());
        }
        direct.registerPacketValues(value);
        direct.sendMessage(builder.build(direct));
    }
}
