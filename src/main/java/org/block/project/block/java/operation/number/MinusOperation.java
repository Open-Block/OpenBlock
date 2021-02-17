package org.block.project.block.java.operation.number;

import javafx.scene.canvas.GraphicsContext;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.group.AbstractBlockSector;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockSector;
import org.block.project.panel.main.FXMainDisplay;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class MinusOperation extends AbstractNumberOperation {

    public class MinusBlockGraphic implements BlockGraphics {

        private final int marginX = 5;
        private final int marginY = 5;

        @Override
        public void draw(GraphicsContext context) {

        }

        @Override
        public int getWidth() {
            int width = 15;
            for (BlockGroup group : MinusOperation.this.getGroups()) {
                width = Math.max(width, group.getWidth());
            }
            return width + this.marginX;
        }

        @Override
        public int getHeight() {
            int height = 0;
            for (BlockGroup group : MinusOperation.this.getGroups()) {
                height = height + group.getHeight();
            }
            return height + this.marginY;
        }
    }

    public static class MinusBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<ValueBlock<? extends Number>> {

        public MinusBlockGroup(String id, String name, ValueBlock<? extends Number> block) {
            super(id, name, 25);
            this.sector = new AbstractBlockSector<>(this,
                    (Class<ValueBlock<? extends Number>>)(Object) ValueBlock.class,
                    block,
                    b -> b.getExpectedValue().isPresent());
        }
    }

    public static class MinusOperationType implements BlockType<MinusOperation> {

        @Override
        public MinusOperation build(int x, int y) {
            return new MinusOperation(x, y);
        }

        @Override
        public MinusOperation build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(opUUID.isEmpty()){
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(opX.isEmpty()){
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(opY.isEmpty()){
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();
            FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getSceneSource();
            List<Block> blocks = panel.getDisplayingBlocks();
            MinusOperation minusBlock = new MinusOperation(opX.get(), opY.get());
            List<BlockGroup> groups = minusBlock.getGroups();
            minusBlock.id = opUUID.get();
            for(int A = 0; A < connected.size(); A++){
                UUID uuid = connected.get(A);
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if(opBlock.isEmpty()){
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if(!(opBlock.get() instanceof ValueBlock)){
                    throw new IllegalStateException("Attached block was not a value block");
                }
                groups.add(A, new MinusBlockGroup("minus:index" + A, "index " + A, opBlock.map(b -> (ValueBlock<? extends Number>)b).get()));
            }
            return minusBlock;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull MinusOperation block) {
            BlockType.super.write(node, block);
            List<UUID> list = new ArrayList<>();
            List<BlockGroup> blockLists = block.getGroups();
            for (BlockGroup blockList : blockLists) {
                blockList.getSectors().get(0).getAttachedBlock().ifPresent(b -> list.add(b.getUniqueId()));
            }
            TITLE_DEPENDS.serialize(node, list);
        }

        @Override
        public File saveLocation() {
            return new File("blocks/maths/minus/");
        }

        @Override
        public String getName() {
            return "Minus";
        }
    }

    /**
     * The constructor for MinusOperator
     *
     * @param x        The X position
     * @param y        The Y position
     */
    public MinusOperation(int x, int y) {
        super(x, y, "Minus", "-");
    }

    @Override
    public MinusBlockGraphic getGraphicShape() {
        return new MinusBlockGraphic();
    }

    @Override
    public BlockType<? extends Block> getType() {
        return BlockType.BLOCK_TYPE_MINUS;
    }
}
