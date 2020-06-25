package org.block.panel.context;

import org.block.panel.block.Block;

import java.util.Optional;

public class DragContext {

    private Block dragging;
    private int offX;
    private int offY;
    private Block attached;

    public Optional<Block> getAttached() {
        return Optional.ofNullable(this.attached);
    }

    public DragContext setAttached(Block attached) {
        this.attached = attached;
        return this;
    }

    public Block getDragging() {
        return this.dragging;
    }

    public DragContext setDragging(Block dragging) {
        this.dragging = dragging;
        return this;
    }

    public int getOffX() {
        return this.offX;
    }

    public DragContext setOffX(int offX) {
        this.offX = offX;
        return this;
    }

    public int getOffY() {
        return this.offY;
    }

    public DragContext setOffY(int offY) {
        this.offY = offY;
        return this;
    }
}
