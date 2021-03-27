package org.block.project.block.java.operation.number.minus;

import javafx.scene.Node;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.group.*;
import org.block.project.block.java.operation.number.AbstractNumberOperation;
import org.block.project.block.BlockNode;
import org.block.project.block.java.operation.number.minus.group.MinusOperationGroupNode;
import org.block.project.block.type.value.ValueBlock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MinusOperation extends AbstractNumberOperation {

    private final NodeMinusOperation nodeBlock;

    public MinusOperation(){
        this(null);
    }

    public MinusOperation(UUID uuid) {
        super(uuid, "Minus", "-");
        this.nodeBlock = new NodeMinusOperation(this);
    }

    public Optional<BlockGroup> getGroup(int index){
        return this.getGroup("minus:index" + index);
    }

    public void addValue(ValueBlock<? extends Number> valueBlock){
        clearBlankValues();
        this.blockGroups.add(new MinusOperation.MinusBlockGroup(this, "minus:index" + this.blockGroups.size(), "index " + this.blockGroups.size(), valueBlock));
        if(this.blockGroups.size() < 2){
            this.blockGroups.add(new MinusOperation.MinusBlockGroup(this, "minus:index" + this.blockGroups.size(), "index " + this.blockGroups.size(), null));
        }
    }

    private void fillBlankValues(){
        clearBlankValues();
        while(this.blockGroups.size() < 2){
            this.blockGroups.add(new MinusOperation.MinusBlockGroup(this, "minus:index" + this.blockGroups.size(), "index " + this.blockGroups.size(), null));
        }
    }

    private void clearBlankValues(){
        var sectors = this.blockGroups.parallelStream()
                .map(g -> (MinusOperation.MinusBlockGroup)g)
                .filter(g -> g.getSector().getAttachedBlock().isPresent())
                .collect(Collectors.toList());
        this.blockGroups.clear();
        this.blockGroups.addAll(sectors);
    }

    @Override
    public List<BlockGroup> getGroups() {
        this.fillBlankValues();
        return super.getGroups();
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
            Node node;
            if (this.getSector().getAttachedBlock().isPresent()){
                node = (Node)this.getSector().getAttachedBlock().get().getNode();
            }else{
                node = new EmptyBlockSectorNode(this.getSector());
            }
            return new MinusOperationGroupNode(this, node);
        }
    }
}
