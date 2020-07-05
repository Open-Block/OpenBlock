package org.block.project.block.java.value;

import org.block.Blocks;
import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;
import org.block.project.block.Shapes;

import java.awt.*;
import java.util.function.Function;

/**
 * AbstractValue is a block that has a dedicated value provided with it. No other blocks can directly
 * affect this block in terms of it being attached to another block.
 * @param <V> The expected output type of the value
 */
public abstract class AbstractValue<V> extends AbstractBlock implements Block.ValueBlock.ConnectedValueBlock<V>, Block.TextBlock {

    protected final Function<V, String> toString;
    protected V value;
    protected String text;
    protected int marginX = 2;
    protected int marginY = 2;
  
    /**
     * Th init of the Abstract value.
     * This uses {@link Object#toString()} to calculate the display name
     * @param x The X position
     * @param y The Y position
     * @param value The attached value
     */
    public AbstractValue(int x, int y, V value){
        this(x, y, value, v -> v.toString());
    }

    /**
     * The init of the Abstract value
     * @param x The X position
     * @param y The Y position
     * @param value The attached value
     * @param toString The generic convection of the value to a String
     */
    public AbstractValue(int x, int y, V value, Function<V, String> toString) {
        super(x, y, 10, 10);
        this.toString = toString;
        this.setValue(value);
    }

    /**
     * Sets the value of the block. Please note that for the change to take affect visually the panel must be repainted
     * @param value The new value
     */
    public void setValue(V value){
        this.value = value;
        this.text = this.toString.apply(value);
        int width = Blocks
                .getInstance()
                .getMetrics()
                .stringWidth(this.text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.width = width + this.marginX + Shapes.ATTACHABLE_WIDTH;
        if(this.height < Shapes.ATTACHABLE_HEIGHT){
            this.height = Shapes.ATTACHABLE_HEIGHT;
        }
    }

    @Override
    public V getValue(){
        return this.value;
    }

    /**
     * Sets the text of the block. This should display the value that is attached.
     * Note that setting the value using {@link AbstractValue#setValue(Object)} will override the set text,
     * therefore this function should be ran after the setting of the value if you wish to use abnormal text
     * @param text The text to display
     */
    @Override
    public void setText(String text){
        this.text = text;
    }

    /**
     * Gets the text of the block. This should display the value that is attached.
     * @return
     */
    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        Polygon attachable = Shapes.drawAttachableConnector(getX(), getY(), Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT);
        Color outline = null;
        if(this.isHighlighted() && this.isSelected()){
            outline = new Color(0, 255, 0);
        }else if(this.isSelected()){
            outline = new Color(255, 0, 0);
        }else if(this.isHighlighted()){
            outline = new Color(0, 0, 255);
        }
        if(outline != null){
            graphics2D.setColor(outline);
            graphics2D.fillRect(getX() + Shapes.ATTACHABLE_WIDTH + this.marginX, getY() + this.marginY, (getWidth() + this.marginX), (getHeight() + this.marginY));
        }
        graphics2D.setColor(new Color(255, 255, 0));
        graphics2D.fillPolygon(attachable);
        graphics2D.fillRect(getX() + Shapes.ATTACHABLE_WIDTH, getY(), getWidth(), getHeight());
        graphics2D.setColor(new Color(0,0,0));
        graphics2D.setFont(Blocks.getInstance().getFont());
        graphics2D.drawString(getText(), getX() + Shapes.ATTACHABLE_WIDTH + this.marginX, (getY() + Blocks.getInstance().getFont().getSize()) - this.marginY);
    }
}
