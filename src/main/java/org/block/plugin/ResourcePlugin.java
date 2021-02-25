package org.block.plugin;

import org.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum ResourcePlugin {

    STANDARD_JAVA("StandardJava.json", "/plugins/StandardJava.json");

    private final String defaultPath;
    private final String resourcePath;

    ResourcePlugin(String defaultPath, String resourcePath) {
        this.defaultPath = defaultPath;
        this.resourcePath = resourcePath;
    }

    public File getStorageLocation(File file) {
        return new File(file, this.defaultPath);
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public void copyTo(File file) throws IOException {
        var stream = Blocks.class.getResourceAsStream(this.resourcePath);
        Files.copy(stream, file.toPath());
    }
}
