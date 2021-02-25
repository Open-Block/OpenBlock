package org.block.project;

import javafx.scene.shape.Rectangle;
import org.block.Blocks;
import org.block.plugin.Plugin;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;
import org.block.serialization.json.JSONConfigNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class UnloadedProject implements Project {

    private final File path;
    private String tempName;
    private Plugin tempPlugin;
    private TreeSet<String> tempVersions = new TreeSet<>();

    public UnloadedProject(File file) {
        this.path = file;
    }

    public void setTempName(String name) {
        this.tempName = name;
    }

    public void setTempPlugin(Plugin module) {
        this.tempPlugin = module;
    }

    public void addTempVersions(String... versions) {
        this.addTempVersions(Arrays.asList(versions));
    }

    public void addTempVersions(Collection<String> versions) {
        this.tempVersions.addAll(versions);
    }

    public void saveTempData() throws IOException {
        File file = this.getFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        JSONConfigNode node = ConfigImplementation.JSON.createEmptyNode();
        Project.CONFIG_PROJECT_NAME.serialize(node, this.tempName);
        Project.CONFIG_PLUGIN.serialize(node, this.tempPlugin);
        Project.CONFIG_PLUGIN_VERSION.serialize(node, this.tempPlugin.getVersion());
        Project.CONFIG_PROJECT_VERSION.serialize(node, new ArrayList<>(this.tempVersions));
        Project.CONFIG_PROJECT_WINDOW_LOCATION.serialize(node, new Rectangle(0, 0, 600, 800));
        ConfigImplementation.JSON.write(node, this.getFile().toPath());
    }

    public Project.Loaded load(ConfigImplementation<? extends ConfigNode> impl) {
        try {
            return this.load(this.getExpectedPlugin(), impl);
        } catch (IOException e) {
            throw new IllegalStateException("No plugin found to load project", e);
        }
    }

    public Project.Loaded load(Plugin plugin, ConfigImplementation<? extends ConfigNode> impl) {
        Project.Loaded loaded = plugin.load(this, impl);
        Blocks.getInstance().setLoadedProject(loaded);
        return loaded;
    }

    @Override
    public File getDirectory() {
        return this.path;
    }
}
