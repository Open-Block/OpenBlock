package org.block.project.block.group;

import org.block.project.block.Block;

import java.util.*;

public abstract class AbstractBlockGroup implements BlockGroup {

    public static class AbstractListBlockGroup extends AbstractBlockGroup {

        protected final List<BlockSector<?>> blockSectors = new ArrayList<>();

        public AbstractListBlockGroup(String id, String name, int relativeY) {
            super(id, name, relativeY);
        }

        @Override
        public List<BlockSector<?>> getSectors() {
            return this.blockSectors;
        }

        @Override
        public boolean removeSector(Block block) {
            Optional<BlockSector<?>> opSector = this.blockSectors.parallelStream().filter(s -> s.getAttachedBlock().isPresent()).filter(s -> s.getAttachedBlock().get().equals(block)).findAny();
            return opSector.map(this.blockSectors::remove).orElse(false);
        }
    }

    public static class AbstractSingleBlockGroup<B extends Block> extends AbstractBlockGroup implements SingleBlockGroup<B> {

        protected BlockSector<B> sector;

        public AbstractSingleBlockGroup(String id, String name, int relativeY) {
            super(id, name, relativeY);
        }

        @Override
        public BlockSector<B> getSector() {
            if(this.sector == null){
                throw new IllegalStateException("A Single block group was created without a block sector. Fix this");
            }
            return this.sector;
        }
    }

    private final String id;
    private final String name;
    private int relativeY;

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
    public int getRelativeYPosition() {
        return this.relativeY;
    }

    @Override
    public void setRelativeYPosition(int pos) {
        this.relativeY = pos;
    }
}
