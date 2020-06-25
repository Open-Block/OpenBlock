package org.block.panel.block.java.operation;

import org.block.panel.block.BlockType;

public class SumOperation extends AbstractNumberOperation {

    public static class SumOperationType implements BlockType<SumOperation> {

        @Override
        public SumOperation build(int x, int y) {
            return new SumOperation(x, y);
        }
    }

    public SumOperation(int x, int y) {
        super(x, y, "Sum", "+");
    }
}
