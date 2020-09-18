package org.block.network.common.packets.request;

import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;

import java.time.LocalTime;
import java.util.regex.Pattern;

public class RequestJoinConnectionPacketBuilder implements Packet.PacketBuilder {

    private LocalTime time = LocalTime.now();
    private Long timeout;
    private Boolean accept;
    private String identifier;

    public static final String TIMEOUT = "TimeOut";
    public static final String REQUEST = "Req";
    public static final String ACCEPT = "Accept";

    public LocalTime getTime(){
        return this.time;
    }

    public Long getTimeout(){
        return this.timeout;
    }

    public RequestJoinConnectionPacketBuilder setTimeout(long timeout){
        if(timeout < 0){
            throw new IllegalArgumentException("timeout must be positive");
        }
        this.timeout = timeout;
        return this;
    }

    public Boolean isAccepting(){
        return this.accept;
    }

    public RequestJoinConnectionPacketBuilder setAccepting(boolean value){
        this.accept = value;
        return this;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    public RequestJoinConnectionPacketBuilder setIdentifier(String identifier){
        if(identifier == null){
            throw new IllegalArgumentException("Identifier cannot be null");
        }
        if(identifier.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
            throw new IllegalArgumentException("Identifier requires characters");
        }
        this.identifier = identifier;
        return this;
    }

    @Override
    public String build(Connection connection) {
        switch (connection.getMeans()){
            case CLIENT:
                if(this.identifier == null){
                    throw new IllegalArgumentException("No identifier set");
                }
                return this.time.toString() + "\t" + (this.timeout == null ? "" : (TIMEOUT + ": " + this.timeout + "|")) + REQUEST + ":" + this.identifier;
            case HOST:
                if(this.accept == null){
                    throw new IllegalArgumentException("No accept set");
                }
                return this.time.toString() + "\t" + ACCEPT + ": " + this.accept;
            default: throw new IllegalArgumentException("Unknown ConnectionMeans." + connection.getMeans().name());
        }
    }

    @Override
    public Packet getPacket() {
        return Packet.REQUEST_CONNECTION_PACKET;
    }

    static RequestJoinConnectionPacketBuilder build(String id){
        String[] split = id.split("\t");
        RequestJoinConnectionPacketBuilder builder = new RequestJoinConnectionPacketBuilder();
        builder.time = LocalTime.parse(split[0]);
        String[] split2 = split[1].split(Pattern.quote("|"));
        for(String keyValue : split2){
            if(keyValue.startsWith(TIMEOUT)){
                builder.timeout = Long.parseLong(keyValue.substring(TIMEOUT.length() + 2));
            }else if(keyValue.startsWith(ACCEPT)){
                builder.accept = Boolean.parseBoolean(keyValue.substring(REQUEST.length() + 1));
            }else if(keyValue.startsWith(REQUEST)){
                builder.identifier = keyValue.substring(REQUEST.length() + 1);
            }
        }
        return builder;
    }
}
