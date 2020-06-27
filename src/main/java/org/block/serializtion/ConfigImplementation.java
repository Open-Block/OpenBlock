package org.block.serializtion;

import org.block.serializtion.json.JSONConfigImplementation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ConfigImplementation {

    JSONConfigImplementation JSON = new JSONConfigImplementation();

    ConfigNode createEmptyNode();
    ConfigNode load(String json);

    String write(ConfigNode node);

    default ConfigNode load(Path path) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(path).forEach(l -> builder.append(l + "\n"));
        return load(builder.toString());
    }

    default String write(ConfigNode node, Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toFile());
        String json = this.write(node);
        writer.write(json);
        writer.flush();
        writer.close();
        return json;
    }

}
