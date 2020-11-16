package org.block.network.common.packets;

import org.block.network.common.Connection;
import org.block.network.common.packets.project.SendProjectPacket;
import org.block.network.common.packets.request.RequestConnectionPacket;
import org.block.network.common.packets.verify.VerifyConnectionPacket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Packet {

    RequestConnectionPacket REQUEST_CONNECTION_PACKET = new RequestConnectionPacket();
    VerifyConnectionPacket VERIFY_CONNECTION_PACKET = new VerifyConnectionPacket();
    SendProjectPacket SEND_PROJECT_PACKET = new SendProjectPacket();

    Set<? extends Packet> PACKETS = new HashSet<>(Arrays.asList(SEND_PROJECT_PACKET, REQUEST_CONNECTION_PACKET, VERIFY_CONNECTION_PACKET));

    interface PacketBuilder {

        String build(Connection connection);
        Packet getPacket();

    }

    String getId();
    void onReceive(Connection.Direct direct);
    void onSend(Connection.Direct direct, PacketBuilder builder);

}