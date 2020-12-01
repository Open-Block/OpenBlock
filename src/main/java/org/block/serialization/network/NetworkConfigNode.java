package org.block.serialization.network;

import org.array.utils.ArrayUtils;
import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;

import java.util.Collection;
import java.util.Optional;

public class NetworkConfigNode implements ConfigNode {

    private String[] node;
    private NetworkConfigImplementation implementation;

    public NetworkConfigNode(NetworkConfigImplementation impl, String... node){
        this.node = node;
        this.implementation = impl;
    }

    public String[] getCurrentNode(){
        return this.node;
    }

    private String[] getPath(String title){
        String[] path = new String[this.node.length + 1];
        for(int A = 0; A < this.node.length; A++){
            path[A] = this.node[A];
        }
        path[this.node.length] = title;
        return path;
    }

    @Override
    public NetworkConfigNode getNode(String... path) {
        return new NetworkConfigNode(this.implementation, path);
    }

    @Override
    public Optional<String> getString(String title) {
        return this.implementation.getUnparsedValue(this.getPath(title));
    }

    @Override
    public Optional<Integer> getInteger(String title) {
        return this.getValue(title, Parser.INTEGER);
    }

    @Override
    public Optional<Long> getLong(String title) {
        return this.getValue(title, Parser.LONG);
    }

    @Override
    public Optional<Double> getDouble(String title) {
        return this.getValue(title, Parser.DOUBLE);
    }

    @Override
    public Optional<Float> getFloat(String title) {
        return this.getValue(title, Parser.FLOAT);
    }

    @Override
    public Optional<Boolean> getBoolean(String title) {
        return this.getValue(title, Parser.BOOLEAN);
    }

    @Override
    public <T> Optional<T> getValue(String title, Parser<T> parser) {
        Optional<String> opValue = this.getString(title);
        if(!opValue.isPresent()){
            return Optional.empty();
        }
        return parser.deserialize(this, opValue.get());
    }

    @Override
    public <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into) {
        Optional<String> opString = this.getString(title);
        if(!opString.isPresent()){
            return into;
        }
        String[] array = opString.get().split("¬");
        for(int A = 0; A < array.length; A++){
            parser.deserialize(this, A + "").ifPresent(into::add);
        }
        return into;
    }

    @Override
    public void setValue(String title, Object value) {
        this.implementation.registerValue(value.toString(), this.getPath(title));
    }

    @Override
    public <T> void setValue(String title, Parser<T> parser, T value) {
        parser.serialize(this, title, value);
    }

    @Override
    public void setCollection(String title, Collection<?> collection) {
        this.setValue(title, ArrayUtils.toString("¬", t -> t.toString(), collection));
    }

    @Override
    public <T> void setCollection(String title, Parser<T> parser, Collection<T> collection) {
        int index = 0;
        for(T value : collection){
            parser.serialize(this, index + "", value);
            index++;
        }
    }
}
