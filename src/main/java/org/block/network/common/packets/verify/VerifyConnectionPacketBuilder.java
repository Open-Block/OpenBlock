package org.block.network.common.packets.verify;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.project.block.Block;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class VerifyConnectionPacketBuilder implements Packet.PacketBuilder {

    private LocalTime time = LocalTime.now();
    private int[] version = Blocks.VERSION;
    private Set<String> plugins = Blocks.getInstance().getAllPlugins().get(pc -> pc.getPluginMeta().id());
    private int request;

    public static final String VERSION = "Version";
    public static final String PLUGINS = "Plugins";
    public static final String REQUEST = "Request";

    public static final int REQUEST_TYPE_INIT = 0;

    public LocalTime getTime(){
        return this.time;
    }

    public int getRequest(){
        return this.request;
    }

    public void setRequest(int request){
        this.request = request;
    }

    @Override
    public String build(Connection connection) {
        switch (connection.getMeans()){
            case CLIENT:
                return this.time.toString() + "\t" + VERSION + ": " + this.version[0] + "." + this.version[1] + "." + this.version[2] + "." + this.version[3] + "|" + PLUGINS + ": " + ArrayUtils.toString(", ", t -> t, this.plugins);
            case HOST:
                return this.time.toString() + "\t" + REQUEST + ": " + this.request;
            default: throw new IllegalArgumentException("Unknown ConnectionMeans." + connection.getMeans().name());
        }
    }

    @Override
    public Packet getPacket() {
        return Packet.VERIFY_CONNECTION_PACKET;
    }

    public static VerifyConnectionPacketBuilder build(String line){
        String[] split = line.split("\t");
        LocalTime time = LocalTime.parse(split[0]);
        String[] values = split[1].split("|");
        VerifyConnectionPacketBuilder builder = new VerifyConnectionPacketBuilder();
        builder.time = time;
        for(String keyValue : values){
            String[] pair = keyValue.split(": ");
            switch (pair[0]){
                case VERSION: builder.version = Blocks.parseVersion(pair[1]); break;
                case PLUGINS: builder.plugins = ArrayUtils.ofSet(pair[1].split(", ")); break;
                case REQUEST: builder.request = Integer.parseInt(pair[1]); break;
                default: break;
            }
        }
        return builder;
    }
}
