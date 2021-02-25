package org.block.network.common.packets.verify;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.network.common.Connection;
import org.block.network.common.packets.Packet;
import org.block.plugin.Plugin;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VerifyConnectionPacketBuilder implements Packet.PacketBuilder {

    public static final String VERSION = "Version";
    public static final String PLUGINS = "Plugins";
    public static final String REQUEST = "Request";
    public static final String ID = "ID";
    public static final int REQUEST_TYPE_INIT = 0;
    private LocalTime time = LocalTime.now();
    private int[] version = Blocks.VERSION;
    private Set<String> plugins = Blocks.getInstance().getPlugins().parallelStream().map(Plugin::getId).collect(Collectors.toSet());
    private int request;
    private String id;

    public LocalTime getTime() {
        return this.time;
    }

    public int getRequest() {
        return this.request;
    }

    public VerifyConnectionPacketBuilder setRequest(int request) {
        this.request = request;
        return this;
    }

    public VerifyConnectionPacketBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(this.id);
    }

    @Override
    public String build(Connection connection) {
        if (this.id == null) {
            throw new IllegalStateException("Unknown ID. Failed to build VerifyConnectionPacketBuilder");
        }
        if (this.version == null) {
            throw new IllegalStateException("Unknown Version. Failed to build VerifyConnectionPacketBuilder");
        }
        if (this.version.length != 4) {
            throw new IllegalStateException("Not a valid version. Failed to build VerifyConnectionPacketBuilder");
        }
        if (this.plugins == null) {
            throw new IllegalStateException("Null plugins list. Failed to build VerifyConnectionPacketBuilder");
        }
        return this.time.toString() + "\t" + VERSION + ": " + this.version[0] + "." + this.version[1] + "." + this.version[2] + "." + this.version[3] + "|" + ID + ": " + this.id + "|" + PLUGINS + ": " + ArrayUtils.toString(", ", t -> t, this.plugins);
    }

    @Override
    public Packet getPacket() {
        return Packet.VERIFY_CONNECTION_PACKET;
    }

    public static VerifyConnectionPacketBuilder build(String line) {
        String[] split = line.split(Pattern.quote("\t"));
        LocalTime time = LocalTime.parse(split[0]);
        String[] values = split[1].split(Pattern.quote("|"));
        VerifyConnectionPacketBuilder builder = new VerifyConnectionPacketBuilder();
        builder.time = time;
        for (String keyValue : values) {
            String[] pair = keyValue.split(Pattern.quote(": "));
            switch (pair[0]) {
                case VERSION:
                    builder.version = Blocks.parseVersion(pair[1]);
                    break;
                case PLUGINS:
                    builder.plugins = ArrayUtils.ofSet(pair[1].split(", "));
                    break;
                case REQUEST:
                    builder.request = Integer.parseInt(pair[1]);
                    break;
                case ID:
                    builder.id = pair[1];
                    break;
                default:
                    System.err.println("VerifyConnectionPacketBuilder: No idea what '" + pair[0] + "' is");
                    break;
            }
        }
        return builder;
    }
}
