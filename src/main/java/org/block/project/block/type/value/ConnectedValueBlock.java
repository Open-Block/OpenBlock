package org.block.project.block.type.value;

import org.block.project.block.BlockGraphics;

import java.util.Optional;

/**
 * If a block has a value attached to it, then this will be implemented
 *
 * @param <V> The value type of of the connected block
 * @param <G> The graphic attached to the block
 */
public interface ConnectedValueBlock<V> extends ValueBlock<V> {

    /**
     * Gets the value of the block
     * @return The actual value of the block
     */
    V getValue();

    @Override
    default Optional<Class<V>> getExpectedValue(){
        return Optional.of((Class<V>)getValue().getClass());
    }
}