package org.block.serializtion.parse;

import org.block.serializtion.ConfigNode;

public interface Serialize <T> {

    void serialize(ConfigNode node, String key, T value);

}
