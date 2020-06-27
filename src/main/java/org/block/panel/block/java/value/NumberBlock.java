package org.block.panel.block.java.value;

import org.block.panel.block.Block;
import org.block.panel.block.BlockType;

/**
 * The generic {@link Block} to display a pre-defended number value
 * Note that in its current form, the expected value is not in its primitive form.
 * This needs to be worked on ... might need a overhaul to {@link Block.ValueBlock} as generics can not accept primitives
 * @param <V> The expected value
 */
public class NumberBlock<V extends Number> extends AbstractValue<V> {

    /**
     * This will be removed after the serialization branch is committed to main.
     * Ignore this
     */
    public static class IntegerBlockType implements BlockType<NumberBlock<Integer>>{

        @Override
        public NumberBlock<Integer> build(int x, int y) {
            return new NumberBlock<>(x, y, 0);
        }
    }

    public NumberBlock(int x, int y, V value) {
        super(x, y, value, (n) -> n.toString());
    }

    @Override
    public String writeCode() {
        return this.getValue().toString();
    }
}
