package org.block.serializtion;

import org.block.serializtion.parse.Parser;

import java.util.List;
import java.util.Optional;

public class FixedTitle<N> {

    public static class Listable<N> extends FixedTitle<List<N>>{

        public Listable(String title, Parser<N> parser) {
            super(parser, title);
        }

        public void serialize(ConfigNode node, List<N> value){
            node.setCollection(this.title, ((Parser<N>)this.parser), value);
        }

        public Optional<List<N>> deserialize(ConfigNode node){
            return Optional.of(node.getCollection(this.title, ((Parser<N>)this.parser)));
        }
    }

    protected final String title;
    protected final Parser<?> parser;

    public FixedTitle(String title, Parser<N> parser) {
        this(parser, title);
    }

    private FixedTitle(Parser<?> parser, String title){
        this.title = title;
        this.parser = parser;
    }

    public void serialize(ConfigNode node, N value){
        ((Parser<N>)this.parser).serialize(node, this.title, value);
    }

    public Optional<N> deserialize(ConfigNode node){
        return ((Parser<N>)this.parser).deserialize(node, this.title);
    }

    @Override
    public String toString(){
        return this.title;
    }
}
