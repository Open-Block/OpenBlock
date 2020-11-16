package org.block.serialization.network;

import org.array.utils.ArrayUtils;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * This is the implementation used for sending config style data across the network.
 * By doing this it means that we can send essentially a project across the network
 * without the need to program it in specificly as we already have the serialization
 * and deserialization for projects by providing just a ConfigNode.
 *
 * The idea here is to serialize the data in a way that is easy to parse/unparse
 * (network can only see one line at a time) as well as saves data, therefore we
 * can make it look as unreadable as possible (such as compression tactics) without
 * any none-advanced user complaints.
 */
public class NetworkConfigImplementation implements ConfigImplementation<NetworkConfigNode> {

    private Map<String[], String> values = new HashMap<>();

    public Optional<String> getUnparsedValue(String... value){
        return Optional.ofNullable(this.values.get(value));
    }

    public void registerValue(String value, String... path){
        if(this.values.containsKey(path)){
            this.values.replace(path, value);
            return;
        }
        this.values.put(path, value);
    }
    @Override
    public NetworkConfigNode createEmptyNode() {
        return new NetworkConfigNode(this);
    }

    @Override
    public NetworkConfigNode load(String structure) {
        this.values.clear();
        String[] args = structure.split("\n");
        for(String value : args){
            String[] split = value.split(":", 2);
            this.values.put(split[0].split("\\."), split[1]);
        }
        return this.createEmptyNode();
    }

    @Override
    public String write(ConfigNode node) {
        if(!(node instanceof NetworkConfigNode)){
            throw new IllegalStateException("Node must be NetworkConfigNode");
        }
        NetworkConfigNode nNode = (NetworkConfigNode) node;
        StringBuilder builder = new StringBuilder();
        this.values.entrySet().stream().filter(e -> ArrayUtils.contains(e.getKey(), nNode.getCurrentNode())).forEach(e -> {
            builder
                    .append(ArrayUtils.toString(".", t -> t, e.getKey()))
                    .append(":")
                    .append(e.getValue())
                    .append("\f");
        });
        return builder.toString();
    }

    @Override
    @Deprecated
    public NetworkConfigNode load(Path path) throws IOException {
        throw new IllegalStateException("Network Config Implementation can not be parsed/unparsed to file");
    }

    @Override
    public String write(ConfigNode node, Path path) throws IOException {
        throw new IllegalStateException("Network Config Implementation can not be parsed/unparsed to file");
    }
}
