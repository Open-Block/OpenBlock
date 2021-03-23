package org.block.project.block.java.method;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.block.project.block.group.BlockGroupNode;
import org.block.project.block.group.EmptyBlockSectorNode;

public class NodeMethodNameBlock extends HBox implements BlockGroupNode<MethodBlock> {

    private final MethodBlock.NameBlockGroup group;

    public NodeMethodNameBlock(MethodBlock.NameBlockGroup group) {
        this.group = group;
        this.updateBlock();
    }

    public void updateBlock(){
        this.getChildren().clear();
        this.getChildren().add(createLabel());
        var opAttached = this.group.getSector().getAttachedBlock();
        if(opAttached.isEmpty()){
            this.getChildren().add(new EmptyBlockSectorNode(this.group.getSector()));
            return;
        }
        this.getChildren().add((Node)opAttached.get().getNode());
    }

    @Override
    public MethodBlock.NameBlockGroup getGroup() {
        return this.group;
    }

    private Label createLabel(){
        return new Label("Name");
    }
}
