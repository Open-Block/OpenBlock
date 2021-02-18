package org.block.project.block.type.attachable;

import org.block.project.block.AbstractBlock;
import org.block.project.block.group.BlockGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAttachableBlock extends AbstractBlock implements AttachableBlock {

    protected List<BlockGroup> blockGroups = new ArrayList<>();

    public AbstractAttachableBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public List<BlockGroup> getGroups() {
        return this.blockGroups;
    }
}
