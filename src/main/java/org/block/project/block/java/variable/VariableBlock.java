package org.block.project.block.java.variable;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.Shapes;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.project.block.java.value.StringBlock;
import org.block.project.section.BlockSection;
import org.block.project.section.GUISection;
import org.block.project.section.GroupedSection;
import org.block.serialization.ConfigNode;

import java.awt.*;
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
            return VariableBlock.this.getWidth() - Shapes.ATTACHABLE_WIDTH;
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
            return Math.max((Blocks.getInstance().getFont().getSize() * 2), VariableBlock.this.getNameAttachment().getSlotHeight(0)) + VariableBlock.this.marginY;

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
        this.attached.put(SECTION_NAME, new StringBlockList(Shapes.ATTACHABLE_HEIGHT, name));
        this.attached.put(SECTION_VALUE, new VariableBlockList(Shapes.ATTACHABLE_HEIGHT, block));
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
        int max = Math.max(Blocks.getInstance().getMetrics().stringWidth("Name"), Blocks.getInstance().getMetrics().stringWidth("Value"));
        this.width = max + Shapes.ATTACHABLE_WIDTH + (this.marginX * 2);
        this.height = Math.max((Blocks.getInstance().getFont().getSize() * 2), this.getNameAttachment().getSlotHeight(0)) + (this.marginY * 2);
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
    public List<GUISection> getUniqueSections(GroupedSection section) {
        List<GUISection> list = new ArrayList<>();
        list.add(new BlockSection(section, BlockType.BLOCK_TYPE_VARIABLE_USE, "Use"));
        return list;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        this.updateSize();
        graphics2D.setColor(new Color(150, 200, 0));
        graphics2D.setFont(Blocks.getInstance().getFont());
        graphics2D.fillRoundRect(getX(), getY(), this.getWidth() - Shapes.ATTACHABLE_WIDTH, this.getHeight() - this.marginY - getVariableAttachment().getSlotHeight(0), 20, 20);
        graphics2D.fillRect(getX(), getY(), this.marginX, this.getHeight());
        graphics2D.fillRect(getX(), (getY() + this.getHeight()) - this.marginY, this.getWidth(), this.marginY);
        graphics2D.fillPolygon(Shapes.drawAttachingConnector(getX() + this.marginX, (this.getY() + this.getHeight()) - this.marginY - this.getVariableAttachment().getSlotHeight(0), Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT));
        graphics2D.fillPolygon(Shapes.drawAttachingConnector((getX() + this.width) - Shapes.ATTACHABLE_WIDTH, getY() + this.marginY, Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString("Name", getX() + this.marginX, getY() + this.marginY + Blocks.getInstance().getFont().getSize());
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
            Class<?> clazz2 = clazz == null ? opValueBlock.get().getExpectedValue() : clazz.getDeclaringClass();
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
            Class<?> clazz2 = (clazz == null) ? opValueBlock.get().getExpectedValue() : clazz.getDeclaringClass();
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
