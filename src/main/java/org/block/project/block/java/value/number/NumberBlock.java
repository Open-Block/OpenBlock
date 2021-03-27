package org.block.project.block.java.value.number;

import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.java.value.AbstractValue;
import org.block.project.block.type.value.MutableConnectedValueBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.block.util.ClassCompare;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * The generic {@link Block} to display a pre-defended number value
 * Note that in its current form, the expected value is not in its primitive form.
 * This needs to be worked on ... might need a overhaul to {@link ValueBlock} as generics can not accept primitives
 *
 * @param <V> The expected value
 */
public abstract class NumberBlock<V extends Number> extends AbstractValue<V> implements MutableConnectedValueBlock<V> {

    public NumberBlock(UUID uuid, V value) {
        super(uuid, value, Object::toString);
    }

    @Override
    public Optional<Class<V>> getExpectedValue() {
        return Optional.of((Class<V>) ClassCompare.toPrimitive(this.getValue().getClass()));
    }

    @Override
    public String writeCode(int tab) {
        return this.getValue().toString();
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.unmodifiableCollection(Collections.emptySet());
    }

    public abstract static class NumberBlockType<N extends Number> implements BlockType<NumberBlock<N>> {

        private final Parser<N> parser;

        public NumberBlockType(Parser<N> parser) {
            this.parser = parser;
        }

        @Override
        public NumberBlock<N> build(ConfigNode node) {
            Optional<Double> opX = TITLE_X.deserialize(node);
            if (opX.isEmpty()) {
                throw new IllegalStateException("Could not find position X");
            }
            Optional<Double> opY = TITLE_Y.deserialize(node);
            if (opY.isEmpty()) {
                throw new IllegalStateException("Could not find position Y");
            }
            Optional<N> opValue = node.getValue("Title", this.parser);
            if (opValue.isEmpty()) {
                throw new IllegalStateException("Could not find value");
            }
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            NumberBlock<N> block = this.build();
            block.setPosition(opX.get(), opY.get());
            block.setValue(opValue.get());
            block.id = opUUID.orElseThrow(() -> new IllegalStateException("No Id"));
            //block.layer = TITLE_LAYER.deserialize(node).orElse(Blocks.getInstance().getLoadedProject().get().getPanel().getBlocksPanel().getBlocks().size());
            return block;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/value/number");
        }

        @Override
        public String getName() {
            return "Number";
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull NumberBlock<N> block) {
            BlockType.super.write(node, block);
            node.setValue("Title", this.parser, block.getValue());
        }
    }
}
