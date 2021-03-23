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
     */
    public BooleanBlock() {
        super(true, Object::toString);
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
        throw new IllegalStateException("Not Implemented");
    }
}
