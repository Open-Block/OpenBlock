package org.block.panel.block;

import org.block.panel.block.event.BlockEvent;

import java.util.*;

/**
 * The most common implementation for a Block, while this does not provided all functions,
 * it does provided majority of the default functions.
 * This also takes on {@link Block.TextBlock} as all default blocks will have text of some sort
 */
public abstract class AbstractBlock implements Block.TextBlock {

    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected String text;
    protected boolean selected;
    protected boolean highlighted;
    protected Set<BlockEvent> events = new HashSet<>();
    protected UUID id;

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

    @Override
    public UUID getUniqueId(){
        if(this.id == null){
            this.id = UUID.randomUUID();
        }
        return this.id;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Block)){
            return false;
        }
        return ((Block)obj).getUniqueId().equals(this.id);
    }
}
