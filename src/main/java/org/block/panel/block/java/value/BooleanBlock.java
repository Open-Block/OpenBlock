package org.block.panel.block.java.value;

import org.block.panel.block.BlockType;

/**
 * The custom block to display a pre-set {@link Boolean} value
 */
public class BooleanBlock extends AbstractValue<Boolean> {

    /**
     * Used to init the block
     * @param x The X position
     * @param y The Y position
     */
    public BooleanBlock(int x, int y) {
        super(x, y, true, m -> m.toString());
    }

    @Override
    public String writeCode() {
        return this.getValue().toString();
    }

    @Override
    public BlockType<?> getType() {
        return null;
    }
}
