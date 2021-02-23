package org.block.project.block.java.method;

import org.block.Blocks;
import org.block.panel.main.FXMainDisplay;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.group.AbstractBlockSector;
import org.block.project.block.group.BlockSector;
import org.block.project.block.group.UnlimitedBlockGroup;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.called.CalledBlock;
import org.block.project.block.type.called.CodeStartBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.block.util.GeneralUntil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class MethodBlock extends AbstractAttachableBlock implements CodeStartBlock {

    public static final String SECTION_BODY = "method:body";
    public static final String SECTION_PARAMETER = "method:parameter";
    public static final String SECTION_NAME = "method:name";
    private int marginX = 8;
    private int marginY = 8;

    public MethodBlock(int x, int y) {
        this(x, y, null);
    }

    public MethodBlock(int x, int y, StringBlock block) {
        super(x, y);
        this.blockGroups.add(new MethodBlock.NameBlockGroup(SECTION_NAME, "Name", block));
        /*this.attached.put(SECTION_NAME, "Name", new MethodBlock.StringBlockList(12, name));
        this.attached.put(SECTION_VALUE, "Value", new MethodBlock.VariableBlockList());*/
    }

    public BodyBlockGroup getBodyBlockGroup() {
        return (BodyBlockGroup) this.getGroup(SECTION_BODY).get();
    }

    public NameBlockGroup getNameBlockGroup() {
        return (NameBlockGroup) this.getGroup(SECTION_NAME).get();
    }

    @Override
    public BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public String writeCode(int tab) {
        return "";
    }

    @Override
    public Collection<String> getCodeImports() {
        Optional<StringBlock> opStringBlock = this.getNameBlockGroup().getSector().getAttachedBlock();
        if (opStringBlock.isEmpty()) {
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if (opStringBlock.get().getValue().length() == 0) {
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        List<String> imports = new ArrayList<>();
        for (BlockSector<?> sector : this.getBodyBlockGroup().getSectors()) {
            if (sector.getAttachedBlock().isEmpty()) {
                continue;
            }
            imports.addAll(sector.getAttachedBlock().get().getCodeImports());
        }
        return Collections.unmodifiableCollection(imports);
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_METHOD;
    }

    @Override
    public Map<String, Integer> writeBlockCode(int tab) {
        Optional<StringBlock> opStringBlock = this.getNameBlockGroup().getSector().getAttachedBlock();
        if (opStringBlock.isEmpty()) {
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if (opStringBlock.get().getValue().length() == 0) {
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        String retur = "public static void " + GeneralUntil.formatToMethodName(opStringBlock.get().getValue()) + " () {\n";
        for (BlockSector<?> sector : this.getBodyBlockGroup().getSectors()) {
            if (sector.getAttachedBlock().isEmpty()) {
                continue;
            }
            retur += Block.tab(tab + 1) + sector.getAttachedBlock().get().writeCode(tab) + "\n";
        }
        retur += Block.tab(tab) + "}";
        Map<String, Integer> methodMap = new HashMap<>();
        methodMap.put(retur, CalledBlock.METHOD);
        return methodMap;
    }

    public static class MethodBlockType implements BlockType<MethodBlock> {

        public static final FixedTitle<UUID> TITLE = new FixedTitle<>("title", Parser.UNIQUE_ID);

        @Override
        public MethodBlock build(int x, int y) {
            return new MethodBlock(x, y);
        }

        @Override
        public MethodBlock build(ConfigNode node) {
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if (opUUID.isEmpty()) {
                throw new IllegalStateException("Unknown Unique Id");
            }
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if (opX.isEmpty()) {
                throw new IllegalStateException("Unknown X position");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if (opY.isEmpty()) {
                throw new IllegalStateException("Unknown Y position");
            }
            List<UUID> connected = TITLE_DEPENDS.deserialize(node).get();
            FXMainDisplay panel = (FXMainDisplay) Blocks.getInstance().getWindow();
            List<Block> blocks = panel.getDisplayingBlocks();
            MethodBlock methodBlock = new MethodBlock(opX.get(), opY.get());
            methodBlock.id = opUUID.get();


            BodyBlockGroup bodyBlockGroup = methodBlock.getBodyBlockGroup();
            for (int A = 0; A < connected.size(); A++) {
                UUID uuid = connected.get(A);
                Optional<Block> opBlock = blocks.stream().filter(b -> b.getUniqueId().equals(uuid)).findAny();
                if (opBlock.isEmpty()) {
                    throw new IllegalStateException("Unable to find dependency of " + uuid.toString());
                }
                if (!(opBlock.get() instanceof ValueBlock)) {
                    throw new IllegalStateException("Attached block was not a value block");
                }
                bodyBlockGroup.addSector(opBlock.get(), A);
            }

            TITLE.deserialize(node)
                    .flatMap(uuid -> blocks
                            .stream()
                            .filter(b -> b.getUniqueId().equals(uuid))
                            .findAny())
                    .ifPresent(block -> methodBlock
                            .getNameBlockGroup()
                            .getSector().setAttachedBlock(block));
            return methodBlock;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/start/method");
        }

        @Override
        public String getName() {
            return "Method defined";
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull MethodBlock block) {
            BlockType.super.write(node, block);
            List<UUID> bodyBlocks = new ArrayList<>();

            BodyBlockGroup bodyBlockGroup = block.getBodyBlockGroup();
            for (BlockSector<?> sector : bodyBlockGroup.getSectors()) {
                sector.getAttachedBlock().ifPresent(b -> bodyBlocks.add(b.getUniqueId()));
            }
            TITLE_DEPENDS.serialize(node, bodyBlocks);
            block.getNameBlockGroup().getSector().getAttachedBlock().ifPresent(b -> TITLE.serialize(node, b.getUniqueId()));
        }
    }

    public class NameBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<StringBlock> {

        public NameBlockGroup(String id, String name, @Nullable StringBlock block) {
            super(id, name, MethodBlock.this.marginY);
            this.sector = new AbstractBlockSector<>(this, StringBlock.class, block);
        }
    }

    public class BodyBlockGroup extends AbstractBlockGroup.AbstractListBlockGroup implements UnlimitedBlockGroup {

        public BodyBlockGroup(String id, String name, int relativeY) {
            super(id, name, relativeY);
        }

        @Override
        public boolean canAccept(Block block) {
            return true;
        }

        @Override
        public boolean addSector(Block block, int pos) {
            if (block == null) {
                return false;
            }
            AbstractBlockSector<Block> sector = new AbstractBlockSector<>(this, Block.class, block);
            this.blockSectors.add(pos, sector);
            return true;
        }
    }
}
