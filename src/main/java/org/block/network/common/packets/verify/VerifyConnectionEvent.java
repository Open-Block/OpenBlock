package org.block.network.common.packets.verify;

import org.block.network.common.Connection;
import org.block.network.common.event.NetworkEvent;

public class VerifyConnectionEvent implements NetworkEvent {

    private VerifyConnectionPacketBuilder builder;
    private Connection.Direct direct;

    public VerifyConnectionEvent(Connection.Direct direct, VerifyConnectionPacketBuilder builder) {
        this.builder = builder;
        this.direct = direct;
    }

    public VerifyConnectionPacketBuilder getValue() {
        return this.builder;
    }

    @Override
    public Connection.Direct getConnection() {
        return this.direct;
    }
}
