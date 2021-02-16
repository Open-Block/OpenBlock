package org.block.project.block.group;

import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;

import java.util.*;

public class AbstractBlockGroup implements BlockGroup {

    private final String id;
    private final String name;
    private final int relativeY;
    protected final List<BlockSector<?>> blockSectors = new ArrayList<>();

    public AbstractBlockGroup(String id, String name, int relativeY){
        this.id = id;
        this.name = name;
        this.relativeY = relativeY;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<BlockSector<?>> getSectors() {
        return this.blockSectors;
    }

    @Override
    public int getRelativeYPosition() {
        return this.relativeY;
    }

    @Override
    public boolean removeSector(Block block) {
        Optional<BlockSector<?>> opSector = this.blockSectors.parallelStream().filter(s -> s.getAttachedBlock().isPresent()).filter(s -> s.getAttachedBlock().get().equals(block)).findAny();
        return opSector.map(blockSector -> this.blockSectors.remove(blockSector)).orElse(false);
    }
}
