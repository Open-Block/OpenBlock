package org.block.project.block.event;

import org.block.plugin.event.Event;
import org.block.project.block.Block;

/**
 * Base interface for all Block based events
 */
public interface BlockEvent extends Event {

    /**
     * Gets the block that caused the event
     * @return The block
     */
    Block getBlock();
}
