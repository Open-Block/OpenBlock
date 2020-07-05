package org.block.plugin;

import org.block.Blocks;
import org.block.plugin.launch.event.ModuleRegisterEvent;
import org.block.plugin.launch.meta.Dependent;
import org.block.plugin.launch.meta.Inject;
import org.block.plugin.launch.meta.InjectData;
import org.block.plugin.standard.OpenBlockModule;
import org.block.plugin.standard.OpenBlockPlugin;
import org.block.project.module.Module;
import org.block.util.GeneralUntil;
import org.block.util.MapBuilder;
import org.block.util.ReflectProcessor;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PluginContainer implements Comparable<PluginContainer>{

    private Object plugin;
    private boolean isDisabled;
    private File filePath;
    private Set<Module> modules = new HashSet<>();

    public static final PluginContainer OPEN_BLOCK_CONTAINER;
    public static final OpenBlockModule OPEN_BLOCK_MODULE = new OpenBlockModule();

    static {
        PluginContainer container;
        try {
            container = new PluginContainer(new OpenBlockPlugin(), new File(Blocks.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (URISyntaxException e) {
            container = new PluginContainer(new OpenBlockPlugin(), new File("Unknown"));
            System.err.println("Could not find the location of the .jar, this may cause problems later on");
            e.printStackTrace();
        }
        OPEN_BLOCK_CONTAINER = container;
    }

    public PluginContainer(Object plugin, File filePath){
        this(plugin, filePath, false);
    }

    public PluginContainer(Object plugin, File filePath, boolean isDisabled){
        if(!plugin.getClass().isAnnotationPresent(Plugin.class)){
            throw new IllegalArgumentException("The provided plugin does not contain @Plugin");
        }
        this.plugin = plugin;
        this.isDisabled = isDisabled;
        this.filePath = filePath;
    }

    public Set<Module> getModules(){
        if(this.modules.isEmpty()){
            ModuleRegisterEvent event = new ModuleRegisterEvent();
            ReflectProcessor.fireEvent(event, this.getPlugin());
            this.modules.addAll(event.getModule());
        }
        return this.modules;
    }

    public boolean isDisabled(){
        return this.isDisabled;
    }

    public Object getPlugin(){
        return this.plugin;
    }

    public Plugin getPluginMeta(){
        return this.plugin.getClass().getAnnotation(Plugin.class);
    }

    public File getFilePath(){
        return this.filePath;
    }

    public File getConfigDirectory(){
        return new File("config/" + this.getPluginMeta().id() + "/");
    }

    public void delete(){
        GeneralUntil.getFiles(this.getConfigDirectory()).parallelStream().forEach(f -> f.deleteOnExit());
        this.getFilePath().deleteOnExit();
    }

    public void initInjects(){
        initInjects(new MapBuilder.MapArrayBuilder<InjectData, Object>()
                .put(InjectData.PLUGIN_CONTAINER, this)
                .put(InjectData.PLUGIN_LOCATION, this.filePath, this.filePath.toPath())
                .put(InjectData.PLUGIN_SETTINGS, this.getConfigDirectory(), this.getConfigDirectory().toPath())
                .build());
    }

    public void initInjects(Map<InjectData, Object[]> data){
        ReflectProcessor.getDirect(c -> c.getFields(), this.plugin).stream().filter(f -> f.isAnnotationPresent(Inject.class)).forEach(f -> {
            data.entrySet().parallelStream().forEach(entry -> {
                if (!entry.getKey().equals(f.getAnnotation(Inject.class).as())){
                    return;
                }
                for(Object d : entry.getValue()) {
                    try {
                        entry.getKey().apply(this.plugin, f, d);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

    }

    @Override
    public int compareTo(PluginContainer o) {
        for(Dependent dependent : this.getPluginMeta().dependsOn()){
            if (ReflectProcessor.isDependent(dependent, o.getPluginMeta())){
                return 1;
            }
        }
        for(Dependent dependent : o.getPluginMeta().dependsOn()){
            if (ReflectProcessor.isDependent(dependent, this.getPluginMeta())){
                return -1;
            }
        }
        return 0;
    }
}
