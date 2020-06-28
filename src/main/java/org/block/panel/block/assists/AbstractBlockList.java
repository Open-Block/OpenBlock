package org.block.panel.block.assists;

import org.block.panel.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBlockList<T extends Block> implements BlockList<T> {

    protected class BlockListIterator implements Iterator<T>{

        private int target = 0;

        @Override
        public boolean hasNext() {
            for(int next = this.target; next < AbstractBlockList.this.blocks.size(); next++){
                if(AbstractBlockList.this.blocks.get(next) != null){
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            for(int next = this.target; next < AbstractBlockList.this.blocks.size(); next++){
                T block = AbstractBlockList.this.blocks.get(next);
                if(block != null){
                    this.target = next + 1;
                    return block;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    private final int maxAttachment;
    private final int defaultHeight;
    private final List<T> blocks = new ArrayList<>();

    public AbstractBlockList(int maxAttachment, int defaultHeight){
        this.maxAttachment = maxAttachment;
        this.defaultHeight = defaultHeight;
    }

    @Override
    public int getMaxAttachments() {
        return (this.maxAttachment != -1) ? this.maxAttachment : this.blocks.size() + 1;
    }

    @Override
    public int getSlotHeight(int slot) {
        Optional<T> opSlot = this.getAttachment(slot);
        return opSlot.map(Block::getHeight).orElse(this.defaultHeight);
    }

    @Override
    public Optional<T> getAttachment(int index) {
        if(index >= this.getMaxAttachments()){
            throw new IndexOutOfBoundsException(index + " is out of range of 0 - " + this.getMaxAttachments());
        }
        if(index >= this.blocks.size()){
            return Optional.empty();
        }
        return Optional.ofNullable(this.blocks.get(index));
    }

    @Override
    public void setAttachment(int index, T block) {
        int oldSize = this.blocks.size();
        if(index >= this.getMaxAttachments()){
            throw new IndexOutOfBoundsException(index + " is out of range of 0 - " + this.getMaxAttachments());
        }
        if(index >= this.blocks.size()){
            for(int A = this.blocks.size(); A <= index; A++){
                this.blocks.add(null);
            }
        }
        this.blocks.set(index, block);
        System.out.println("Added block to " + index + ". New Size: " + this.blocks.size() + " | Old Size: " + oldSize);
        int x = this.getParent().getX() + this.getXPosition(index);
        int y = this.getParent().getY() + this.getYPosition(index);
        block.setX(x);
        block.setY(y);
    }

    @Override
    public void removeAttachment(Block block) {
        if(this.maxAttachment == -1) {
            this.blocks.remove(block);
            for(int A = 0; A < this.blocks.size(); A++){
                if (this.blocks.get(A) == null){
                    this.blocks.remove(A);
                }
            }
        }else{
            for(int A = 0; A < this.blocks.size(); A++){
                Block block2 = this.blocks.get(A);
                if(block2.equals(block)){
                    this.blocks.set(A, null);
                }
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BlockListIterator();
    }
}
