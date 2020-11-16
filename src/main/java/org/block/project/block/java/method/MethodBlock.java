package org.block.project.block.java.method;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.Shapes;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.AbstractBlockList;
import org.block.project.block.assists.AbstractSingleBlockList;
import org.block.project.block.java.value.StringBlock;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.project.section.BlockSection;
import org.block.project.section.GUISection;
import org.block.project.section.GroupedSection;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.block.util.GeneralUntil;
import org.block.util.OrderedUniqueList;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;

public class MethodBlock extends AbstractAttachable implements Block.SpecificSectionBlock, Block.CalledBlock.CodeStartBlock {

    public static class MethodBlockType implements BlockType<MethodBlock> {

        public static final FixedTitle<UUID> TITLE = new FixedTitle<>("title", Parser.UNIQUE_ID);

        @Override
        public MethodBlock build(int x, int y) {
            return new MethodBlock(x, y);
        }

        @Override
        public MethodBlock build(ConfigNode node) {
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
            MethodBlock methodBlock = new MethodBlock(opX.get(), opY.get());
            VariableBlockList blockList = methodBlock.getVariableAttachment();
            methodBlock.id = opUUID.get();
            for(int A = 0; A < connected.size(); A++){
                UUID uuid = connected.get(A);
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if(!opBlock.isPresent()){
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if(!(opBlock.get() instanceof ValueBlock)){
                    throw new IllegalStateException("Attached block was not a value block");
                }
                blockList.setAttachment(A, opBlock.get());
            }

            TITLE.deserialize(node)
                    .flatMap(uuid -> blocks
                            .stream()
                            .filter(b -> b.getUniqueId().equals(uuid))
                            .findAny())
                    .ifPresent(block -> methodBlock
                            .getNameAttachment()
                            .setAttachment((StringBlock) block));
            return methodBlock;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull MethodBlock block) {
            BlockType.super.write(node, block);
            List<UUID> list = new ArrayList<>();
            VariableBlockList blockList = block.getVariableAttachment();
            for(int A = 0; A < blockList.getMaxAttachments(); A++){
                blockList.getAttachment(A).ifPresent(b -> list.add(b.getUniqueId()));
            }
            TITLE_DEPENDS.serialize(node, list);
            block.getNameAttachment().getAttachment().ifPresent(b -> TITLE.serialize(node, b.getUniqueId()));
        }

        @Override
        public File saveLocation() {
            return new File("blocks/start/method");
        }

        @Override
        public String getName() {
            return "Method defined";
        }
    }

    public class StringBlockList extends AbstractSingleBlockList<StringBlock> {

        public StringBlockList(int height) {
            super(height);
        }

        public StringBlockList(int height, StringBlock value) {
            super(height, value);
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            return block instanceof StringBlock;
        }

        @Override
        public AttachableBlock getParent() {
            return MethodBlock.this;
        }

        @Override
        public int getXPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return MethodBlock.this.getWidth() - Shapes.ATTACHABLE_WIDTH;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return MethodBlock.this.marginY;
        }

        @Override
        public int getSlot(int x, int y) {
            int end = Math.max((Blocks.getInstance().getFont().getSize() * 2), this.getSlotHeight(0)) + MethodBlock.this.marginY;
            if(y >= 0 && end > y) {
                return 0;
            }
            throw new IllegalArgumentException("Position could not be found");
        }
    }

    public class VariableBlockList extends AbstractBlockList<Block> {

        public VariableBlockList() {
            super(-1, Shapes.ATTACHABLE_HEIGHT);
        }

        @Override
        public boolean canAcceptAttachment(int slot, Block block) {
            return true;
        }

        @Override
        public AttachableBlock getParent() {
            return MethodBlock.this;
        }


        @Override
        public int getSlot(int x, int y) {
            for(int A = 0; A < this.getMaxAttachments(); A++){
                int height = this.getSlotHeight(A);
                int posY = this.getYPosition(A);
                if(posY <= y && y < (posY + ((A == (this.getMaxAttachments() - 1) ? (this.getParent().getHeight() - posY) : height)))){
                    return A;
                }
            }
            throw new IllegalArgumentException("Position could not be found");
        }

        @Override
        public int getXPosition(int slot) {
            if(slot > this.getMaxAttachments()){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            return MethodBlock.this.marginX;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot > this.getMaxAttachments()){
                throw new IndexOutOfBoundsException(slot + " is out of range");
            }
            int start = Math.max((Blocks.getInstance().getFont().getSize() * 2), MethodBlock.this.getNameAttachment().getSlotHeight(0)) + MethodBlock.this.marginY;
            for(int A = 0; A < slot; A++){
                start += this.getSlotHeight(A);
            }
            return start;
        }

        @Override
        public void addAttachment(Block block) {
            super.addAttachment(block);
            MethodBlock.this.updateSize();
        }

        @Override
        public void removeAttachment(Block block) {
            super.removeAttachment(block);
            MethodBlock.this.updateSize();
        }
    }

    public static final String SECTION_VALUE = "Section";
    public static final String SECTION_PARAMETER = "Parameter";
    public static final String SECTION_NAME = "Name";

    private int marginX = 8;
    private int marginY = 8;

    public MethodBlock(int x, int y){
        this(x, y, null);
    }

    public MethodBlock(int x, int y, StringBlock name) {
        super(x, y, 0, 0);
        this.attached.put(SECTION_NAME, new MethodBlock.StringBlockList(Shapes.ATTACHABLE_HEIGHT, name));
        this.attached.put(SECTION_VALUE, new MethodBlock.VariableBlockList());
        updateSize();
    }

    private void updateSize(){
        int max = Math.max(Blocks.getInstance().getMetrics().stringWidth("Name"), Blocks.getInstance().getMetrics().stringWidth("Value"));
        this.width = max + Shapes.ATTACHABLE_WIDTH + (this.marginX * 2);
        this.height = Math.max((Blocks.getInstance().getFont().getSize() * 2), this.getNameAttachment().getSlotHeight(0)) + (this.marginY * 2);
        VariableBlockList attachment = this.getVariableAttachment();
        for(int A = 0; A < this.getVariableAttachment().getMaxAttachments(); A++){
            this.height += attachment.getSlotHeight(A);
        }
    }

    public StringBlockList getNameAttachment(){
        return (StringBlockList)(Object) this.getAttachments(SECTION_NAME);
    }

    public VariableBlockList getVariableAttachment(){
        return (VariableBlockList) this.getAttachments(SECTION_VALUE);
    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        int relX = x - this.getX();
        int relY = y - this.getY();
        for(String section : this.getSections()){
            try{
                this.getAttachments(section).getSlot(relX, relY);
                return Optional.of(section);
            }catch (IllegalArgumentException ignore){
            }
        }
        return Optional.empty();
    }

    @Override
    public List<GUISection> getUniqueSections(GroupedSection section) {
        List<GUISection> list = new ArrayList<>();
        GroupedSection controlSection = new GroupedSection(section, "Control", Collections.emptySet());
        controlSection.register(new BlockSection(controlSection, BlockType.BLOCK_TYPE_RETURN, "Return"));
        list.add(controlSection);
        return list;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        this.updateSize();
        int lineHeight = 0;
        for(int A = 0; A < this.getVariableAttachment().getMaxAttachments(); A++){
            lineHeight += this.getVariableAttachment().getSlotHeight(A);
        }
        graphics2D.setColor(new Color(50, 55, 50));
        graphics2D.setFont(Blocks.getInstance().getFont());
        graphics2D.fillRoundRect(getX(), getY(), this.getWidth() - Shapes.ATTACHABLE_WIDTH, this.getHeight() - this.marginY - lineHeight, 20, 20);
        graphics2D.fillRect(getX(), getY(), this.marginX, this.getHeight());
        graphics2D.fillRect(getX(), (getY() + this.getHeight()) - this.marginY, this.getWidth(), this.marginY);
        graphics2D.fillPolygon(Shapes.drawAttachingConnector((getX() + this.width) - Shapes.ATTACHABLE_WIDTH, getY() + this.marginY, Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Name", getX() + this.marginX, getY() + this.marginY + (Blocks.getInstance().getFont().getSize() / 2));
    }

    @Override
    public String writeCode(int tab) {
        return "";
    }

    @Override
    public Map<String, Integer> writeBlockCode(int tab) {
        Optional<StringBlock> opStringBlock = this.getNameAttachment().getAttachment();
        if(!opStringBlock.isPresent()){
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if(opStringBlock.get().getValue().length() == 0){
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        String retur = "public static void " + GeneralUntil.formatToMethodName(opStringBlock.get().getValue()) + " () {\n";
        for (Block attachment : this.getVariableAttachment()) {
            retur += Block.tab(tab + 1) + attachment.writeCode(tab) + "\n";
        }
        retur += Block.tab(tab) + "}";
        Map<String, Integer> methodMap = new HashMap<>();
        methodMap.put(retur, CalledBlock.METHOD);
        return methodMap;
    }

    @Override
    public Collection<String> getCodeImports() {
        Optional<StringBlock> opStringBlock = this.getNameAttachment().getAttachment();
        if(!opStringBlock.isPresent()){
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if(opStringBlock.get().getValue().length() == 0){
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        List<String> list = new ArrayList<>();
        for (Block attachment : this.getVariableAttachment()) {
            list.addAll(attachment.getCodeImports());
        }
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_METHOD;
    }
}
