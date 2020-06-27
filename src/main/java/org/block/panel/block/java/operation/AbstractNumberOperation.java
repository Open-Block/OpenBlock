package org.block.panel.block.java.operation;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.panel.block.Block;
import org.block.panel.block.Shapes;
import org.block.panel.block.assists.AbstractAttachable;
import org.block.util.ClassCompare;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The basic Block implementation for all number operations.
 * All methods are implemented for block including code and paint.
 * All attachments must be of {@link Number}
 */
public class AbstractNumberOperation extends AbstractAttachable implements Block.ValueBlock<Number> {

    private final String operator;
    private final List<ValueBlock<?>> list = new ArrayList<>();
    private int marginX = 2;
    private int marginY = 2;

    /**
     * The constructor for abstract number operations
     * @param x The X position
     * @param y The Y position
     * @param text The text to display, this would typically be the English word for the operation
     * @param operator The Java maths operator
     */
    public AbstractNumberOperation(int x, int y, String text, String operator) {
        super(x, y, 0, 0, text, -1);
        this.operator = operator;
        setText(text);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        int width = Blocks.getInstance().getMetrics().stringWidth(text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.height = (this.list.size() + 2) * this.height;
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

        int amount = this.list.size() + 1;
        for(int A = 0; A < amount; A++){
            graphics2D.setColor(new Color(100, 0, 255));
            graphics2D.fillPolygon(Shapes.drawAttachingConnector(this.getX() + (this.getWidth() - Shapes.ATTACHABLE_WIDTH), this.getY() + ((A + 1) * Blocks.getInstance().getFont().getSize()), Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(this.operator, this.getX(), this.getY() + ((A + 2) * Blocks.getInstance().getFont().getSize()));
        }
    }

    @Override
    public String writeCode() {
        return ArrayUtils.toString(" " + this.operator + " ", Block::writeCode, this.list);
    }

    @Override
    public Class<Number> getExpectedValue() {
        return (Class<Number>) ClassCompare.toPrimitive((Class<Number>)ClassCompare.compareNumberClasses(b -> (Class<? extends Number>)b.getExpectedValue(), this.list).get().getExpectedValue());
    }

    @Override
    public boolean canAcceptAttachment(int index, Block block) {
        if(!(block instanceof Block.ValueBlock)){
            return false;
        }
        Block.ValueBlock<?> aBlock = (ValueBlock<?>) block;
        return Number.class.isAssignableFrom(aBlock.getExpectedValue());
    }
}
