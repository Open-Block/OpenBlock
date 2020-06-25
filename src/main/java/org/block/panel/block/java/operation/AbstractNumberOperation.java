package org.block.panel.block.java.operation;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.panel.block.AbstractBlock;
import org.block.panel.block.Block;
import org.block.panel.block.Shapes;
import org.block.util.ClassCompare;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractNumberOperation extends AbstractBlock implements Block.ParameterInsertBlock, Block.ValueBlock<Number> {

    private String operator;
    private List<ValueBlock<?>> list = new ArrayList<>();
    private int marginX = 2;
    private int marginY = 2;

    public AbstractNumberOperation(int x, int y, String text, String operator) {
        super(x, y, 0, 0, text);
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
    public int getMaxCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ValueBlock<?>> getCurrentParameters() {
        return Collections.unmodifiableList(this.list);
    }

    @Override
    public void addParameter(ValueBlock<?> block) {
        this.list.add(block);
        block.setX(this.getX() + (this.getWidth() - Shapes.ATTACHABLE_WIDTH));
        block.setY(this.getY() + (this.list.size() * Blocks.getInstance().getFont().getSize()));

        int width = Blocks.getInstance().getMetrics().stringWidth(this.text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.height = (this.list.size() + 2) * this.height;
        this.width = width + this.marginX + Shapes.ATTACHABLE_WIDTH;
        if(this.height < Shapes.ATTACHABLE_HEIGHT){
            this.height = Shapes.ATTACHABLE_HEIGHT * 2;
        }
    }

    @Override
    public void addParameter(int index, ValueBlock<?> block) {
        this.list.add(index, block);
    }

    @Override
    public void removeParameter(ValueBlock<?> block) {
        this.list.remove(block);
        
        int width = Blocks.getInstance().getMetrics().stringWidth(this.text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.height = (this.list.size() + 2) * this.height;
        this.width = width + this.marginX + Shapes.ATTACHABLE_WIDTH;
        if(this.height < Shapes.ATTACHABLE_HEIGHT){
            this.height = Shapes.ATTACHABLE_HEIGHT * 2;
        }
    }

    @Override
    public void removeParameter(int space) {
        this.list.remove(space);
    }

    @Override
    public boolean canAccept(int slot, ValueBlock<?> block) {
        Class<?> clazz = block.getExpectedValue();
        if(!clazz.isAssignableFrom(Number.class)){
            return false;
        }
        return true;
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
            graphics2D.fillPolygon(Shapes.drawAttachingConnector(this.getX() + (this.getWidth() - Shapes.ATTACHABLE_WIDTH), this.getY() + ((A + 1) * Blocks.getInstance().getFont().getSize()), Shapes.ATTACHABLE_HEIGHT, Shapes.ATTACHABLE_WIDTH));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(this.operator, this.getX(), this.getY() + ((A + 2) * Blocks.getInstance().getFont().getSize()));
        }

    }

    @Override
    public String writeCode() {
        return ArrayUtils.toString(" " + this.operator + " ", b -> b.writeCode() , this.list);
    }

    @Override
    public Class<Number> getExpectedValue() {
        return (Class<Number>) ClassCompare.compareNumberClasses(b -> (Class<? extends Number>)b.getExpectedValue(), this.list).get().getExpectedValue();
    }
}
