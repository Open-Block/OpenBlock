package org.block.project.block.java.variable;

import org.block.project.block.*;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.serialization.ConfigNode;
import org.block.util.GeneralUntil;

import java.io.File;
import java.util.*;
import java.util.List;

public class UseVariableBlock extends AbstractAttachable implements Block.ValueBlock<Object>, Block.SpecificSectionBlock, Block.LinkedBlock {

    public static class VariableBlockType implements BlockType<UseVariableBlock>{

        @Override
        public UseVariableBlock build(int x, int y) {
            return new UseVariableBlock(x, y);
        }

        @Override
        public UseVariableBlock build(ConfigNode node) {
            return null;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/variable/use");
        }

        @Override
        public String getName() {
            return "Use Variable";
        }
    }

    public class VariableLinkBlockList extends AbstractSingleBlockList<VariableBlock>{

        public VariableLinkBlockList() {
            super(12);
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            if(block instanceof VariableBlock){
                return true;
            }
            return false;
        }

        @Override
        public AttachableBlock getParent() {
            return UseVariableBlock.this;
        }

        @Override
        public int getXPosition(int slot) {
            return 0;
        }

        @Override
        public int getYPosition(int slot) {
            return 0;
        }

        @Override
        public int getSlot(int x, int y) {
            return 0;
        }
    }

    public static final String VARIABLE_SECTION = "Variable";

    public UseVariableBlock(int x, int y) {
        super(x, y, 1, 1);
        this.attached.put(VARIABLE_SECTION, new VariableLinkBlockList());
        updateSize();
    }

    public VariableLinkBlockList getVariableBlockList(){
        return (VariableLinkBlockList) (Object)this.getAttachments(VARIABLE_SECTION);
    }

    public void updateSize(){
        this.height = this.getVariableBlockList().getSlotHeight(0);
        this.width = 12;
    }

    @Override
    public Optional<Block> getLinkedBlock() {
        return (Optional<Block>) (Object)this.getVariableBlockList().getAttachment();
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        return Optional.empty();
    }

    @Override
    public Optional<Class<Object>> getExpectedValue() {
        if(getLinkedBlock().isPresent()){
            Optional<VariableBlock> opAttached = getVariableBlockList().getAttachment();
            if(opAttached.isPresent()){
                Optional<ValueBlock<?>> opValue = opAttached.get().getVariableAttachment().getAttachment();
                if(opValue.isPresent()){
                    return Optional.of((Class<Object>)opValue.get().getExpectedValue().get());
                }
            }
        }
        return Optional.of(Object.class);
    }

    @Override
    public BlockGraphics getGraphicShape() {
        return null;
    }

    @Override
    public String writeCode(int tabs) {
        Optional<VariableBlock> opBlock = getVariableBlockList().getAttachment();
        if(!opBlock.isPresent()){
            throw new IllegalStateException("Unknown link. Link this to a block");
        }
        Optional<String> opName = opBlock.get().getName();
        if(!opName.isPresent()){
            throw new IllegalStateException("Unknown name on link. Make sure the link is correct");
        }
        return GeneralUntil.formatToLocalVariableName(opName.get());
    }

    @Override
    public Collection<String> getCodeImports() {
        List<String> list = new ArrayList<>();
        Optional<VariableBlock> opBlockList = this.getVariableBlockList().getAttachment();
        if(!opBlockList.isPresent()) {
            throw new IllegalStateException("Unknown linked block");
        }
        Optional<ValueBlock<?>> opValueBlock = opBlockList.get().getVariableAttachment().getAttachment();
        if(!opValueBlock.isPresent()) {
            throw new IllegalStateException("Unknown value on linked block");
        }

        list.add(GeneralUntil.getImport(opValueBlock.get())[0]);
        list.addAll(opValueBlock.get().getCodeImports());
        return list;
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_VARIABLE_USE;
    }
}
