package org.block.plugin;

import org.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;

public enum ResourcePlugin {

    STANDARD_JAVA("StandardJava", "/plugins/StandardJava/StandardJava.json");

    private final String defaultPath;
    private final String[] resourcePaths;

    ResourcePlugin(String defaultPath, String... resourcePath) {
        this.defaultPath = defaultPath;
        this.resourcePaths = resourcePath;
    }

    public File getStorageLocation(File file) {
        return new File(file, this.defaultPath);
    }

    public String[] getResourcePath() {
        return this.resourcePaths;
    }

    public File copyTo(File file) throws IOException {
        var files = new HashMap<String, InputStream>();
        for(var resource : this.resourcePaths){
            var stream = Blocks.class.getResourceAsStream(resource);
            if(stream == null){
                throw new IOException("No Resource at location of '" + resource + "'");
            }
            files.put(resource, stream);
        }
        for(var entry : files.entrySet()) {
            var location = new File(file, entry.getKey().substring(9));
            if (location.exists()) {
                if (!location.delete()) {
                    throw new IOException("Could not delete file");
                }
            }
            Files.createDirectories(location.getParentFile().toPath());
            Files.copy(entry.getValue(), location.toPath());
        }
        return file;
    }
}
