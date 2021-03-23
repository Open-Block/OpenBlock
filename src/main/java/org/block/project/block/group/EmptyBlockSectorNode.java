package org.block.project.block.group;

import javafx.scene.layout.Region;

public class EmptyBlockSectorNode extends Region {

    private final BlockSector<?> sector;

    public EmptyBlockSectorNode(BlockSector<?> sector) {
        this.sector = sector;
    }
}
