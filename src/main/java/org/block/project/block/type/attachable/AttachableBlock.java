package org.block.project.block.type.attachable;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockSector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * If the Block requires additional information to function properly that is in code form, then it should implement Attachable.
 * This allows users to add blocks directly to this block
 */
public interface AttachableBlock extends Block {

    interface Single extends AttachableBlock {

        BlockGroup getGroup();

        default List<BlockGroup> getGroups(){
            return Arrays.asList(this.getGroup());
        }

    }

    /**
     * Gets all attached block groups
     * @return A unmodifiable list of all block groups
     */
    List<BlockGroup> getGroups();

    /**
     * Gets the block group with the specified id
     * @param id The block groups id
     * @return The BlockGroup with the provided id, if none was found then returns {@link Optional#empty}
     */
    default Optional<BlockGroup> getGroup(String id){
        return this.getGroups().parallelStream().filter(g -> g.getId().equalsIgnoreCase(id)).findFirst();
    }

    default Optional<BlockGroup> getGroup(int y){
        return this.getGroups().parallelStream().filter(g -> g.getRelativeSector(y - g.getRelativeYPosition()).isPresent()).findFirst();
    }

    default List<BlockGroup> getApplicableGroups(Block block){
        return this.getGroups().parallelStream().filter(g -> !g.getApplicableSectors(block).isEmpty()).collect(Collectors.toList());
    }

    default List<BlockSector<?>> getApplicableSectors(Block block){
        List<BlockSector<?>> list = new ArrayList<>();
        this.getGroups().parallelStream().forEach(g -> g.getSectors().parallelStream()
                .filter(s -> s.getAttachedBlock().isPresent())
                .filter(s -> s.canAccept(block))
                .forEach(list::add));
        return list;
    }

    default List<BlockSector<?>> getSectors(Block block){
        List<BlockSector<?>> list = new ArrayList<>();
        this.getGroups().parallelStream().forEach(g -> g.getSectors().parallelStream()
                .filter(s -> s.getAttachedBlock().isPresent())
                .filter(s -> s.getAttachedBlock().get().equals(block))
                .forEach(list::add));
        return list;
    }

    default boolean containsBlock(Block block){
        return this.getGroups()
                .parallelStream()
                .anyMatch(g -> g.getSectors().parallelStream()
                        .filter(s -> s.getAttachedBlock().isPresent())
                        .anyMatch(s -> s.getAttachedBlock().get().equals(block)));
    }

    default void removeBlock(Block block){
        this.getSectors(block).forEach(BlockSector::removeAttachedBlock);
    }

    @Override
    default void delete() {
        Block.super.delete();
        for(BlockGroup group : this.getGroups()){
            List<Block> blocks = group.getSectors().parallelStream().filter(s -> s.getAttachedBlock().isPresent()).map(s -> s.getAttachedBlock().get()).collect(Collectors.toList());
            blocks.forEach(group::removeSector);
        }
    }

    @Override
    default void update() {
        Block.super.update();
    }
}
