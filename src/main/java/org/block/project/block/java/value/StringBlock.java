package org.block.project.block.java.value;

import org.block.project.block.BlockType;
import org.block.project.block.type.value.MutableConnectedValueBlock;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StringBlock extends AbstractValue<String> implements MutableConnectedValueBlock<String> {

    public StringBlock(String value){
        this(null, value);
    }

    public StringBlock(UUID uuid, String value) {
        this(uuid, value, v -> v);
    }


    public StringBlock(UUID uuid, String value, Function<String, String> toString) {
        super(uuid, value, toString);
        //this.registerEventListener(new OnClickListener());
    }

    @Override
    public String writeCode(int tab) {
        return '\"' + this.getValue() + "\"";
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.emptyList();
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_STRING;
    }

    @Override
    public void showValueDialog(Consumer<String> consumer) {

    }

    public static class StringBlockType implements BlockType<StringBlock> {

        public static final FixedTitle<String> TITLE = new FixedTitle<>("Title", Parser.STRING);

        @Override
        public StringBlock build() {
            return new StringBlock("");
        }

        @Override
        public StringBlock build(ConfigNode node) {
            Optional<Double> opX = TITLE_X.deserialize(node);
            if (opX.isEmpty()) {
                throw new IllegalStateException("Could not find position X");
            }
            Optional<Double> opY = TITLE_Y.deserialize(node);
            if (opY.isEmpty()) {
                throw new IllegalStateException("Could not find position Y");
            }
            Optional<String> opValue = node.getString("Title");
            if (opValue.isEmpty()) {
                throw new IllegalStateException("Could not find value");
            }
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            StringBlock block = this.build();
            block.setPosition(opX.get(), opY.get());
            block.setValue(opValue.get());
            block.id = opUUID.get();
            TITLE.deserialize(node).ifPresent(v -> block.value = v);
            return block;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/value/string");
        }

        @Override
        public String getName() {
            return "String";
        }

        @Override
        public void write(@NotNull ConfigNode node, @NotNull StringBlock block) {
            BlockType.super.write(node, block);
            TITLE.serialize(node, block.getText());
        }

        @Override
        public StringBlock buildDefault() {
            return new StringBlock("Text");
        }
    }

}
