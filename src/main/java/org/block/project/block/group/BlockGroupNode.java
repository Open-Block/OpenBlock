package org.block.project.block.group;

import org.block.project.block.BlockNode;
import org.block.project.block.type.attachable.AttachableBlock;

public interface BlockGroupNode<B extends AttachableBlock> extends BlockNode<B> {

    BlockGroup getGroup();

    @Override
    default B getBlock() {
        return (B) this.getGroup().getBlock();
    }
}
