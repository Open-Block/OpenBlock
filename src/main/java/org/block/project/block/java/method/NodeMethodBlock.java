package org.block.project.block.java.method;

import javafx.scene.layout.VBox;
import org.block.project.block.BlockNode;

public class NodeMethodBlock extends VBox implements BlockNode<MethodBlock> {

    private final MethodBlock block;

    public NodeMethodBlock(MethodBlock block) {
        this.block = block;
    }

    @Override
    public MethodBlock getBlock() {
        return this.block;
    }

    @Override
    public void updateBlock() {
        throw new IllegalStateException("Not Implemented");
    }
}
