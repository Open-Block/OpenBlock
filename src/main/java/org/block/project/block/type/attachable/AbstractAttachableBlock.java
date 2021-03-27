package org.block.project.block.type.attachable;

import org.block.project.block.AbstractBlock;
import org.block.project.block.group.BlockGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractAttachableBlock extends AbstractBlock implements AttachableBlock {

    protected List<BlockGroup> blockGroups = new ArrayList<>();

    public AbstractAttachableBlock(UUID uuid) {
        super(uuid);
    }

    @Override
    public List<BlockGroup> getGroups() {
        return this.blockGroups;
    }
}
