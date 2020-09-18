package org.block.project.block.java.operation;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.assists.BlockList;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.util.*;

public class MinusOperation extends AbstractNumberOperation {

    public static class MinusOperationType implements BlockType<MinusOperation> {

        @Override
        public MinusOperation build(int x, int y) {
            return new MinusOperation(x, y);
        }

        @Override
        public MinusOperation build(ConfigNode node) {
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
            MainDisplayPanel panel = Blocks.getInstance().getLoadedProject().get().getPanel();
            NavigableSet<Block> blocks = panel.getBlockPanel().getBlocks();
            MinusOperation minusBlock = new MinusOperation(opX.get(), opY.get());
            BlockList<ValueBlock<? extends Number>> blockList = minusBlock.getAttachments();
            minusBlock.id = opUUID.get();
            for(int A = 0; A < connected.size(); A++){
                UUID uuid = connected.get(A);
                System.out.println("Blocks: " + ArrayUtils.toString(", ", b -> b.getUniqueId().toString() , blocks));
                Optional<Block> opBlock = blocks.stream().filter(b -> {
                    System.out.println("Sum Compare: " + b.getUniqueId().toString() + " | " + uuid.toString() + " | " + b.getUniqueId().equals(uuid));
                    return b.getUniqueId().equals(uuid);
                }).findAny();
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
        public void write(ConfigNode node, MinusOperation block) {
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
    public BlockType<? extends Block> getType() {
        return null;
    }
}