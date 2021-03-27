package org.block.project.block.java.value.number;

import org.block.project.block.BlockType;
import org.block.serialization.parse.Parser;

import java.util.UUID;
import java.util.function.Consumer;

public class IntegerBlock extends NumberBlock<Integer> {

    public IntegerBlock(Integer value) {
        super(null, value);
    }
    
    public IntegerBlock(UUID uuid, Integer value) {
        super(uuid, value);
    }

    @Override
    public void showValueDialog(Consumer<Integer> consumer) {

    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_INTEGER;
    }

    public static class IntegerBlockType extends NumberBlockType<Integer> {

        public IntegerBlockType() {
            super(Parser.INTEGER);
        }

        @Override
        public NumberBlock<Integer> build() {
            return new IntegerBlock(0);
        }
    }
}
