package org.block.project.module.project;

import javafx.scene.shape.Rectangle;
import org.block.Blocks;
import org.block.plugin.PluginContainer;
import org.block.project.module.Module;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;
import org.block.serialization.json.JSONConfigNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class UnloadedProject implements Project {

    private final File path;
    private String tempName;
    private Module tempModule;
    private TreeSet<String> tempVersions = new TreeSet<>();

    public UnloadedProject(File file){
        this.path = file;
    }

    public void setTempName(String name){
        this.tempName = name;
    }

    public void setTempModule(Module module){
        this.tempModule = module;
    }

    public void addTempVersions(String... versions){
        this.addTempVersions(Arrays.asList(versions));
    }

    public void addTempVersions(Collection<String> versions){
        this.tempVersions.addAll(versions);
    }

    public void saveTempData() throws IOException {
        File file = this.getFile();
        if (!file.exists()){
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
        Project.CONFIG_MODULE.serialize(node, this.tempModule);
        Project.CONFIG_MODULE_VERSION.serialize(node, this.tempModule.getVersion());
        Project.CONFIG_PROJECT_VERSION.serialize(node, new ArrayList<>(this.tempVersions));
        Project.CONFIG_PROJECT_WINDOW_LOCATION.serialize(node, new Rectangle(0, 0, 600, 800));
        ConfigImplementation.JSON.write(node, getFile().toPath());
    }

    public Project.Loaded load(ConfigImplementation<? extends ConfigNode> impl){
        try {
            return load(this.getExpectedModule(), impl);
        } catch (IOException e) {
            Set<Module> modules = Blocks.getInstance().getEnabledPlugins().getAll(PluginContainer::getModules);
            Optional<Module> opMod = modules.parallelStream().filter(m -> m.canLoad(this)).findFirst();
            if(opMod.isPresent()){
                return load(opMod.get(), impl);
            }
        }
        throw new IllegalStateException("No module found to load project");
    }

    public Project.Loaded load(Module module, ConfigImplementation<? extends ConfigNode> impl){
        Project.Loaded loaded = module.load(this, impl);
        Blocks.getInstance().setLoadedProject(loaded);
        return loaded;
    }

    @Override
    public File getDirectory() {
        return this.path;
    }
}
