package org.block.serialization.parse;

import javafx.scene.shape.Rectangle;
import org.block.Blocks;
import org.block.plugin.Plugin;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

/**
 * The interface of both serialize and deserialize in a combined faction
 *
 * @param <T> The expected value class type for serialization and deserialization
 */
public interface Parser<T> extends Serialize<T>, Deserialize<T> {

    Parser<String> STRING = new Abstract<>(ConfigNode::setValue, ConfigNode::getString);
    Parser<Integer> INTEGER = new Abstract<>(ConfigNode::setValue, ConfigNode::getGenericInteger);
    Parser<Long> LONG = new Abstract<>(ConfigNode::setValue, ConfigNode::getGenericLong);
    Parser<Double> DOUBLE = new Abstract<>(ConfigNode::setValue, ConfigNode::getGenericDouble);
    Parser<Float> FLOAT = new Abstract<>(ConfigNode::setValue, ConfigNode::getFloat);
    Parser<Boolean> BOOLEAN = new Abstract<>(ConfigNode::setValue, ConfigNode::getBoolean);
    Parser<UUID> UNIQUE_ID = new Abstract<>((n, t, v) -> n.setValue(t, v.toString()), (n, t) -> {
        Optional<String> opValue = n.getString(t);
        return opValue.map(UUID::fromString);
    });
    Parser<Plugin> PLUGIN = new Abstract<>((n, t, v) -> n.setValue(t, v.getId()), (n, t) -> {
        Optional<String> opValue = n.getString(t);
        System.out.println("Finding plugin of " + opValue);
        if (opValue.isEmpty()) {
            return Optional.empty();
        }
        return Blocks
                .getInstance()
                .getPlugins()
                .parallelStream()
                .filter(m -> m.getId().equals(opValue.get()))
                .findAny();
    });
    Parser<Rectangle> RECTANGLE = new Abstract<>((n, t, v) -> {
        n.getNode(t).setValue("x", v.getX());
        n.getNode(t).setValue("y", v.getY());
        n.getNode(t).setValue("width", v.getWidth());
        n.getNode(t).setValue("height", v.getHeight());
    }, (n, t) -> {
        OptionalInt opX = n.getNode(t).getInteger("x");
        OptionalInt opY = n.getNode(t).getInteger("y");
        OptionalInt opWidth = n.getNode(t).getInteger("width");
        OptionalInt opHeight = n.getNode(t).getInteger("height");
        if (opX.isPresent() && opY.isPresent() && opWidth.isPresent() && opHeight.isPresent()) {
            return Optional.of(new Rectangle(opX.getAsInt(), opY.getAsInt(), opWidth.getAsInt(), opHeight.getAsInt()));
        }
        return Optional.empty();
    });

    class Abstract<T> implements Parser<T> {

        private final Serialize<T> serialize;
        private final Deserialize<T> deserialize;

        public Abstract(@NotNull Serialize<T> serialize, @NotNull Deserialize<T> deserialize) {
            this.serialize = serialize;
            this.deserialize = deserialize;
        }

        @Override
        public Optional<T> deserialize(@NotNull ConfigNode node, @NotNull String title) {
            return this.deserialize.deserialize(node, title);
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String title, @NotNull T value) {
            this.serialize.serialize(node, title, value);
        }
    }

}
