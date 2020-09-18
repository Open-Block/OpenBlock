package org.block.network.common.packets.request;

import org.block.network.common.Connection;
import org.block.network.common.event.NetworkEvent;

public class RequestJoinEvent implements NetworkEvent {

    private RequestPacketValue value;
    private Connection.Direct connection;

    public RequestJoinEvent(Connection.Direct connection, RequestPacketValue value){
        this.value = value;
        this.connection = connection;
    }

    public RequestPacketValue getPacketValue(){
        return this.value;
    }

    @Override
    public Connection.Direct getConnection() {
        return this.connection;
    }
}
