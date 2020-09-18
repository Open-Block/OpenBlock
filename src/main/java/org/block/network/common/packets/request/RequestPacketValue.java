package org.block.network.common.packets.request;

import org.block.network.common.Connection;
import org.block.network.common.packets.PacketValue;

import java.time.LocalTime;
import java.util.Optional;

public class RequestPacketValue implements PacketValue {

    private final String username;
    private final LocalTime time;
    private boolean hasAccepted;
    private Long timeout;

    public RequestPacketValue(LocalTime time, String username){
        this.time = time;
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean hasAccepted(){
        return this.hasAccepted;
    }

    public void setHasAccepted(boolean accepted){
        this.hasAccepted = accepted;
    }

    public Optional<Long> getTimeout(){
        return Optional.ofNullable(this.timeout);
    }

    public void setTimeout(Long timeout){
        this.timeout = timeout;
    }

    public LocalTime getTimeoutTime(){
        if(this.time == null){
            return LocalTime.of(23, 59);
        }
        return LocalTime.ofNanoOfDay(this.time.toNanoOfDay() + this.timeout);
    }

    public LocalTime getTime(){
        return this.time;
    }

    @Override
    public String getKey() {
        return "RequestConnection";
    }
}