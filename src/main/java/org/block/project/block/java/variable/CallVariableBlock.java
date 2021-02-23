package org.block.project.block.java.variable;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.called.CodeStartBlock;
import org.block.project.block.type.called.LinkedBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;
import org.block.util.GeneralUntil;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class CallVariableBlock extends AbstractAttachableBlock implements ValueBlock<Object>, LinkedBlock<VariableBlock> {

    public static final String VARIABLE_SECTION = "call:variable";

    public CallVariableBlock(int x, int y) {
        super(x, y);
        this.blockGroups.add(new VariableLinkBlockGroup(0));
    }

    public VariableLinkBlockGroup getVariableBlockGroup() {
        return (VariableLinkBlockGroup) this.getGroup(VARIABLE_SECTION).get();
    }

    @Override
    public Optional<VariableBlock> getLinkedBlock() {
        Optional<CodeStartBlock> opParent = this.getParent();
        if (opParent.isEmpty()) {
            return Optional.empty();
        }
        Optional<StringBlock> opName = this.getVariableBlockGroup().getSector().getAttachedBlock();
        if (opName.isEmpty()) {
            return Optional.empty();
        }
        String name = opName.get().getValue();
        Set<Block> children = opParent.get().getChildren(b -> {
            if (!(b instanceof VariableBlock)) {
                return false;
            }
            VariableBlock block = (VariableBlock) b;
            Optional<String> opVariableName = block.getName();
            if (opVariableName.isEmpty()) {
                return false;
            }
            return opVariableName.get().equalsIgnoreCase(name);
        });
        if (children.isEmpty()) {
            return Optional.empty();
        }
        return children.stream().findAny().map(b -> (VariableBlock) b);
    }

    @Override
    public Optional<Class<Object>> getExpectedValue() {
        Optional<VariableBlock> opLink = this.getLinkedBlock();
        if (opLink.isEmpty()) {
            return Optional.empty();
        }
        //TODO
        Optional<ValueBlock<?>> opValueBlock = opLink.get().getBodyBlockGroup().getSector().getAttachedBlock();
        if (opValueBlock.isEmpty()) {
            return Optional.empty();
        }
        return (Optional<Class<Object>>) (Object) opValueBlock.get().getExpectedValue();
    }

    @Override
    public BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String writeCode(int tabs) {
        Optional<VariableBlock> opBlock = this.getLinkedBlock();
        if (opBlock.isEmpty()) {
            throw new IllegalStateException("Unknown link. Link this to a block");
        }
        Optional<String> opName = opBlock.get().getName();
        if (opName.isEmpty()) {
            throw new IllegalStateException("Unknown name on link. Make sure the link is correct");
        }
        return GeneralUntil.formatToLocalVariableName(opName.get());
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.emptySet();
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_VARIABLE_USE;
    }

    public static class VariableBlockType implements BlockType<CallVariableBlock> {

        @Override
        public CallVariableBlock build(int x, int y) {
            return new CallVariableBlock(x, y);
        }

        @Override
        public CallVariableBlock build(ConfigNode node) {
            return null;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/variable/use");
        }

        @Override
        public String getName() {
            return "Use Variable";
        }
    }

    public static class VariableLinkBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<StringBlock> {

        public VariableLinkBlockGroup(int relativeY) {
            super(VARIABLE_SECTION, "Variable", relativeY);
        }
    }
}
