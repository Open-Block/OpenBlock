package org.block.network.common.packets.verify;

import org.block.network.common.packets.PacketValue;

public class VerifyConnectionValue implements PacketValue {

    private VerifyConnectionPacketBuilder builder;

    public VerifyConnectionValue(VerifyConnectionPacketBuilder builder) {
        this.builder = builder;
    }

    public VerifyConnectionPacketBuilder getBuilder() {
        return this.builder;
    }

    @Override
    public String getKey() {
        return "Verify";
    }
}
