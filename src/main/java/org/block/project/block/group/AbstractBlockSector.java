package org.block.project.block.group;

import org.block.project.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class AbstractBlockSector<B extends Block> implements BlockSector<B> {

    private B block;
    private final Class<B> generic;
    private final BlockGroup parent;
    private final Predicate<Block> acceptable;

    public AbstractBlockSector(BlockGroup parent, Class<B> clazz){
        this(parent, clazz, null);
    }

    public AbstractBlockSector(BlockGroup parent, Class<B> clazz, @Nullable B block){
        this(parent, clazz, block, t -> true);
    }

    public AbstractBlockSector(BlockGroup parent, Class<B> clazz, @Nullable B block, Predicate<Block> acceptable){
        this.block = block;
        this.parent = parent;
        this.generic = clazz;
        this.acceptable = acceptable;
    }
    @Override
    public BlockGroup getParent() {
        return this.parent;
    }

    @Override
    public Optional<B> getAttachedBlock() {
        return Optional.ofNullable(this.block);
    }

    @Override
    public void setAttachedBlock(@Nullable Block block) throws IllegalArgumentException {
        if(block != null){
            if(!this.generic.isInstance(block)){
                throw new IllegalArgumentException("Block (" + block.toString() + ") is not of class (" + this.generic.getTypeName() + ")");
            }
            if(!this.acceptable.test((B)block)){
                throw new IllegalArgumentException("Block was rejected");
            }
        }

        this.block = (B)block;
    }

    @Override
    public boolean canAccept(Block block) {
        if(!this.generic.isInstance(block)){
            return false;
        }
        return this.acceptable.test((B) block);
    }

    @Override
    public int getHeight() {
        if(this.block == null){
            return 20;
        }
        return this.block.getHeight();
    }

    @Override
    public int getWidth() {
        if(this.block == null){
            return 50;
        }
        return this.block.getWidth();
    }
}
