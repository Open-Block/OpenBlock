package org.block.project.block.group;

import org.block.project.block.Block;

public interface UnlimitedBlockGroup extends BlockGroup {

    boolean canAccept(Block block);

    boolean addSector(Block block, int placement);

    default boolean addSector(Block block) {
        return addSector(block, this.getSectors().size());
    }


}
