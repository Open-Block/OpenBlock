package org.block.serializtion;

import org.block.serializtion.parse.Parser;

import java.util.*;

public interface ConfigNode {

    ConfigNode getNode(String... path);

    Optional<String> getString(String title);
    Optional<Integer> getInteger(String title);
    Optional<Long> getLong(String title);
    Optional<Double> getDouble(String title);
    Optional<Float> getFloat(String title);

    <T> Optional<T> getValue(String title, Parser<T> parser);
    <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into);

    void setValue(String title, Object value);
    <T> void setValue(String title, Parser<T> parser, T value);
    void setCollection(String title, Collection<?> collection);
    <T> void setCollection(String title, Parser<T> parser, Collection<T> collection);

    default <T> List<T> getCollection(String title, Parser<T> parser){
        return this.getCollection(title, parser, new ArrayList<>());
    }

    default void setCollection(String title, Object... collection){
        this.setCollection(title, Arrays.asList(collection));
    }

    default <T> void setCollection(String title, Parser<T> parser, T... collection){
        this.setCollection(title, parser, Arrays.asList(collection));
    }


}
