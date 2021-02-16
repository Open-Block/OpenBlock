package org.block.project.block.java.variable;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.project.block.java.value.StringBlock;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.util.List;
import java.util.*;

public class VariableBlock extends AbstractAttachable implements Block.SpecificSectionBlock {

    public static class VariableBlockType implements BlockType<VariableBlock>{

        @Override
        public VariableBlock build(int x, int y) {
            return new VariableBlock(x, y);
        }

        @Override
        public VariableBlock build(ConfigNode node) {
            return null;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/variable/define");
        }

        @Override
        public String getName() {
            return "Variable Define";
        }
    }

    public class StringBlockList extends AbstractSingleBlockList<StringBlock> {

        public StringBlockList(int height) {
            super(height);
        }

        public StringBlockList(int height, StringBlock value) {
            super(height, value);
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            if(!(block instanceof StringBlock)){
                return false;
            }
            return true;
        }

        @Override
        public AttachableBlock getParent() {
            return VariableBlock.this;
        }

        @Override
        public int getXPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return VariableBlock.this.getWidth() - 12;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return VariableBlock.this.marginY;
        }

        @Override
        public int getSlot(int x, int y) {
            if(y >= 0 && getSlotHeight(0) > y) {
                return 0;
            }
            throw new IllegalArgumentException("Position could not be found");
        }
    }

    public class VariableBlockList extends AbstractSingleBlockList<ValueBlock<? extends Object>> {

        public VariableBlockList(int height) {
            super(height);
        }

        public VariableBlockList(int height, ValueBlock<?> value) {
            super(height, value);
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            if(block instanceof ValueBlock){
                return true;
            }
            return false;
        }

        @Override
        public AttachableBlock getParent() {
            return VariableBlock.this;
        }


        @Override
        public int getSlot(int x, int y) {
            int top = VariableBlock.this.marginY + VariableBlock.this.getNameAttachment().getSlotHeight(0);
            int bottom = VariableBlock.this.getHeight();
            if(top <= y && bottom >= y){
                return 0;
            }
            throw new IllegalArgumentException("Position could not be found");
        }

        @Override
        public int getXPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return VariableBlock.this.marginX;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return Math.max((150 * 2), VariableBlock.this.getNameAttachment().getSlotHeight(0)) + VariableBlock.this.marginY;

        }

        @Override
        public void addAttachment(ValueBlock<? extends Object> block) {
            super.addAttachment(block);
            VariableBlock.this.updateSize();
        }

        @Override
        public void removeAttachment(Block block) {
            super.removeAttachment(block);
            VariableBlock.this.updateSize();
        }
    }

    public static final String SECTION_VALUE = "Section";
    public static final String SECTION_NAME = "Name";

    private int marginX = 8;
    private int marginY = 8;

    public VariableBlock(int x, int y){
        this(x, y, null, null);
    }

    public VariableBlock(int x, int y, StringBlock name, ValueBlock<?> block) {
        super(x, y, 0, 0);
        this.attached.put(SECTION_NAME, new StringBlockList(12, name));
        this.attached.put(SECTION_VALUE, new VariableBlockList(12, block));
        updateSize();
    }

    public Optional<String> getName(){
        Optional<StringBlock> opBlock = this.getNameAttachment().getAttachment();
        if(opBlock.isPresent()){
            return Optional.of(opBlock.get().getValue());
        }
        return Optional.empty();
    }

    private void updateSize(){
        int max = 150;
        this.width = max + 12 + (this.marginX * 2);
        this.height = Math.max((12 * 2), this.getNameAttachment().getSlotHeight(0)) + (this.marginY * 2);
        VariableBlockList attachment = this.getVariableAttachment();
        this.height += attachment.getSlotHeight(0);
    }

    public StringBlockList getNameAttachment(){
        return (StringBlockList)(Object) this.getAttachments(SECTION_NAME);
    }

    public VariableBlockList getVariableAttachment(){
        return (VariableBlockList)(Object) this.getAttachments(SECTION_VALUE);
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        int relX = x - this.getX();
        int relY = y - this.getY();
        for(String section : this.getSections()){
            try{
                this.getAttachments(section).getSlot(relX, relY);
                return Optional.of(section);
            }catch (IllegalArgumentException e){
            }
        }
        return Optional.empty();
    }

    @Override
    public BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public String writeCode(int tab) {
        Optional<ValueBlock<?>> opValueBlock = this.getVariableAttachment().getAttachment();
        if(!opValueBlock.isPresent()){
            throw new IllegalStateException("Could not find the type specified. Does it have a value?");
        }
        Optional<StringBlock> opStringBlock = this.getNameAttachment().getAttachment();
        if(!opStringBlock.isPresent()){
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if(opStringBlock.get().getValue().length() == 0){
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        Class<?> clazz = null;
        String ret = null;
        do{
            Class<?> clazz2 = clazz == null ? opValueBlock.get().getExpectedValue().orElse(null) : clazz.getDeclaringClass();
            if(clazz2 == null){
                break;
            }
            clazz = clazz2;
            ret = clazz.getSimpleName() + ((ret == null) ? "" :  "." + ret);
        }while(true);
        return (ret.startsWith(".") ? ret.substring(1) : ret) + " " + opStringBlock.get().getValue() + " = " + opValueBlock.get().writeCode(tab + 1);
    }

    @Override
    public Collection<String> getCodeImports() {
        Optional<ValueBlock<?>> opValueBlock = this.getVariableAttachment().getAttachment();
        if(!opValueBlock.isPresent()){
            throw new IllegalStateException("Could not find the type specified. Does it have a value?");
        }
        Class<?> clazz = null;
        do{
            Class<?> clazz2 = (clazz == null) ? opValueBlock.get().getExpectedValue().orElse(null) : clazz.getDeclaringClass();
            if(clazz2 == null){
                break;
            }
            clazz = clazz2;
        }while(true);

        if(clazz == null){
            throw new IllegalStateException("No value block attached");
        }
        List<String> list = new ArrayList<>();
        if(clazz.getPackage() != null){
            list.add(clazz.getPackage().getName());
        }
        list.addAll(opValueBlock.get().getCodeImports());
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_VARIABLE;
    }
}
