package org.block.panel.block;

import org.block.panel.block.event.BlockEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractBlock implements Block.TextBlock {

    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected String text;
    protected boolean selected;
    protected boolean highlighted;
    protected Set<BlockEvent> events = new HashSet<>();

    public AbstractBlock(int x, int y, int width, int height, String text){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isHighlighted(){
        return this.highlighted;
    }

    @Override
    public void setHighlighted(boolean check){
        this.highlighted = check;
    }

    @Override
    public Collection<BlockEvent> getEvents() {
        return Collections.unmodifiableCollection(this.events);
    }

    @Override
    public void registerEvent(BlockEvent event) {
        this.events.add(event);
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
