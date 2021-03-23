package org.block.project.block.group;

import org.block.project.block.Block;
import org.vector.type.Vector2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BlockGroup {

    String getId();

    String getName();

    Block getBlock();

    BlockGroupNode<? extends Block> getBlockNode();

    List<BlockSector<?>> getSectors();

    default double getRelativeYPosition() {
        return this.getBlockNode().getBoundsInParent().getMinY();
    }

    default double getRelativeXPosition() {
        return this.getBlockNode().getBoundsInParent().getMinY();
    }

    boolean removeSector(Block block);

    default double getHeight() {
        double height = 0;
        for (BlockSector<?> sector : this.getSectors()) {
            height = Math.max(sector.getHeight(), height);
        }
        return height;
    }

    default double getWidth() {
        double width = 0;
        for (BlockSector<?> sector : this.getSectors()) {
            width = Math.max(sector.getWidth(), width);
        }
        return width;
    }

    default boolean contains(Vector2<Double> vector2) {
        return contains(vector2.getX(), vector2.getZ());
    }

    default boolean contains(double x, double y) {
        double relX = this.getRelativeXPosition();
        double relY = this.getRelativeYPosition();
        if (y < relY) {
            return false;
        }
        if (x < relX) {
            return false;
        }
        double differenceX = x - relX;
        double differenceY = y - relY;
        if (differenceX >= getWidth()) {
            return false;
        }
        return !(differenceY >= getHeight());
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
