package org.block.serializtion.parse;

import org.block.serializtion.ConfigNode;

import java.util.Optional;

public interface Parser<T> extends Serialize<T>, Deserialize<T> {

    Parser<Integer> INTEGER = new Abstract<>((n, t, v) -> n.setValue(t, v), (n, t) -> n.getInteger(t));

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
