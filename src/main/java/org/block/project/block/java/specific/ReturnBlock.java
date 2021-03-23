package org.block.project.block.java.specific;

import org.block.Blocks;
import org.block.panel.main.FXMainDisplay;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.BlockNode;
import org.block.project.block.group.BlockGroupNode;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class ReturnBlock extends AbstractAttachableBlock {

    public static final String RETURN_BLOCK = "return:value";

    public ReturnBlock() {
        this.blockGroups.add(new ReturnBlockGroup());
    }

    public ReturnBlockGroup getReturnBlockList() {
        return (ReturnBlockGroup) this.getGroup("Return").get();
    }

    @Override
    public String writeCode(int tabs) {
        /*
        Find attached method
         */
        return "return;";
    }

    @Override
    public Collection<String> getCodeImports() {
        Optional<ValueBlock<Object>> opReturn = this.getReturnBlockList().getSector().getAttachedBlock();
        if (opReturn.isPresent()) {
            return opReturn.get().getCodeImports();
        }
        return Collections.emptySet();
    }

    @Override
    public BlockType<? extends Block> getType() {
        return BlockType.BLOCK_TYPE_RETURN;
    }

    @Override
    public BlockNode<? extends Block> getNode() {
        throw new IllegalStateException("Not Implemented");
    }

    public static class ReturnBlockType implements BlockType<ReturnBlock> {

        @Override
        public ReturnBlock build() {
            return new ReturnBlock();
        }

        @Override
        public ReturnBlock build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if (opUUID.isEmpty()) {
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Double> opX = TITLE_X.deserialize(node);
            if (opX.isEmpty()) {
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Double> opY = TITLE_Y.deserialize(node);
            if (opY.isEmpty()) {
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();
            FXMainDisplay panel = ((FXMainDisplay) Blocks.getInstance().getWindow());
            List<Block> blocks = panel.getDisplayingBlocks();
            ReturnBlock methodBlock = new ReturnBlock();
            methodBlock.setPosition(opX.get(), opY.get());
            ReturnBlockGroup returnBlockGroup = methodBlock.getReturnBlockList();
            methodBlock.id = opUUID.get();
            for (UUID uuid : connected) {
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if (opBlock.isEmpty()) {
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if (!(opBlock.get() instanceof ValueBlock)) {
                    throw new IllegalStateException("Attached block was not a value block");
                }
                returnBlockGroup.getSector().setAttachedBlock(opBlock.get());
            }
            return methodBlock;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/start/method/return");
        }

        @Override
        public String getName() {
            return "Return";
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull ReturnBlock block) {
            BlockType.super.write(node, block);
            block.getReturnBlockList().getSector().getAttachedBlock().ifPresent(v -> TITLE_DEPENDS.serialize(node, Collections.singletonList(v.getUniqueId())));
        }
    }

    public class ReturnBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<ValueBlock<Object>> {

        public ReturnBlockGroup() {
            super(RETURN_BLOCK, "Return");
        }

        @Override
        public Block getBlock() {
            return ReturnBlock.this;
        }

        @Override
        public BlockGroupNode<? extends Block> getBlockNode() {
            throw new IllegalStateException("Not implemented");
        }
    }
}
