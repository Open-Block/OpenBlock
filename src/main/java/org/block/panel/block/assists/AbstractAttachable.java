package org.block.panel.block.assists;

import org.block.panel.block.AbstractBlock;
import org.block.panel.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAttachable extends AbstractBlock implements Block.AttachableBlock {

    protected int maxAttachment;
    protected final List<Block> attachment = new ArrayList<>();

    /**
     * Init the AbstractAttachable
     * @param x The X position
     * @param y The Y position
     * @param width The width of the block
     * @param height The height of the block
     * @param text The text to display on the block
     * @param maxAttachment The max amount of attachments, use -1 for unlimited
     */
    public AbstractAttachable(int x, int y, int width, int height, String text, int maxAttachment) {
        super(x, y, width, height, text);
        this.maxAttachment = maxAttachment;
    }

    @Override
    public int getMaxAttachments() {
        return (this.maxAttachment == -1 ? this.attachment.size() + 1 : this.maxAttachment);
    }

    @Override
    public Optional<Block> getAttachment(int index) {
        if(index >= this.getMaxAttachments()){
            throw new IndexOutOfBoundsException(index + " is out of range of 0 - " + (this.maxAttachment - 1));
        }
        if(index >= this.attachment.size()){
            return Optional.empty();
        }
        return Optional.ofNullable(this.attachment.get(index));
    }

    @Override
    public void setAttachment(int index, Block block) {
        if(index >= this.getMaxAttachments()){
            throw new IndexOutOfBoundsException(index + " is out of range of 0 - " + (this.maxAttachment - 1));
        }
        if(!this.canAcceptAttachment(index, block)){
            throw new IllegalArgumentException("Block can not be accepted in that slot, read documentation for what can be accepted");
        }
        this.attachment.add(index, block);
    }

    @Override
    public void removeAttachment(Block block) {
        this.attachment.remove(block);
    }
}
