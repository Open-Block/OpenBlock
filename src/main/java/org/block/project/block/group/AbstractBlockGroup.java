package org.block.project.block.group;

import org.block.project.block.Block;
import org.block.project.block.BlockNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBlockGroup implements BlockGroup {

    private final String id;
    private final String name;


    public AbstractBlockGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public abstract static class AbstractListBlockGroup extends AbstractBlockGroup {

        protected final List<BlockSector<?>> blockSectors = new ArrayList<>();

        public AbstractListBlockGroup(String id, String name) {
            super(id, name);
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

    public abstract static class AbstractSingleBlockGroup<B extends Block> extends AbstractBlockGroup implements SingleBlockGroup<B> {

        protected BlockSector<B> sector;

        public AbstractSingleBlockGroup(String id, String name) {
            super(id, name);
        }

        @Override
        public BlockSector<B> getSector() {
            if (this.sector == null) {
                throw new IllegalStateException("A Single block group was created without a block sector. Fix this");
            }
            return this.sector;
        }
    }
}
