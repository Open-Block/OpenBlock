package org.block.panel.block.java.operation;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.panel.MainDisplayPanel;
import org.block.panel.block.Block;
import org.block.panel.block.BlockType;
import org.block.serializtion.ConfigNode;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The implementation for sum Blocks
 */
public class SumOperation extends AbstractNumberOperation {

    /**
     * The BlockType for the Sum Block.
     */
    public static class SumOperationType implements BlockType<SumOperation> {

        @Override
        public SumOperation build(int x, int y) {
            return new SumOperation(x, y);
        }

        @Override
        public SumOperation build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(!opUUID.isPresent()){
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(!opX.isPresent()){
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(!opY.isPresent()){
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();
            MainDisplayPanel panel = (MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane();
            List<Block> blocks = panel.getBlockPanel().getBlocks();
            SumOperation sumBlock = new SumOperation(opX.get(), opY.get());
            sumBlock.id = opUUID.get();
            for(int A = 0; A < connected.size(); A++){
                UUID uuid = connected.get(A);
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if(!opBlock.isPresent()){
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                sumBlock.addParameter(A, (ValueBlock<?>) opBlock.get());
            }
            return sumBlock;
        }

        @Override
        public void write(ConfigNode node, SumOperation block) {
            BlockType.super.write(node, block);
            TITLE_DEPENDS.serialize(node, ArrayUtils.convert(p -> p.getUniqueId(), block.getCurrentParameters()));
        }

        @Override
        public File saveLocation() {
            return new File("blocks/maths/sum/");
        }

        @Override
        public String getName() {
            return "Sum";
        }
    }

    public SumOperation(int x, int y) {
        super(x, y, "Sum", "+");
    }
}
