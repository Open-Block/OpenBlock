package org.block.network.common.packets.verify;

import org.block.network.ConnectionMeans;
import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.network.server.ServerClientInfo;

public class VerifyConnectionPacket implements Packet {

    @Override
    public String getId() {
        return "Verify";
    }

    @Override
    public void onReceive(Connection.Direct direct) {
        switch (direct.getMeans()){
            case CLIENT:
                direct.onReceive(m -> {
                    direct.sendMessage(new VerifyConnectionPacketBuilder());
                    direct.onReceive(null);
                });
                break;
            case HOST:
                direct.onReceive(m -> {
                    VerifyConnectionPacketBuilder builder = VerifyConnectionPacketBuilder.build(m);
                    ServerClientInfo info = (ServerClientInfo)direct;
                    
                });
                break;
        }
    }

    @Override
    public void onSend(Connection.Direct direct, PacketBuilder builder) {

    }
}