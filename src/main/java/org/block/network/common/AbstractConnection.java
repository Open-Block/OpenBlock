package org.block.network.common;

import org.block.network.common.event.NetEvent;
import org.block.network.common.event.NetworkEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.Packet;
import org.block.network.common.packets.PacketValue;
import org.block.network.common.packets.request.RequestPacketValue;
import org.block.network.common.packets.verify.VerifyConnectionValue;
import org.block.network.server.ServerClientInfo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractConnection implements Connection.Direct {

    protected final Socket socket;
    protected final BufferedReader sendToServer;
    protected final PrintWriter printWriter;
    protected final Set<PacketValue> packetValues = new HashSet<>();
    protected final Set<NetworkListener> networkListeners = new HashSet<>();
    protected Consumer<String> onReceive;
    protected boolean connection;

    public AbstractConnection(Socket socket) throws IOException {
        this.socket = socket;
        OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();
        this.sendToServer = new BufferedReader(new InputStreamReader(input));
        this.printWriter = new PrintWriter(output, true);
    }

    @Override
    public Socket getTargetSocket() {
        return this.socket;
    }

    @Override
    public void connect() {
        try {
            this.connection = true;
            String fromServer;
            while (this.connection && (fromServer = this.sendToServer.readLine()) != null) {
                if (fromServer.startsWith("Pack: ")) {
                    this.onReceive = null;
                    String id = fromServer.substring(6);
                    Optional<? extends Packet> opPacket = Packet.PACKETS.parallelStream().filter(p -> p.getId().equals(id)).findAny();
                    if (!opPacket.isPresent()) {
                        System.err.println("Unknown Packet of " + id);
                        continue;
                    }
                    opPacket.get().onReceive(this);
                    continue;
                }

                if (this.onReceive != null) {
                    this.onReceive.accept(fromServer);
                    continue;
                }
                if (fromServer.equals("end")) {
                    break;
                }
            }
            if (this instanceof ServerClientInfo) {
                this.sendMessage("end");
            }
        } catch (IOException ignore) {
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public BufferedReader getConnectionReader() {
        return this.sendToServer;
    }

    @Override
    public PrintWriter getConnectionWriter() {
        return this.printWriter;
    }

    @Override
    public Collection<NetworkListener> getListeners() {
        return this.networkListeners;
    }

    @Override
    public Set<PacketValue> getPacketValues() {
        return Collections.unmodifiableSet(this.packetValues);
    }

    @Override
    public void registerPacketValues(PacketValue... values) {
        this.packetValues.addAll(Arrays.asList(values));
    }

    @Override
    public void unregisterPacketValue(PacketValue... values) {
        for (PacketValue value : values) {
            this.packetValues.remove(value);
        }
    }

    @Override
    public void registerNetworkListener(NetworkListener listener) {
        this.networkListeners.add(listener);
    }

    @Override
    public void unregisterNetworkListener(NetworkListener listener) {
        this.networkListeners.remove(listener);
    }

    @Override
    public void callEvent(NetworkEvent event) {
        this.networkListeners.parallelStream().forEach(l -> {
            for (Method method : l.getClass().getDeclaredMethods()) {
                if (method.getParameterCount() != 1) {
                    continue;
                }
                if (!method.isAnnotationPresent(NetEvent.class)) {
                    continue;
                }
                if (!method.getParameters()[0].getType().isInstance(event)) {
                    continue;
                }
                try {
                    method.invoke(l, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void sendMessage(String message) {
        this.printWriter.write(message + "\n");
        this.printWriter.flush();
    }

    @Override
    public void sendMessage(Packet.PacketBuilder builder) {
        sendMessage("Pack: " + builder.getPacket().getId());
        builder.getPacket().onSend(this, builder);
    }

    @Override
    public void onReceive(Consumer<String> consumer) {
        this.onReceive = consumer;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractConnection) {
            return this.socket.getInetAddress().equals(((AbstractConnection) object).socket.getInetAddress());
        }
        return false;
    }

    @Override
    public String toString() {
        for (RequestPacketValue value : getPacketValue(RequestPacketValue.class)) {
            if (value.getUsername() != null) {
                return value.getUsername();
            }
        }
        for (VerifyConnectionValue value : getPacketValue(VerifyConnectionValue.class)) {
            if (value.getBuilder().getId().isPresent()) {
                return value.getBuilder().getId().get();
            }
        }

        return super.toString();
    }
}