package org.block.project.block.java.operation.number;

import org.array.utils.ArrayUtils;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockSector;
import org.block.project.block.type.TextBlock;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.util.ClassCompare;

import java.util.*;

/**
 * The basic Block implementation for all number operations.
 * All methods are implemented for block including code and paint.
 * All attachments must be of {@link Number}
 */
public abstract class AbstractNumberOperation extends AbstractAttachableBlock implements ValueBlock<Number>, TextBlock {

    private final String operator;
    protected int marginX = 8;
    protected int marginY = 8;
    private String name;

    /**
     * The constructor for abstract number operations
     *
     * @param x        The X position
     * @param y        The Y position
     * @param text     The text to display, this would typically be the English word for the operation
     * @param operator The Java maths operator
     */
    public AbstractNumberOperation(int x, int y, String text, String operator) {
        super(x, y);
        this.operator = operator;
        setText(text);
    }

    public List<ValueBlock<? extends Number>> getAttached() {
        List<ValueBlock<? extends Number>> list = new ArrayList<>();
        for (BlockGroup group : this.getGroups()) {
            for (BlockSector<?> sector : group.getSectors()) {
                sector.getAttachedBlock().ifPresent(b -> {
                    list.add((ValueBlock<? extends Number>) b);
                });
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public String getText() {
        return this.name;
    }

    @Override
    public void setText(String text) {
        this.name = text;
    }

    @Override
    public String writeCode(int tab) {
        return "(" + ArrayUtils.toString(" " + this.operator + " ", b -> b.writeCode(tab), this.getAttached()) + ")";
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.emptySet();
    }

    @Override
    public Optional<Class<Number>> getExpectedValue() {
        if (this.getAttached().isEmpty()) {
            return Optional.of((Class<Number>) (Object) byte.class);
        }
        ValueBlock<? extends Number> block = ClassCompare.compareNumberClasses(b -> ClassCompare.toObject(b.getExpectedValue().get()), this.getAttached()).get();
        Class<? extends Number> primitive = ClassCompare.toPrimitive(block.getExpectedValue().get());
        return Optional.of((Class<Number>) primitive);
    }
}
