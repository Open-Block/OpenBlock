package org.block.panel.block.event;

import org.block.panel.block.Block;

/**
 * Base interface for all Block based events
 */
public interface BlockEvent {

    /**
     * Gets the block that caused the event
     * @return The block
     */
    Block getBlock();
}
