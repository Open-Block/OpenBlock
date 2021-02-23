package org.block.project.context;

import org.block.project.block.Block;

import java.util.Optional;

/**
 * All information about a block being dragged.
 */
public class DragContext {

    private Block dragging;
    private int offX;
    private int offY;
    private Block attached;

    /**
     * If the dragging block has been attached to another block, then that will be provided here.
     *
     * @return A optional value of the attached block to the block being dragged.
     */
    public Optional<Block> getAttached() {
        return Optional.ofNullable(this.attached);
    }

    /**
     * Sets the block that is attaching the dragged block
     *
     * @param attached The attached block
     * @return itself for chaining
     * @throws IllegalArgumentException if the provided block does not have the dragging block attached
     */
    public DragContext setAttached(Block attached) {
        this.attached = attached;
        return this;
    }

    /**
     * Gets the block being dragged
     *
     * @return The block being dragged
     */
    public Block getDragging() {
        return this.dragging;
    }

    /**
     * Sets the block being dragged.
     * Please note that this may not work depending on the implementation of the dragging
     *
     * @param dragging The block being dragged
     * @return Itself for chaining
     */
    public DragContext setDragging(Block dragging) {
        this.dragging = dragging;
        return this;
    }

    /**
     * Gets the X offset. This prevents the annoyance of Blocks moving way too much after a slight drag.
     *
     * @return The X offset in pixels
     */
    public int getOffX() {
        return this.offX;
    }

    /**
     * Sets the X offset.
     *
     * @param offX The X offset
     * @return Itself for chaining
     */
    public DragContext setOffX(int offX) {
        this.offX = offX;
        return this;
    }

    /**
     * Gets the Y offset. This prevents the annoyance of Blocks moving way too much after a slight drag.
     *
     * @return The Y offset in pixels
     */
    public int getOffY() {
        return this.offY;
    }

    /**
     * Sets the Y offset.
     *
     * @param offY The Y offset
     * @return Itself for chaining
     */
    public DragContext setOffY(int offY) {
        this.offY = offY;
        return this;
    }
}
