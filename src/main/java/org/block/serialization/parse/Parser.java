package org.block.serialization.parse;

import org.block.Blocks;
import org.block.plugin.PluginContainer;
import org.block.project.module.Module;
import org.block.serialization.ConfigNode;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface of both serialize and deserialize in a combined faction
 * @param <T> The expected value class type for serialization and deserialization
 */
public interface Parser<T> extends Serialize<T>, Deserialize<T> {

    Parser<String> STRING = new Abstract<>(ConfigNode::setValue, ConfigNode::getString);
    Parser<Integer> INTEGER = new Abstract<>(ConfigNode::setValue, ConfigNode::getInteger);
    Parser<Long> LONG = new Abstract<>(ConfigNode::setValue, ConfigNode::getLong);
    Parser<Double> DOUBLE = new Abstract<>(ConfigNode::setValue, ConfigNode::getDouble);
    Parser<Float> FLOAT = new Abstract<>(ConfigNode::setValue, ConfigNode::getFloat);
    Parser<UUID> UNIQUE_ID = new Abstract<>((n, t, v) -> n.setValue(t, v.toString()), (n, t) -> {
        Optional<String> opValue = n.getString(t);
        return opValue.map(UUID::fromString);
    });
    Parser<Module> MODULE = new Abstract<>((n, t, v) -> n.setValue(t, v.getId()), (n, t) -> {
        Optional<String> opValue = n.getString(t);
        return opValue.flatMap(s -> Blocks.getInstance().getAllEnabledPlugins().getAll(PluginContainer::getModules).parallelStream().filter(m -> m.getId().equals(s)).findAny());
    });
    Parser<Rectangle> RECTANGLE = new Abstract<>((n, t, v) -> {
        n.getNode(t).setValue("x", v.x);
        n.getNode(t).setValue("y", v.y);
        n.getNode(t).setValue("width", v.width);
        n.getNode(t).setValue("height", v.height);
    }, (n, t) -> {
        Optional<Integer> opX = n.getNode(t).getInteger("x");
        Optional<Integer> opY = n.getNode(t).getInteger("y");
        Optional<Integer> opWidth = n.getNode(t).getInteger("width");
        Optional<Integer> opHeight = n.getNode(t).getInteger("height");
        if(opX.isPresent() && opY.isPresent() && opWidth.isPresent() && opHeight.isPresent()){
            return Optional.of(new Rectangle(opX.get(), opY.get(), opWidth.get(), opHeight.get()));
        }
        return Optional.empty();
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
