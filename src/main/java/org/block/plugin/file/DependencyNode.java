package org.block.plugin.file;

import org.block.jsf.data.jsfclass.JSFClass;
import org.jetbrains.annotations.Nullable;
import org.util.async.AsyncCollectionResult;

import java.util.Optional;
import java.util.Set;

public class DependencyNode {

    private final String pathValue;
    private final String downloadValue;

    public DependencyNode(String pathValue, @Nullable String downloadValue) {
        this.pathValue = pathValue;
        this.downloadValue = downloadValue;
    }

    public String getPath() {
        return this.pathValue;
    }

    public Optional<String> getDownloadPath() {
        return Optional.ofNullable(this.downloadValue);
    }

    public void getClasses(AsyncCollectionResult<JSFClass, Set<JSFClass>> classes) {
        throw new IllegalStateException("Not implemented");
    }
}
