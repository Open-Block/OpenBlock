package org.block.project.block.type;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;

import java.util.Optional;

/**
 * If a block is directly affected by another block then the former should implement this.
 */
interface LinkedBlock extends Block {

    /**
     * Gets the linked block
     * @return A optional of the linked block, if no link is defined then {@link Optional#empty()} will be returned
     */
    Optional<Block> getLinkedBlock();

}
