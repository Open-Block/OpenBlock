package org.block.project.block.java.method.call;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.assists.AbstractBlockList;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.project.block.assists.BlockList;

public interface MethodCallBlock extends Block.AttachableBlock, Block.ValueBlock<Object> {

    String METHOD_BLOCK_LIST = "Method_Name";

    abstract class AbstractMethodNameBlockList extends AbstractSingleBlockList<ConnectedValueBlock<String>> {

        public AbstractMethodNameBlockList(int height) {
            super(height);
        }

        public AbstractMethodNameBlockList(int height, ConnectedValueBlock<String> value) {
            super(height, value);
        }
    }

    default AbstractMethodNameBlockList getMethodNameBlockList() {
        return (AbstractMethodNameBlockList) (Object) this.getAttachments(METHOD_BLOCK_LIST);
    }

    @Override
    default BlockGraphics getGraphicShape() {
        return null;
    }
}
