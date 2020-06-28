package org.block.panel.block.java.operation;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.panel.block.Block;
import org.block.panel.block.Shapes;
import org.block.panel.block.assists.AbstractAttachable;
import org.block.panel.block.assists.AbstractBlockList;
import org.block.panel.block.assists.BlockList;
import org.block.util.ClassCompare;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The basic Block implementation for all number operations.
 * All methods are implemented for block including code and paint.
 * All attachments must be of {@link Number}
 */
public class AbstractNumberOperation extends AbstractAttachable implements Block.ValueBlock<Number> {

    public class NumberOperationBlockList extends AbstractBlockList<ValueBlock<? extends Number>>{

        public NumberOperationBlockList() {
            super(-1, Shapes.ATTACHABLE_HEIGHT);
        }

        @Override
        public AbstractNumberOperation getParent() {
            return AbstractNumberOperation.this;
        }

        @Override
        public void addAttachment(ValueBlock<? extends Number> block) {
            super.addAttachment(block);
            AbstractNumberOperation.this.height = Blocks.getInstance().getFont().getSize() + AbstractNumberOperation.this.marginY;
            AbstractNumberOperation.this.height = (this.getMaxAttachments() + 1) * AbstractNumberOperation.this.height;
            if(AbstractNumberOperation.this.height < Shapes.ATTACHABLE_HEIGHT){
                AbstractNumberOperation.this.height = Shapes.ATTACHABLE_HEIGHT * 2;
            }
        }

        @Override
        public int getXPosition(int slot) {
            return AbstractNumberOperation.this.getWidth() - Shapes.ATTACHABLE_WIDTH;
        }

        @Override
        public int getYPosition(int slot) {
            return ((slot + 1) * Blocks.getInstance().getFont().getSize());
        }

        @Override
        public int getSlot(int x, int y) {
            System.out.println("Checking Position: Y: " + y);
            for(int A = 0; A < this.getMaxAttachments(); A++){
                System.out.println("Slot: " + A + " | Block: " + this.getAttachment(A));
                int height = this.getSlotHeight(A);
                int posY = this.getYPosition(A);
                System.out.println("\tComparing: " + ((A == 0) ? 0 : posY) + " <= " + y + " < " + (posY + height));
                if(((A == 0) ? 0 : posY) <= y && y < (posY + ((A == (this.getMaxAttachments() - 1) ? (this.getParent().getHeight() - posY) : height)))){
                    System.out.println("Returned");
                    return A;
                }
            }
            throw new IllegalArgumentException("Position could not be found");
        }

        @Override
        public boolean canAcceptAttachment(int index, ValueBlock<? extends Number> block) {
            return Number.class.isAssignableFrom(block.getExpectedValue());
        }
    }

    private final String operator;
    private int marginX = 2;
    private int marginY = 2;

    public static final String PARAMETER_SECTION = "Parameter";

    /**
     * The constructor for abstract number operations
     * @param x The X position
     * @param y The Y position
     * @param text The text to display, this would typically be the English word for the operation
     * @param operator The Java maths operator
     */
    public AbstractNumberOperation(int x, int y, String text, String operator) {
        super(x, y, 0, 0, text);
        this.attached.put(PARAMETER_SECTION, new NumberOperationBlockList());
        this.operator = operator;
        setText(text);
    }

    public BlockList<ValueBlock<? extends Number>> getAttachments(){
        return this.getAttachments(PARAMETER_SECTION);
    }

    public List<ValueBlock<? extends Number>> getAttached(){
        List<ValueBlock<? extends Number>> list = new ArrayList<>();
        for(ValueBlock<? extends Number> attach : this.getAttachments()){
            list.add(attach);
        }
        return list;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        int width = Blocks.getInstance().getMetrics().stringWidth(text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.height = (this.getAttachments().getMaxAttachments() + 1) * this.height;
        this.width = width + this.marginX + Shapes.ATTACHABLE_WIDTH;
        if(this.height < Shapes.ATTACHABLE_HEIGHT){
            this.height = Shapes.ATTACHABLE_HEIGHT * 2;
        }
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(100, 0, 255));
        graphics2D.fillRect(getX(), getY(), getWidth() - Shapes.ATTACHABLE_WIDTH, getHeight());
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(Blocks.getInstance().getFont());
        graphics2D.drawString(this.getText(), getX(), getY() + Blocks.getInstance().getFont().getSize());

        int amount = this.getAttachments().getMaxAttachments();
        for(int A = 0; A < amount; A++){
            graphics2D.setColor(new Color(100, 0, 255));
            graphics2D.fillPolygon(Shapes.drawAttachingConnector(this.getX() + (this.getWidth() - Shapes.ATTACHABLE_WIDTH), this.getY() + ((A + 1) * Blocks.getInstance().getFont().getSize()), Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(this.operator, this.getX(), this.getY() + ((A + 2) * Blocks.getInstance().getFont().getSize()));
        }
    }

    @Override
    public String writeCode() {
        return ArrayUtils.toString(" " + this.operator + " ", Block::writeCode, this.getAttached());
    }

    @Override
    public Class<Number> getExpectedValue() {
        return (Class<Number>) ClassCompare.toPrimitive((Class<Number>)ClassCompare.compareNumberClasses(b -> (Class<? extends Number>)b.getExpectedValue(), this.getAttached()).get().getExpectedValue());
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        if(this.contains(x, y)){
            return Optional.of(PARAMETER_SECTION);
        }
        return Optional.empty();
    }
}
