package org.block.serializtion.parse;

import org.block.serializtion.ConfigNode;

import java.util.Optional;

public interface Deserialize<T> {

    Optional<T> deserialize(ConfigNode node, String key);

}
