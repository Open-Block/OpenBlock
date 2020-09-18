package org.block.network.common.event;

import org.block.network.common.Connection;

public interface NetworkEvent {

    Connection.Direct getConnection();
}
