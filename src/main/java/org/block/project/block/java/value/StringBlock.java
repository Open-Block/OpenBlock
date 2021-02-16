package org.block.project.block.java.value;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.plugin.event.EventListener;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class StringBlock extends AbstractValue<String> implements Block.ValueBlock.ConnectedValueBlock.MutableConnectedValueBlock<String>{


    public static class StringBlockType implements BlockType<StringBlock> {

        public static final FixedTitle<String> TITLE = new FixedTitle<>("Title", Parser.STRING);

        @Override
        public StringBlock buildDefault(int x, int y) {
            return new StringBlock(x, y, "Text");
        }

        @Override
        public StringBlock build(int x, int y) {
            return new StringBlock(x, y, "");
        }

        @Override
        public StringBlock build(ConfigNode node) {
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(!opX.isPresent()){
                throw new IllegalStateException("Could not find position X");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(!opY.isPresent()){
                throw new IllegalStateException("Could not find position Y");
            }
            Optional<String> opValue = node.getString("Title");
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find value");
            }
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find ID");
            }
            StringBlock block = this.build(opX.get(), opY.get());
            block.setValue(opValue.get());
            block.id = opUUID.get();
            TITLE.deserialize(node).ifPresent(v -> block.value = v);
            return block;
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull StringBlock block) {
            BlockType.super.write(node, block);
            TITLE.serialize(node, block.getText());
        }

        @Override
        public File saveLocation() {
            return new File("blocks/value/string");
        }

        @Override
        public String getName() {
            return "String";
        }
    }


    public StringBlock(int x, int y, String value) {
        this(x, y, value, v -> v);
    }

    public StringBlock(int x, int y, String value, Function<String, String> toString) {
        super(x, y, value, toString);
        //this.registerEventListener(new OnClickListener());
    }

    @Override
    public BlockGraphics getGraphicShape() {
        return null;
    }

    @Override
    public String writeCode(int tab) {
        return '\"' + this.getValue() + "\"";
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.unmodifiableList(Collections.emptyList());
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_STRING;
    }

}
