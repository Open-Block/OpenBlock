package org.block.serialization;

import org.block.serialization.parse.Parser;

import java.util.List;
import java.util.Optional;

/**
 * If a location is known and used a lot within the config, it maybe simpler to use a FixedTitle which
 * includes the location and parser.
 * @param <N> The common expected class type of the output
 */
public class FixedTitle<N> {

    /**
     * A list version of a FixedTitle
     * @param <N> The class type of the value inside the list
     */
    public static class Listable<N> extends FixedTitle<List<N>>{

        /**
         * Init the Listable
         * @param title The location on the target node
         * @param node The target node
         * @param parser The parser
         */
        public Listable(String title, Parser<N> parser, String... node) {
            super(parser, title, node);
        }

        public void serialize(ConfigNode node, List<N> value){
            node.getNode(this.node).setCollection(this.title, ((Parser<N>)this.parser), value);
        }

        public Optional<List<N>> deserialize(ConfigNode node){
            return Optional.of(node.getNode(this.node).getCollection(this.title, ((Parser<N>)this.parser)));
        }
    }

    protected final String title;
    protected final Parser<?> parser;
    protected final String[] node;

    /**
     * Init the FixedTitle
     * @param title The location on the target node
     * @param parser The parser
     * @param node The target node
     */
    public FixedTitle(String title, Parser<N> parser, String... node) {
        this(parser, title, node);
    }

    /**
     * Used for extending. Should be ignored otherwise
     * @param title The location on the target node
     * @param parser The parser
     * @param node The target node
     */
    protected FixedTitle(Parser<?> parser, String title, String... node){
        this.title = title;
        this.parser = parser;
        this.node = node;
    }

    /**
     * Saves the target to the provided node
     * @param node The root node
     * @param value The target
     */
    public void serialize(ConfigNode node, N value){
        ((Parser<N>)this.parser).serialize(node.getNode(this.node), this.title, value);
    }

    /**
     * Gets the target from the provided node
     * @param node The root node
     * @return The value from the node
     */
    public Optional<N> deserialize(ConfigNode node){
        return ((Parser<N>)this.parser).deserialize(node.getNode(this.node), this.title);
    }

    @Override
    public String toString(){
        return this.title;
    }
}
