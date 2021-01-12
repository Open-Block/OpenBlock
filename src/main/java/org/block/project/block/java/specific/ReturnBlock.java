package org.block.project.block.java.specific;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.Shapes;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.project.block.assists.BlockList;
import org.block.project.legacypanel.inproject.MainDisplayPanel;
import org.block.serialization.ConfigNode;
import org.block.util.OrderedUniqueList;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class ReturnBlock extends AbstractAttachable implements Block.AttachableBlock {

    public static class ReturnBlockType implements BlockType<ReturnBlock>{

        @Override
        public ReturnBlock build(int x, int y) {
            return new ReturnBlock(x, y);
        }

        @Override
        public ReturnBlock build(ConfigNode node) {
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
            MainDisplayPanel panel = Blocks.getInstance().getLoadedProject().get().getPanel();
            OrderedUniqueList<Block> blocks = panel.getBlocksPanel().getSelectedComponent().getBlocks();
            ReturnBlock methodBlock = new ReturnBlock(opX.get(), opY.get());
            ReturnAttacher blockList = methodBlock.getReturnBlockList();
            methodBlock.id = opUUID.get();
            for (UUID uuid : connected) {
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if (!opBlock.isPresent()) {
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if (!(opBlock.get() instanceof ValueBlock)) {
                    throw new IllegalStateException("Attached block was not a value block");
                }
                blockList.setAttachment((ValueBlock<?>) opBlock.get());
            }
            return methodBlock;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull ReturnBlock block) {
            BlockType.super.write(node, block);
            block.getReturnBlockList().getAttachment().ifPresent(v -> TITLE_DEPENDS.serialize(node, Collections.singletonList(v.getUniqueId())));
        }

        @Override
        public File saveLocation() {
            return new File("blocks/start/method/return");
        }

        @Override
        public String getName() {
            return "Return";
        }
    }

    public class ReturnAttacher extends AbstractSingleBlockList<ValueBlock<?>> implements BlockList<ValueBlock<?>> {

        public ReturnAttacher(int height) {
            super(height);
        }

        public ReturnAttacher(int height, ValueBlock<?> value) {
            super(height, value);
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            return block instanceof ValueBlock;
        }

        @Override
        public AttachableBlock getParent() {
            return ReturnBlock.this;
        }

        @Override
        public int getXPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException("ReturnBlock.ReturnAttacher can only accept slot 0");
            }
            return 0;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException("ReturnBlock.ReturnAttacher can only accept slot 0");
            }
            return 25;
        }

        @Override
        public int getSlot(int x, int y) {
            return 0;
        }
    }

    private int marginX = 2;
    private int marginY = 4;

    public ReturnBlock(int x, int y) {
        super(x, y, 0, 0);
        ReturnAttacher r = new ReturnAttacher(0);
        this.attached.put("Return", r);
        updateSize();
    }

    public void updateSize(){
        int max = Blocks.getInstance().getMetrics().stringWidth("Return");
        this.width = max + Shapes.ATTACHABLE_WIDTH + (this.marginX * 2);
        int textHeight = Blocks.getInstance().getFont().getSize();
        int slotHeight = this.getReturnBlockList().getSlotHeight(0);
        this.height = Math.max(textHeight, slotHeight) + (this.marginY * 2);
    }

    public ReturnAttacher getReturnBlockList(){
        return (ReturnAttacher) (Object) this.getAttachments("Return");
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        return Optional.empty();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        updateSize();
        graphics2D.setColor(new Color(50, 55, 50));
        graphics2D.drawRect(this.x, this.y, this.width, this.height);
        System.out.println("Y: " + this.getY() + " Width: " + this.getWidth() + " Font: " + (Blocks.getInstance().getFont().getSize() / 2) + " Total: " + ((this.getY() + this.getWidth()) - (Blocks.getInstance().getFont().getSize()/2)));
        graphics2D.drawString("Return", this.x + this.marginX, (this.getY() - this.getWidth()) + (Blocks.getInstance().getFont().getSize()/2));
    }

    @Override
    public String writeCode(int tabs) {
        if(this.getReturnBlockList().getAttachment().isPresent()){
            ValueBlock<?> ret = this.getReturnBlockList().getAttachment().get();
            return "return " + ret.writeCode(0);
        }
        return "return;";
    }

    @Override
    public Collection<String> getCodeImports() {
        if(this.getReturnBlockList().getAttachment().isPresent()){
            ValueBlock<?> ret = this.getReturnBlockList().getAttachment().get();
            return ret.getCodeImports();
        }
        return Collections.emptySet();
    }

    @Override
    public BlockType<? extends Block> getType() {
        return BlockType.BLOCK_TYPE_RETURN;
    }
}
