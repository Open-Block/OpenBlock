package org.block.project.block;

import org.block.project.block.type.called.CodeStartBlock;

import java.util.*;

public abstract class AbstractBlock implements Block {

    protected int x;
    protected int y;
    protected boolean selected;
    protected boolean highlighted;
    protected boolean error;
    protected UUID id;
    protected int layer;
    protected CodeStartBlock codeStartBlock;

    public AbstractBlock(int x, int y){
        this.x = x;
        this.y = y;
        this.id = UUID.randomUUID();
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
    public boolean isHighlighted() {
        return this.highlighted;
    }

    @Override
    public void setHighlighted(boolean selected) {
        this.highlighted = selected;
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
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public UUID getUniqueId() {
        return this.id;
    }

    @Override
    public Optional<CodeStartBlock> getParent() {
        return Optional.ofNullable(this.codeStartBlock);
    }

    @Override
    public void setParent(CodeStartBlock block) {
this.codeStartBlock = block;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Block)){
            return false;
        }
        Block block = (Block)obj;
        return this.getUniqueId().equals(block.getUniqueId());
    }
}
