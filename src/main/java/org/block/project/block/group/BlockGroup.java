package org.block.project.block.group;

import org.block.project.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BlockGroup {

    /**
     * Gets the id of the block group.
     *
     * @return The id the block group
     */
    String getId();

    /**
     * Gets the name of the block group.
     * This will be the name displayed if a error directly relates
     * to this group.
     * <p>
     * For example: "The block 'Block type' required a number block within 'this name' sector"
     *
     * @return
     */
    String getName();

    /**
     * Gets the sectors within the block group. This will typically be unmodifiable however if the
     * blockgroup allows unlimited then it will be modifiable.
     *
     * @return
     */
    List<BlockSector<?>> getSectors();

    /**
     * Gets the Y position related to its parent
     *
     * @return Y position related to its parent
     */
    int getRelativeYPosition();

    void setRelativeYPosition(int pos);

    boolean removeSector(Block block);

    default int getHeight() {
        int height = 0;
        for (BlockSector<?> sector : this.getSectors()) {
            height = Math.max(sector.getHeight(), height);
        }
        return height;
    }

    default int getWidth() {
        int width = 0;
        for (BlockSector<?> sector : this.getSectors()) {
            width = Math.max(sector.getWidth(), width);
        }
        return width;
    }

    default List<BlockSector<?>> getApplicableSectors(Block block) {
        return this.getSectors().parallelStream().filter(s -> s.canAccept(block)).collect(Collectors.toList());
    }

    default Optional<BlockSector<?>> getRelativeSector(int y) {
        int relativeCheck = 0;
        for (BlockSector<?> sector : this.getSectors()) {
            relativeCheck += sector.getHeight();
            if (relativeCheck >= y) {
                return Optional.of(sector);
            }
        }
        return Optional.empty();
    }
}
