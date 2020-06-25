package org.block.panel.block.java.value;

import org.block.panel.block.BlockType;

public class NumberBlock<V extends Number> extends AbstractValue<V> {

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
