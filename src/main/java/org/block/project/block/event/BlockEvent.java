package org.block.project.block.event;

import org.block.plugin.event.Event;
import org.block.project.block.Block;
import org.block.project.block.event.value.BlockEditValueEvent;

/**
 * Base interface for all Block based events
 */
public interface BlockEvent extends Event {

    interface Cancelable extends BlockEvent {

        boolean isCancelled();
        void setCancelled(boolean check);

    }

    /**
     * Gets the block that caused the event
     * @return The block
     */
    Block getBlock();
}
