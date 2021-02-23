package org.block.project.block.java.method.call;

import org.block.project.block.BlockGraphics;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.type.attachable.AttachableBlock;
import org.block.project.block.type.value.ValueBlock;

import java.util.Optional;

public interface MethodCallBlock extends AttachableBlock, ValueBlock<Object> {

    String METHOD_NAME_BLOCK_GROUP = "method_call:name";
    String METHOD_PARAMETERS_BLOCK_GROUP = "method_call:parameters";

    default MethodNameBlockGroup getNameBlockGroup() {
        Optional<BlockGroup> opBlockGroup = this.getGroup(METHOD_NAME_BLOCK_GROUP);
        if (opBlockGroup.isEmpty()) {
            throw new IllegalStateException("This object has not registered the block group of '" + METHOD_NAME_BLOCK_GROUP + "'");
        }

        return (MethodNameBlockGroup) opBlockGroup.get();
    }

    default MethodParameterBlockGroup getParametersBlockGroup() {
        Optional<BlockGroup> opBlockGroup = this.getGroup(METHOD_NAME_BLOCK_GROUP);
        if (opBlockGroup.isEmpty()) {
            throw new IllegalStateException("This object has not registered the block group of '" + METHOD_PARAMETERS_BLOCK_GROUP + "'");
        }

        return (MethodParameterBlockGroup) opBlockGroup.get();
    }

    @Override
    default BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not Implemented");
    }

    class MethodNameBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<StringBlock> {

        public MethodNameBlockGroup(int relativeY) {
            super(METHOD_NAME_BLOCK_GROUP, "Name", relativeY);
        }
    }

    abstract class MethodParameterBlockGroup extends AbstractBlockGroup.AbstractListBlockGroup {

        public MethodParameterBlockGroup(String id, String name, int relativeY) {
            super(id, name, relativeY);
        }

        public abstract void update();

    }
}
