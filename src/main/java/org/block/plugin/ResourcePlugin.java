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

    public File copyTo(File file) throws IOException {
        var stream = Blocks.class.getResourceAsStream(this.resourcePath);
        var location = this.getStorageLocation(file);
        if(location.exists()){
            if (!location.delete()){
                throw new IOException("Could not delete file");
            }
        }
        Files.copy(stream, location.toPath());
        return location;
    }
}
