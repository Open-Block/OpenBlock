package org.block.project.block.java.value;

import org.block.Blocks;
import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
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
        this(x, y, value, Object::toString);
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
        int width = 150;
        this.height = 12 + this.marginY;
        this.width = width + this.marginX + 12;
        if(this.height < 12){
            this.height = 12;
        }
    }

    @Override
    public BlockGraphics getGraphicShape() {
        return null;
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
     * @return The text provided on the block
     */
    @Override
    public String getText() {
        return this.text;
    }
}
