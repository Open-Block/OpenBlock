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
        System.out.println("Received packet");
        direct.onReceive((m) -> {
            System.out.println("Connection means: " + direct.getMeans().name());
            RequestJoinConnectionPacketBuilder builder = RequestJoinConnectionPacketBuilder.build(m);
            System.out.println("Builder: " + builder);
            System.out.println("Accepting: " + builder.isAccepting());
            System.out.println("Id: " + builder.getIdentifier());
            System.out.println("Time: " + builder.getTime());
            System.out.println("Timeout: " + builder.getTimeout());
            switch (direct.getMeans()){
                case CLIENT:
                    //connect the user
                    System.out.println("Client: User " + (builder.isAccepting() ? "Accepted" : "Denied") + " invite");
                    break;
                case HOST:
                    //send request to user front
                    System.out.println("Server: Got a invite from " + builder.getIdentifier() + " at " + builder.getTime() + " which times out in " + LocalTime.ofNanoOfDay(LocalTime.now().getNano() - (builder.getTime().getNano() + (builder.getTimeout() == null ? 0 : builder.getTimeout()))));
                    //for testing
                    RequestPacketValue packetValue = new RequestPacketValue(builder.getTime(), builder.getIdentifier());
                    if(builder.getTimeout() != null){
                        packetValue.setTimeout(builder.getTimeout());
                    }
                    direct.registerPacketValues(packetValue);
                    RequestJoinEvent joinEvent = new RequestJoinEvent(direct, packetValue);
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
        if(direct.getMeans().equals(ConnectionMeans.HOST)){
            RequestPacketValue value = new RequestPacketValue(packet.getTime(), packet.getIdentifier());
            value.setHasAccepted(packet.isAccepting());
            value.setTimeout(packet.getTimeout());
            direct.registerPacketValues(value);
        }
        direct.sendMessage(builder.build(direct));
    }
}
