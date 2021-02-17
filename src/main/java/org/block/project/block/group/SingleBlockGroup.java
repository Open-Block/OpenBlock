package org.block.project.block.group;

import org.block.project.block.Block;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface SingleBlockGroup<B extends Block> extends BlockGroup {

    BlockSector<B> getSector();

    @Override
    default List<BlockSector<?>> getSectors(){
        return Collections.singletonList(getSector());
    }

    @Deprecated
    @Override
    default boolean removeSector(Block block){
        getSector().removeAttachedBlock();
        return true;
    }
}
