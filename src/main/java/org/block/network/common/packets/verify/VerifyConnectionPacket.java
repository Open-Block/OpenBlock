package org.block.network.common.packets.verify;

import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;

public class VerifyConnectionPacket implements Packet {

    @Override
    public String getId() {
        return "Verify";
    }

    @Override
    public void onReceive(Connection.Direct direct) {
        direct.onReceive(s -> {
            VerifyConnectionPacketBuilder builder = VerifyConnectionPacketBuilder.build(s);
            VerifyConnectionValue value = new VerifyConnectionValue(builder);
            direct.registerPacketValues(value);
            VerifyConnectionEvent event = new VerifyConnectionEvent(direct, builder);
            direct.callEvent(event);
            direct.onReceive(null);
        });
    }

    @Override
    public void onSend(Connection.Direct direct, PacketBuilder builder) {
        direct.sendMessage(builder.build(direct));
    }
}