package org.block.network.common.packets.request;

import org.block.network.common.Connection;
import org.block.network.common.packets.PacketValue;

import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RequestPacketValue implements PacketValue {

    private final String username;
    private final LocalTime time;
    private boolean hasAccepted;
    private Long timeout;
    private Connection.Direct from;

    public RequestPacketValue(Connection.Direct from, LocalTime time, String username){
        this.time = time;
        this.username = username;
        this.from = from;
    }

    public Connection.Direct getTargetConnection(){
        return this.from;
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

    public boolean hasTimedOut(){
        if(this.timeout == null){
            return false;
        }
        LocalTime out = LocalTime.ofNanoOfDay(this.time.toNanoOfDay() + this.timeout);
        if(LocalTime.now().isBefore(out)){
            return false;
        }
        return true;
    }

    public Optional<Long> getTimeout(){
        return Optional.ofNullable(this.timeout);
    }

    public void setTimeout(Long timeout){
        this.timeout = timeout;
    }

    public LocalTime getTimeoutTime(){
        if(this.timeout == null){
            LocalTime out = LocalTime.of(23, 59);
            if(this.getTime().isAfter(out)){
                throw new IllegalStateException("Time is after the timeout");
            }
            return out;
        }
        LocalTime out = LocalTime.ofNanoOfDay(this.time.toNanoOfDay() + this.timeout);
        if(this.getTime().isAfter(out)){
            throw new IllegalStateException("Time is after the timeout");
        }
        return out;
    }

    public LocalTime getTime(){
        return this.time;
    }

    @Override
    public String getKey() {
        return "RequestConnection";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RequestPacketValue)){
            return false;
        }
        RequestPacketValue value = (RequestPacketValue)obj;
        return this.getTargetConnection().getTargetSocket().getInetAddress().getHostAddress().equals(value.getTargetConnection().getTargetSocket().getInetAddress().getHostAddress());
    }
}