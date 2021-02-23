package org.block.project.block.java.value;

import org.block.project.block.BlockType;

import java.util.Collection;
import java.util.Collections;

/**
 * The custom block to display a pre-set {@link Boolean} value
 */
public class BooleanBlock extends AbstractValue<Boolean> {

    /**
     * Used to init the block
     *
     * @param x The X position
     * @param y The Y position
     */
    public BooleanBlock(int x, int y) {
        super(x, y, true, m -> m.toString());
    }

    @Override
    public String writeCode(int tab) {
        return this.getValue().toString();
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.unmodifiableCollection(Collections.emptySet());
    }

    @Override
    public BlockType<?> getType() {
        return null;
    }
}
