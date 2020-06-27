package org.block.panel.block.java.operation;

import org.block.panel.block.BlockType;
import org.block.serializtion.ConfigNode;

import java.io.File;

/**
 * The implementation for sum Blocks
 */
public class SumOperation extends AbstractNumberOperation {

    /**
     * The BlockType for the Sum Block.
     */
    public static class SumOperationType implements BlockType<SumOperation> {

        @Override
        public SumOperation build(int x, int y) {
            return new SumOperation(x, y);
        }

        @Override
        public SumOperation build(ConfigNode node) {
            return null;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/maths/sum/");
        }

        @Override
        public String getName() {
            return "Sum";
        }
    }

    public SumOperation(int x, int y) {
        super(x, y, "Sum", "+");
    }
}
