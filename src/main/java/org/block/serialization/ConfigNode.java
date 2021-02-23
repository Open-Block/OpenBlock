package org.block.serialization;

import org.block.serialization.parse.Parser;

import java.util.*;

/**
 * This is a location within a structured file such as JSON, YAML, etc that provides the basic functions to that location
 */
public interface ConfigNode {

    /**
     * Gets a child node
     *
     * @param path The path to the child from the current node
     * @return The child node
     */
    ConfigNode getNode(String... path);

    /**
     * Gets a String value from the title
     *
     * @param title The key to the location
     * @return The String from the title, returns {@link Optional#empty()} if none can be found
     */
    Optional<String> getString(String title);

    /**
     * Gets a Integer value from the title
     *
     * @param title The key to the location
     * @return The Integer from the title, returns {@link Optional#empty()} if none can be found. Depending on the implementation this may return '0' if no value can be found.
     */
    Optional<Integer> getInteger(String title);

    /**
     * Gets a Long value from the title
     *
     * @param title The key to the location
     * @return The Long from the title, returns {@link Optional#empty()} if none can be found. Depending on the implementation this may return '0' if no value can be found.
     */
    Optional<Long> getLong(String title);

    /**
     * Gets a Double value from the title
     *
     * @param title The key to the location
     * @return The Double from the title, returns {@link Optional#empty()} if none can be found. Depending on the implementation this may return '0.0' if no value can be found.
     */
    Optional<Double> getDouble(String title);

    /**
     * Gets a Float value from the title
     *
     * @param title The key to the location
     * @return The Float from the title, returns {@link Optional#empty()} if none can be found. Depending on the implementation this may return '0.0' if no value can be found.
     */
    Optional<Float> getFloat(String title);

    Optional<Boolean> getBoolean(String title);

    /**
     * Gets a none standard value from the provided title
     *
     * @param title  The key to the location
     * @param parser The parser to convert the config to the value
     * @param <T>    The expected class type of the value
     * @return A Optional of the value. If none can be found then this will return {@link Optional#empty()}
     */
    <T> Optional<T> getValue(String title, Parser<T> parser);

    /**
     * Gets a collection from the title and adds the found collection to the provided one.
     *
     * @param title  The key to the location
     * @param parser The parser to parse each value within the collection
     * @param into   The collection to add to, this must be modifiable
     * @param <T>    The expected class type for each value within the collection
     * @param <C>    The collection class type
     * @return The same as into
     */
    <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into);

    /**
     * Sets a value within the ConfigNode at the title
     *
     * @param title The key to the location
     * @param value The value to set
     * @throws IllegalArgumentException Is thrown if the value specified is unsupported. All standard values are supported, none standard should be provided with a parser @see {@link ConfigNode#setValue(String, Parser, Object)}, collections and arrays should use @see {@link ConfigNode#setCollection(String, Parser, Collection)}
     */
    void setValue(String title, Object value);

    /**
     * Sets a value within the ConfigNode at the title
     *
     * @param title  The key to the location
     * @param parser The parser to serialize the object correctly
     * @param value  The value to set
     * @param <T>    The class type of the value
     */
    <T> void setValue(String title, Parser<T> parser, T value);

    /**
     * Sets a collection within the ConfigNode at the title
     *
     * @param title      The key to the location
     * @param collection The collection to set
     * @throws IllegalArgumentException Will throw if the collection is not supported by the implementation. None standard collections should use {@link ConfigNode#setCollection(String, Parser, Collection)}
     */
    void setCollection(String title, Collection<?> collection);

    /**
     * Sets a collection within the ConfigNode at the title
     *
     * @param title      The key to the location
     * @param parser     The parser to parse each value within the config
     * @param collection The collection to set
     * @param <T>        The class type of each value within the collection
     */
    <T> void setCollection(String title, Parser<T> parser, Collection<T> collection);

    /**
     * Gets a ordered list from the title
     *
     * @param title  The key to the location
     * @param parser The parser to parse each value within the collection
     * @param <T>    The class type of each value within the collection
     * @return A ordered list with each value provided. If no entries are found the list will be empty.
     */
    default <T> List<T> getCollection(String title, Parser<T> parser) {
        return this.getCollection(title, parser, new ArrayList<>());
    }

    /**
     * Sets a Array within the ConfigNode at the title
     *
     * @param title      The key to the location
     * @param parser     The parser to parse each value within the collection
     * @param collection The Array to set
     * @param <T>        The class type of each value within the collection
     */
    default <T> void setCollection(String title, Parser<T> parser, T... collection) {
        this.setCollection(title, parser, Arrays.asList(collection));
    }


}
