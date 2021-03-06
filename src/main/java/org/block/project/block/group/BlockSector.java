package org.block.project.block.group;

import org.block.project.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BlockSector<B extends Block> {

    BlockGroup getParent();

    Optional<B> getAttachedBlock();

    void setAttachedBlock(@Nullable Block block) throws IllegalArgumentException;

    boolean canAccept(Block block);

    double getHeight();

    double getWidth();

    default void removeAttachedBlock() {
        this.setAttachedBlock(null);
    }
}
