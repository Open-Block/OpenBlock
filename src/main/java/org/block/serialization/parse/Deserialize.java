package org.block.serialization.parse;

import org.block.serialization.ConfigNode;

import java.util.Optional;

/**
 * The base interface for deserializing objects
 * @param <T> The supported class type
 */
public interface Deserialize<T> {

    /**
     * Deserialize the value in the node with the provided key
     * @param node The provided node
     * @param key The provided key
     * @return A optional of the value, if the value could not be found or deserialized then will return {@link Optional#empty()}
     */
    Optional<T> deserialize(ConfigNode node, String key);

}
