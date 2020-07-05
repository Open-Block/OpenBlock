package org.block.serializtion;

import org.block.serializtion.json.JSONConfigImplementation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is all the global settings for loading and saving from a particular implementation of structured file
 * @param <CN> The default configuration node
 */
public interface ConfigImplementation<CN extends ConfigNode> {

    /**
     * Easy access to the implementation for manipulation of JSON files
     */
    JSONConfigImplementation JSON = new JSONConfigImplementation();

    /**
     * Creates a empty root node for creation of the structured file
     * @return An empty node at the root
     */
    CN createEmptyNode();

    /**
     * Loads the root node of the provided text from the structured file.
     * This is useful for if the file was provided from a abnormal location such as user inputted text
     * @param structure the text of the structured file
     * @return The root node
     */
    CN load(String structure);

    /**
     * Writes the structured contents as a String treating the provided node as the root
     * @param node The provided node
     * @return The outputting structured text
     * @throws IllegalArgumentException If the provided node isn't of the correct implementation
     */
    String write(ConfigNode node);

    /**
     * Loads the root node of the provided structured file.
     * @param path The Path to the file
     * @return The Root node
     * @throws java.nio.file.NoSuchFileException If the file does not exist
     * @throws IOException error reading file
     */
    default CN load(Path path) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(path).forEach(l -> builder.append(l + "\n"));
        return load(builder.toString());
    }

    /**
     * Writes the structured contents as a File treating the provided node as the root
     * @param node The provided node
     * @param path The provided file
     * @return The outputting structured text
     * @throws IllegalArgumentException If the provided node isn't of the correct implementation
     * @throws java.nio.file.NoSuchFileException If the file does not exist
     * @throws IOException error reading file
     */
    default String write(ConfigNode node, Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toFile());
        String json = this.write(node);
        writer.write(json);
        writer.flush();
        writer.close();
        return json;
    }

}
