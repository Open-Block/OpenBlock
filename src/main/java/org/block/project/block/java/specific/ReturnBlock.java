package org.block.project.block.java.specific;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.panel.main.FXMainDisplay;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.List;

public class ReturnBlock extends AbstractAttachableBlock {

    public static class ReturnBlockType implements BlockType<ReturnBlock>{

        @Override
        public ReturnBlock build(int x, int y) {
            return new ReturnBlock(x, y);
        }

        @Override
        public ReturnBlock build(ConfigNode node) {
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
            FXMainDisplay panel = ((FXMainDisplay)Blocks.getInstance().getSceneSource());
            List<Block> blocks = panel.getDisplayingBlocks();
            ReturnBlock methodBlock = new ReturnBlock(opX.get(), opY.get());
            ReturnAttacher blockList = methodBlock.getReturnBlockList();
            methodBlock.id = opUUID.get();
            for (UUID uuid : connected) {
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if (opBlock.isEmpty()) {
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if (!(opBlock.get() instanceof ValueBlock)) {
                    throw new IllegalStateException("Attached block was not a value block");
                }
                blockList.setAttachment((ValueBlock<?>) opBlock.get());
            }
            return methodBlock;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull ReturnBlock block) {
            BlockType.super.write(node, block);
            block.getReturnBlockList().getAttachment().ifPresent(v -> TITLE_DEPENDS.serialize(node, Collections.singletonList(v.getUniqueId())));
        }

        @Override
        public File saveLocation() {
            return new File("blocks/start/method/return");
        }

        @Override
        public String getName() {
            return "Return";
        }
    }

    public class ReturnBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<>

    private int marginX = 2;
    private int marginY = 4;

    public ReturnBlock(int x, int y) {
        super(x, y, 0, 0);
        ReturnAttacher r = new ReturnAttacher(0);
        this.attached.put("Return", r);
    }

    public ReturnAttacher getReturnBlockList(){
        return (ReturnAttacher) (Object) this.getAttachments("Return");
    }

    @Override
    public BlockGraphics getGraphicShape() {
        return null;
    }

    @Override
    public String writeCode(int tabs) {
        if(this.getReturnBlockList().getAttachment().isPresent()){
            ValueBlock<?> ret = this.getReturnBlockList().getAttachment().get();
            return "return " + ret.writeCode(0);
        }
        return "return;";
    }

    @Override
    public Collection<String> getCodeImports() {
        if(this.getReturnBlockList().getAttachment().isPresent()){
            ValueBlock<?> ret = this.getReturnBlockList().getAttachment().get();
            return ret.getCodeImports();
        }
        return Collections.emptySet();
    }

    @Override
    public BlockType<? extends Block> getType() {
        return BlockType.BLOCK_TYPE_RETURN;
    }
}
