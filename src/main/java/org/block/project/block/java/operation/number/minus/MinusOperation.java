package org.block.project.block.java.operation.number.minus;

import org.block.Blocks;
import org.block.panel.main.FXMainDisplay;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.group.AbstractBlockSector;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockGroupNode;
import org.block.project.block.java.operation.number.AbstractNumberOperation;
import org.block.project.block.BlockNode;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MinusOperation extends AbstractNumberOperation {

    private final NodeMinusOperation nodeBlock;

    public MinusOperation() {
        super("Minus", "-");
        this.nodeBlock = new NodeMinusOperation(this);
    }

    @Override
    public BlockNode<? extends Block> getNode() {
        return this.nodeBlock;
    }

    @Override
    public BlockType<? extends Block> getType() {
        return BlockType.BLOCK_TYPE_MINUS;
    }

    public static class MinusBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<ValueBlock<? extends Number>> {

        private final MinusOperation parent;

        public MinusBlockGroup(MinusOperation parent, String id, String name, ValueBlock<? extends Number> block) {
            super(id, name);
            this.parent = parent;
            this.sector = new AbstractBlockSector<>(this,
                    (Class<ValueBlock<? extends Number>>) (Object) ValueBlock.class,
                    block,
                    b -> b.getExpectedValue().isPresent());
        }

        @Override
        public MinusOperation getBlock() {
            return this.parent;
        }

        @Override
        public BlockGroupNode<? extends Block> getBlockNode() {
            throw new IllegalStateException("Not implemented");
        }
    }

    public static class MinusOperationType implements BlockType<MinusOperation> {

        @Override
        public MinusOperation build() {
            return new MinusOperation();
        }

        @Override
        public MinusOperation build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if (opUUID.isEmpty()) {
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Double> opX = TITLE_X.deserialize(node);
            if (opX.isEmpty()) {
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Double> opY = TITLE_Y.deserialize(node);
            if (opY.isEmpty()) {
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();
            FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getWindow();
            List<Block> blocks = panel.getDisplayingBlocks();
            MinusOperation minusBlock = new MinusOperation();
            minusBlock.setPosition(opX.get(), opY.get());
            List<BlockGroup> groups = minusBlock.getGroups();
            minusBlock.id = opUUID.get();
            for (int A = 0; A < connected.size(); A++) {
                UUID uuid = connected.get(A);
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if (opBlock.isEmpty()) {
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if (!(opBlock.get() instanceof ValueBlock)) {
                    throw new IllegalStateException("Attached block was not a value block");
                }
                groups.add(A, new MinusBlockGroup(minusBlock, "minus:index" + A, "index " + A, opBlock.map(b -> (ValueBlock<? extends Number>) b).get()));
            }
            return minusBlock;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/maths/minus/");
        }

        @Override
        public String getName() {
            return "Minus";
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull MinusOperation block) {
            BlockType.super.write(node, block);
            List<UUID> list = new ArrayList<>();
            List<BlockGroup> blockLists = block.getGroups();
            for (BlockGroup blockList : blockLists) {
                blockList.getSectors().get(0).getAttachedBlock().ifPresent(b -> list.add(b.getUniqueId()));
            }
            TITLE_DEPENDS.serialize(node, list);
        }
    }
}
