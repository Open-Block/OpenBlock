package org.block.project.block.java.operation.number;

import javafx.scene.layout.VBox;
import org.block.project.block.BlockNode;

public class NodeNumberOperation<N extends AbstractNumberOperation> extends VBox implements BlockNode<N> {

    private final N numberOperation;

    public NodeNumberOperation(N numberOperation) {
        this.numberOperation = numberOperation;
    }

    @Override
    public N getBlock() {
        return this.numberOperation;
    }
}
