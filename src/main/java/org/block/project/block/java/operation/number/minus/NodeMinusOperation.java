package org.block.project.block.java.operation.number.minus;

import org.block.project.block.java.operation.number.NodeNumberOperation;
import org.block.project.block.type.attachable.NodeBlockSelector;

public class NodeMinusOperation extends NodeNumberOperation<MinusOperation> implements NodeBlockSelector<MinusOperation> {

    public NodeMinusOperation(MinusOperation numberOperation) {
        super(numberOperation);
    }


}
