package org.block.project.block.group;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.block.project.block.Block;
import org.block.project.block.BlockNode;

public class EmptyBlockSectorNode extends Region implements BlockNode<Block> {

    private final BlockSector<?> sector;

    public EmptyBlockSectorNode(BlockSector<?> sector) {
        this.sector = sector;
        init();
    }

    private void init() {
        this.setWidth(10);
        this.setHeight(10);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    @Override
    public void setHeight(double v) {
        super.setHeight(v);
        this.setMinHeight(v);
    }

    @Override
    protected void setWidth(double v) {
        super.setWidth(v);
        this.setMinWidth(v);
    }

    @Override
    public Block getBlock() {
        return this.sector.getParent().getBlock();
    }

    @Override
    public void updateBlock() {
        this.sector.getParent().getBlock();
    }
}
