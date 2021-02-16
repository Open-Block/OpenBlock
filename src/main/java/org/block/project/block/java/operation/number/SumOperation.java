package org.block.project.block.java.operation.number;

import org.block.Blocks;
import org.block.project.block.BlockGraphics;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.assists.BlockList;
import org.block.project.panel.main.FXMainDisplay;
import org.block.serialization.ConfigNode;
import org.block.util.OrderedUniqueList;

import java.io.File;
import java.util.*;

/**
 * The implementation for sum Blocks
 */
public class SumOperation extends AbstractNumberOperation {

    /**
     * The BlockType for the Sum Block.
     */
    public static class SumOperationType implements BlockType<SumOperation> {

        @Override
        public SumOperation build(int x, int y) {
            return new SumOperation(x, y);
        }

        @Override
        public SumOperation build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(!opUUID.isPresent()){
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(!opX.isPresent()){
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(!opY.isPresent()){
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();

            FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getSceneSource();
            List<Block> blocks = panel.getDisplayingBlocks();
            SumOperation sumBlock = new SumOperation(opX.get(), opY.get());
            BlockList<ValueBlock<? extends Number>> blockList = sumBlock.getAttachments();
            sumBlock.id = opUUID.get();
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
            return sumBlock;
        }

        @Override
        public void write(ConfigNode node, SumOperation block) {
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
            return new File("blocks/maths/sum/");
        }

        @Override
        public String getName() {
            return "Sum";
        }
    }

    public SumOperation(int x, int y) {
        super(x, y, "Sum", "+");
    }

    @Override
    public BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_SUM;
    }
}
