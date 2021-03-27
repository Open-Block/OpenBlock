package org.block.project.block.java.operation.number.minus.group;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import org.block.project.block.BlockNode;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockGroupNode;
import org.block.project.block.java.operation.number.minus.MinusOperation;

public class MinusOperationGroupNode extends Region implements BlockGroupNode<MinusOperation> {

    private final BlockGroup group;

    public MinusOperationGroupNode(BlockGroup group, Node child) {
        this.group = group;
        this.getChildren().add(child);
    }

    @Override
    public void updateBlock() {
        if (this.getChildren().isEmpty()) {
            return;
        }
        var child = this.getChildren().get(0);
        if (child instanceof BlockNode) {
            ((BlockNode<?>) child).updateBlock();
        }
    }

    @Override
    public BlockGroup getGroup() {
        return this.group;
    }
}
