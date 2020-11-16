package org.block.project.block.assists;

import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;

import java.util.*;

public abstract class AbstractAttachable extends AbstractBlock implements Block.AttachableBlock {

    protected final Map<String, BlockList<? extends Block>> attached;

    /**
     * Init the AbstractAttachable
     * @param x The X position
     * @param y The Y position
     * @param width The width of the block
     * @param height The height of the block
     * @param entries The blocklist for the attached
     */
    @SafeVarargs
    public AbstractAttachable(int x, int y, int width, int height, Map.Entry<String, BlockList<? extends Block>>... entries){
        this(x, y, width, height, new HashMap<>());
        for(Map.Entry<String, BlockList<? extends Block>> entry : entries){
            this.attached.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Init the AbstractAttachable
     * @param x The X position
     * @param y The Y position
     * @param width The width of the block
     * @param height The height of the block
     * @param map The blocklist for the attached
     */
    public AbstractAttachable(int x, int y, int width, int height, Map<String, BlockList<? extends Block>> map) {
        super(x, y, width, height);
        this.attached = map;
    }

    @Override
    public <B extends Block> BlockList<B> getAttachments(String section) {
        BlockList<? extends Block> list = this.attached.get(section);
        if(list == null){
            throw new IllegalArgumentException("Invalid section of '" + section + "' in block " + this.getUniqueId().toString() + " of type '" + this.getType().getName() + "'");
        }
        return (BlockList<B>) list;
    }

    @Override
    public Collection<String> getSections() {
        return this.attached.keySet();
    }
}
