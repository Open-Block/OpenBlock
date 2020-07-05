package org.block.project.block.assists;

import org.block.project.block.Block;

import java.util.Iterator;
import java.util.Optional;

public abstract class AbstractSingleBlockList<T extends Block> implements BlockList.Single<T> {

    protected class BlockListIterator implements Iterator<T>{

        private boolean used;

        @Override
        public boolean hasNext() {
            return !this.used;
        }

        @Override
        public T next() {
            if(!this.used){
                this.used = true;
                return AbstractSingleBlockList.this.value;
            }
            throw new IndexOutOfBoundsException();
        }
    }

    private T value;
    private final int height;

    public AbstractSingleBlockList(int height){
        this(height, null);
    }

    public AbstractSingleBlockList(int height, T value){
        this.value = value;
        this.height = height;
    }

    @Override
    public Optional<T> getAttachment() {
        return Optional.ofNullable(this.value);
    }

    @Override
    public void setAttachment(T block) {
        this.value = block;
    }

    @Override
    public Optional<T> removeAttachment() {
        T value = this.value;
        this.setAttachment(null);
        return Optional.ofNullable(value);
    }

    @Override
    public int getSlotHeight(int slot) {
        if(slot != 0){
            throw new IndexOutOfBoundsException();
        }
        if(this.value == null){
            return this.height;
        }
        return this.value.getHeight();
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractSingleBlockList.BlockListIterator();
    }
}
