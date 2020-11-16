package org.block.serialization.parse;

import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface for serialization
 * @param <T> The expected class type of the serialization
 */
public interface Serialize <T> {

    /**
     * Serializes the value to the node at the title
     * @param node The serialize location
     * @param key The key to the location
     * @param value The value to serialize
     */
    void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull T value);

}
