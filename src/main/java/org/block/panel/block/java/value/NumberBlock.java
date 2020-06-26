package org.block.panel.block.java.value;

import org.block.panel.block.BlockType;
import org.block.serializtion.ConfigNode;
import org.block.serializtion.FixedTitle;
import org.block.serializtion.parse.Parser;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class NumberBlock<V extends Number> extends AbstractValue<V> {

    public static class NumberBlockType<N extends Number> implements BlockType<NumberBlock<N>>{

        private final Parser<N> parser;
        private final N defaul;

        public NumberBlockType(Parser<N> parser, N defau){
            this.parser = parser;
            this.defaul = defau;
        }

        @Override
        public NumberBlock<N> build(int x, int y) {
            return new NumberBlock<>(x, y, this.defaul);
        }

        @Override
        public NumberBlock<N> build(ConfigNode node) {
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(!opX.isPresent()){
                throw new IllegalStateException("Could not find position X");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(!opY.isPresent()){
                throw new IllegalStateException("Could not find position Y");
            }
            Optional<N> opValue = node.getValue("Title", this.parser);
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find value");
            }
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find ID");
            }
            NumberBlock<N> block = new NumberBlock<>(opX.get(), opY.get(), opValue.get());
            block.id = opUUID.get();
            return block;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/value/number");
        }

        @Override
        public String getName() {
            return "Number";
        }
    }

    public NumberBlock(int x, int y, V value) {
        super(x, y, value, (n) -> n.toString());
    }

    @Override
    public String writeCode() {
        return this.getValue().toString();
    }
}
