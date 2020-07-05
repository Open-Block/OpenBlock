package org.block.project.block.event.mouse;

import org.block.project.block.Block;

import java.awt.event.MouseEvent;

public class BlockMouseClickEvent implements BlockMouseEvent.Button {

    private final MouseEvent event;
    private final Block clicked;

    public BlockMouseClickEvent(Block block, MouseEvent event){
        this.event = event;
        this.clicked = block;
    }

    @Override
    public int getButton() {
        return this.event.getButton();
    }

    @Override
    public int getClickCount() {
        return this.event.getClickCount();
    }

    @Override
    public int getX() {
        return this.event.getX();
    }

    @Override
    public int getScreenX() {
        return this.event.getXOnScreen();
    }

    @Override
    public int getY() {
        return this.event.getY();
    }

    @Override
    public int getScreenY() {
        return this.event.getYOnScreen();
    }

    @Override
    public void cancel() {
        this.event.consume();
    }

    @Override
    public Block getBlock() {
        return this.clicked;
    }
}
