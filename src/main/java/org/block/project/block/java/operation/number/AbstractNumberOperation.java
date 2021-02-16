package org.block.project.block.java.operation.number;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractBlockList;
import org.block.project.block.assists.BlockList;
import org.block.util.ClassCompare;

import java.util.*;
import java.util.List;

/**
 * The basic Block implementation for all number operations.
 * All methods are implemented for block including code and paint.
 * All attachments must be of {@link Number}
 */
public abstract class AbstractNumberOperation extends AbstractAttachable implements Block.ValueBlock<Number>, Block.TextBlock {

    public class NumberOperationBlockList extends AbstractBlockList<ValueBlock<? extends Number>>{

        public NumberOperationBlockList() {
            super(-1, 12);
        }

        @Override
        public AbstractNumberOperation getParent() {
            return AbstractNumberOperation.this;
        }

        @Override
        public void addAttachment(ValueBlock<? extends Number> block) {
            super.addAttachment(block);
            AbstractNumberOperation.this.updateHeight();
        }

        @Override
        public void removeAttachment(Block block) {
            super.removeAttachment(block);
            AbstractNumberOperation.this.updateHeight();
        }

        @Override
        public int getXPosition(int slot) {
            if(slot > this.getMaxAttachments()){
                throw new IndexOutOfBoundsException(slot + " is out of range from 0 - " + this.getMaxAttachments());
            }
            return AbstractNumberOperation.this.getWidth() - 12;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot > this.getMaxAttachments()){
                throw new IndexOutOfBoundsException(slot + " is out of range from 0 - " + this.getMaxAttachments());
            }
            int height =  12 + AbstractNumberOperation.this.marginY;
            for(int A = 0; A < slot; A++){
                ValueBlock<? extends Number> block = this.getAttachment(A).get();
                height += block.getHeight();
            }
            return height;
        }

        @Override
        public int getSlot(int x, int y) {
            for(int A = 0; A < this.getMaxAttachments(); A++){
                int height = this.getSlotHeight(A);
                int posY = this.getYPosition(A);
                if(((A == 0) ? 0 : posY) <= y && y < (posY + ((A == (this.getMaxAttachments() - 1) ? (this.getParent().getHeight() - posY) : height)))){
                    return A;
                }
            }
            throw new IllegalArgumentException("Position could not be found");
        }

        @Override
        public boolean canAcceptAttachment(int index, Block block) {
            if(!(block instanceof ValueBlock)){
                return false;
            }
            Class<?> clazz = ((ValueBlock<? extends Block>)block).getExpectedValue().get();
            if(clazz.isPrimitive()){
                try{
                    clazz = ClassCompare.toObject((Class<? extends Number>) clazz);
                }catch (ClassCastException e){

                }
            }
            return Number.class.isAssignableFrom(clazz);
        }
    }

    private final String operator;
    private String name;
    private int marginX = 8;
    private int marginY = 8;

    public static final String PARAMETER_SECTION = "Parameter";

    /**
     * The constructor for abstract number operations
     * @param x The X position
     * @param y The Y position
     * @param text The text to display, this would typically be the English word for the operation
     * @param operator The Java maths operator
     */
    public AbstractNumberOperation(int x, int y, String text, String operator) {
        super(x, y, 0, 0);
        this.attached.put(PARAMETER_SECTION, new NumberOperationBlockList());
        this.operator = operator;
        setText(text);
    }

    public BlockList<ValueBlock<? extends Number>> getAttachments(){
        return this.getAttachments(PARAMETER_SECTION);
    }

    public void updateHeight(){
        AbstractNumberOperation.this.height = 12 + AbstractNumberOperation.this.marginY + 12;
        for(ValueBlock<? extends Number> block1 : AbstractNumberOperation.this.getAttachments()){
            AbstractNumberOperation.this.height += block1.getHeight();
        }
        if(AbstractNumberOperation.this.height < 12){
            AbstractNumberOperation.this.height = 12 * 2;
        }
    }

    public List<ValueBlock<? extends Number>> getAttached(){
        List<ValueBlock<? extends Number>> list = new ArrayList<>();
        for(ValueBlock<? extends Number> attach : this.getAttachments()){
            list.add(attach);
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public void setText(String text) {
        this.name = text;
        this.width = (this.marginX * 2) + (12 * 2);
        AbstractNumberOperation.this.updateHeight();
    }

    @Override
    public String getText() {
        return this.name;
    }

    @Override
    public String writeCode(int tab) {
        return "(" + ArrayUtils.toString(" " + this.operator + " ", b -> b.writeCode(tab), this.getAttached()) + ")";
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.unmodifiableCollection(Collections.emptySet());
    }

    @Override
    public Optional<Class<Number>> getExpectedValue() {
        if (this.getAttached().isEmpty()){
            return Optional.of((Class<Number>)(Object)byte.class);
        }
        ValueBlock<? extends Number> block = ClassCompare.compareNumberClasses(b -> ClassCompare.toObject(b.getExpectedValue().get()), this.getAttached()).get();
        Class<? extends Number> primitive = ClassCompare.toPrimitive(block.getExpectedValue().get());
        return Optional.of((Class<Number>)primitive);
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        if(this.contains(x, y)){
            return Optional.of(PARAMETER_SECTION);
        }
        return Optional.empty();
    }
}
