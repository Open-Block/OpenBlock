package org.block.project.block.group;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;

import java.util.List;
import java.util.Optional;

public interface UnlimitedBlockGroup extends BlockGroup {

    boolean canAccept(Block block);
    boolean addSector(Block block, int placement);

    default boolean addSector(Block block){
        return addSector(block, this.getSectors().size());
    }


}
