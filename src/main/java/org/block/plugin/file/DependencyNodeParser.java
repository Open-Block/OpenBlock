package org.block.plugin.file;

import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DependencyNodeParser implements Parser<DependencyNode> {

    public static final String FILE_PATH_NODE = "FilePath";
    public static final String DOWNLOAD_NODE = "Download";

    @Override
    public Optional<DependencyNode> deserialize(@NotNull ConfigNode node, @NotNull String key) {
        var root = node.getNode(key);
        var opPath = root.getString(FILE_PATH_NODE);
        if (opPath.isEmpty()) {
            return Optional.empty();
        }
        var downloadUrl = root.getString(DOWNLOAD_NODE).orElse(null);
        return Optional.of(new DependencyNode(opPath.get(), downloadUrl));
    }

    @Override
    public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull DependencyNode value) {
        var root = node.getNode(key);
        root.setValue(FILE_PATH_NODE, value.getPath());
        value.getDownloadPath().ifPresent(p -> root.setValue(DOWNLOAD_NODE, p));
    }
}
