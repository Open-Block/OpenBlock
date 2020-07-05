package org.block.project.block;

import org.block.project.block.event.BlockEvent;
import org.block.plugin.event.EventListener;

import java.util.*;

/**
 * The most common implementation for a Block, while this does not provided all functions,
 * it does provided majority of the default functions.
 * This also takes on {@link Block.TextBlock} as all default blocks will have text of some sort
 */
public abstract class AbstractBlock implements Block {

    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected boolean selected;
    protected boolean highlighted;
    protected boolean error;
    protected Set<EventListener<? extends BlockEvent>> events = new HashSet<>();
    protected UUID id;
    protected Block.AttachableBlock attachedTo;
    protected int layer;

    public AbstractBlock(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    public boolean isShowingError() {
        return this.error;
    }

    @Override
    public void setShowingError(boolean error) {
        this.error = error;
    }

    @Override
    public Collection<EventListener<? extends BlockEvent>> getEvents() {
        return Collections.unmodifiableCollection(this.events);
    }

    @Override
    public void registerEventListener(EventListener<? extends BlockEvent> event) {
        this.events.add(event);
    }

    @Override
    public Optional<Block.AttachableBlock> getAttachedTo() {
        return Optional.ofNullable(this.attachedTo);
    }

    @Override
    public void setAttachedTo(Block.AttachableBlock block) {
        this.attachedTo = block;
    }

    @Override
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
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
