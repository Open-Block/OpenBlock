package org.block.panel.block.java.value.number;

import org.block.panel.block.input.type.WholeNumberDialog;
import org.block.panel.block.java.value.NumberBlock;
import org.block.serializtion.parse.Parser;

public class IntegerBlock extends NumberBlock<Integer> {

    public static class IntegerBlockType extends NumberBlockType<Integer>{

        public IntegerBlockType() {
            super(Parser.INTEGER);
        }

        @Override
        public NumberBlock<Integer> build(int x, int y) {
            return new IntegerBlock(x, y, 0);
        }
    }

    public IntegerBlock(int x, int y, Integer value) {
        super(x, y, value);
    }

    @Override
    protected WholeNumberDialog createDialog() {
        return new WholeNumberDialog();
    }
}
