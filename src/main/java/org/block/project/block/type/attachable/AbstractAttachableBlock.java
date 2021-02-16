package org.block.project.block.type.attachable;

import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.BlockGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractAttachableBlock extends AbstractBlock implements AttachableBlock {

    protected List<BlockGroup> blockGroups = new ArrayList<>();

    public AbstractAttachableBlock(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public List<BlockGroup> getGroups() {
        return this.blockGroups;
    }
}
