package org.block.project.block.java.value;

import org.block.project.block.AbstractBlock;
import org.block.project.block.Block;
import org.block.project.block.BlockNode;
import org.block.project.block.type.TextBlock;
import org.block.project.block.type.value.ConnectedValueBlock;

import java.util.UUID;
import java.util.function.Function;

/**
 * AbstractValue is a block that has a dedicated value provided with it. No other blocks can directly
 * affect this block in terms of it being attached to another block.
 *
 * @param <V> The expected output type of the value
 */
public abstract class AbstractValue<V> extends AbstractBlock implements ConnectedValueBlock<V>, TextBlock {

    protected final Function<V, String> toString;
    protected V value;
    protected String text;

    /**
     * Th init of the Abstract value.
     * This uses {@link Object#toString()} to calculate the display name
     *
     * @param value The attached value
     */
    public AbstractValue(UUID uuid, V value) {
        this(uuid, value, Object::toString);
    }

    /**
     * The init of the Abstract value
     *
     * @param value    The attached value
     * @param toString The generic convection of the value to a String
     */
    public AbstractValue(UUID uuid, V value, Function<V, String> toString) {
        super(uuid);
        this.toString = toString;
        this.setValue(value);
    }

    /**
     * Sets the value of the block. Please note that for the change to take affect visually the panel must be repainted
     *
     * @param value The new value
     */
    public void setValue(V value) {
        this.value = value;
        this.text = this.toString.apply(value);
    }

    @Override
    public BlockNode<? extends Block> getNode() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public V getValue() {
        return this.value;
    }

    /**
     * Gets the text of the block. This should display the value that is attached.
     *
     * @return The text provided on the block
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text of the block. This should display the value that is attached.
     * Note that setting the value using {@link AbstractValue#setValue(Object)} will override the set text,
     * therefore this function should be ran after the setting of the value if you wish to use abnormal text
     *
     * @param text The text to display
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
