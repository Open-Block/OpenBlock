package org.block.panel.block.assists;

import org.block.panel.block.AbstractBlock;
import org.block.panel.block.Block;

import java.util.*;

public abstract class AbstractAttachable extends AbstractBlock implements Block.AttachableBlock {

    protected final Map<String, BlockList<? extends Block>> attached;

    public AbstractAttachable(int x, int y, int width, int height, String text, Map.Entry<String, BlockList<? extends Block>>... entries){
        this(x, y, width, height, text, new HashMap<>());
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
     * @param text The text to display on the block
     */
    public AbstractAttachable(int x, int y, int width, int height, String text, Map<String, BlockList<? extends Block>> map) {
        super(x, y, width, height, text);
        this.attached = map;
    }

    @Override
    public <B extends Block> BlockList<B> getAttachments(String section) {
        BlockList<? extends Block> list = this.attached.get(section);
        if(list == null){
            throw new IllegalArgumentException("Invalid section");
        }
        return (BlockList<B>) list;
    }

    @Override
    public Collection<String> getSections() {
        return this.attached.keySet();
    }
}
