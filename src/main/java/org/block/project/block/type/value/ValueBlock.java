package org.block.project.block.type.value;

import org.block.project.block.Block;

import java.util.Optional;

/**
 * If the block has a output value, then this should be implemented which will allow the Block to be added as a parameter.
 *
 * @param <V> The expected class type of the outputted value. This can be {@link Object} if unknown and {@link Void} if no value
 */
public interface ValueBlock<V> extends Block {

    /**
     * Gets the expected value type that the block returns
     *
     * @return The expected class of the code output
     */
    Optional<Class<V>> getExpectedValue();

}