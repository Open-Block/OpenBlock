package org.block.project.block.java.operation.number;

import javafx.scene.canvas.GraphicsContext;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.assists.BlockList;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockSector;
import org.block.project.panel.main.FXMainDisplay;
import org.block.serialization.ConfigNode;
import org.block.util.OrderedUniqueList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class MinusOperation extends AbstractNumberOperation {

    public class MinusBlockGraphic implements BlockGraphics {

        private Block origin = MinusOperation.this.getA

        @Override
        public void draw(GraphicsContext context) {

        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
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
                if(!opBlock.isPresent()){
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if(!(opBlock.get() instanceof ValueBlock)){
                    throw new IllegalStateException("Attached block was not a value block");
                }
                blockList.setAttachment(A, (ValueBlock<? extends Number>) opBlock.get());
            }
            return minusBlock;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull MinusOperation block) {
            BlockType.super.write(node, block);
            List<UUID> list = new ArrayList<>();
            BlockList<ValueBlock<? extends Number>> blockList = block.getAttachments();
            for(int A = 0; A < blockList.getMaxAttachments(); A++){
                blockList.getAttachment(A).ifPresent(b -> list.add(b.getUniqueId()));
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

    private static final String FIRST_PARAMETER = "First";
    private static final String SECOND_PARAMETER = "Second";


    /**
     * The constructor for MinusOperator
     *
     * @param x        The X position
     * @param y        The Y position
     */
    public MinusOperation(int x, int y) {
        super(x, y, "Minus", "-");


    }

    public BlockSector<ValueBlock<? extends Number>> getFirstSector(){
        return (BlockSector<ValueBlock<? extends Number>>) this.getGroup(FIRST_PARAMETER).get().getSectors().get(0);
    }

    public BlockSector<ValueBlock<? extends Number>> getSecondarySector(){
        return (BlockSector<ValueBlock<? extends Number>>) this.getGroup(SECOND_PARAMETER).get().getSectors().get(0);
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
