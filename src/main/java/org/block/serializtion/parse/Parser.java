package org.block.serializtion.parse;

import org.block.serializtion.ConfigNode;

import java.util.Optional;
import java.util.UUID;

public interface Parser<T> extends Serialize<T>, Deserialize<T> {

    Parser<Integer> INTEGER = new Abstract<>(ConfigNode::setValue, ConfigNode::getInteger);
    Parser<UUID> UNIQUE_ID = new Abstract((n, t, v) -> n.setValue(t, v.toString()), (n, t) -> {
        Optional<String> opValue = n.getString(t);
        if(!opValue.isPresent()){
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(opValue.get()));
    });

    class Abstract<T> implements Parser<T> {

        private final Serialize<T> serialize;
        private final Deserialize<T> deserialize;

        public Abstract(Serialize<T> serialize, Deserialize<T> deserialize){
            this.serialize = serialize;
            this.deserialize = deserialize;
        }

        @Override
        public Optional<T> deserialize(ConfigNode node, String title) {
            return this.deserialize.deserialize(node, title);
        }

        @Override
        public void serialize(ConfigNode node, String title, T value) {
            this.serialize.serialize(node, title, value);
        }
    }

}
