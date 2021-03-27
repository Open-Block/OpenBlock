package org.block.project.block.java.value;

import org.block.project.block.BlockType;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * The custom block to display a pre-set {@link Boolean} value
 */
public class BooleanBlock extends AbstractValue<Boolean> {

    public BooleanBlock(){
        this(null);
    }

    public BooleanBlock(UUID uuid) {
        super(uuid, true, Object::toString);
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
