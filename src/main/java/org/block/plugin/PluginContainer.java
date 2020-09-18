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
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This provides the standard data for every plugin launch. Every plugin will have one and should be
 * used for if a plugin is required as a return or a parameter.
 */
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

    /**
     * Init
     * @param plugin The main of the plugin
     * @param filePath The file path to the plugin
     * @throws IllegalArgumentException If the "plugin" does not contain @Plugin
     */
    public PluginContainer(Object plugin, File filePath){
        this(plugin, filePath, false);
    }

    /**
     * Init
     * Provided "isDisabled" for plugins that are specified within the plugins folder however are disabled
     * @param plugin The main of the plugin
     * @param filePath The file path to the public
     * @param isDisabled If the plugin is disabled
     */
    public PluginContainer(Object plugin, File filePath, boolean isDisabled){
        if(!plugin.getClass().isAnnotationPresent(Plugin.class)){
            throw new IllegalArgumentException("The provided plugin does not contain @Plugin");
        }
        this.plugin = plugin;
        this.isDisabled = isDisabled;
        this.filePath = filePath;
    }

    /**
     * Gets the modules from the plugin
     * @return The module from the plugins
     */
    public Set<Module> getModules(){
        if(this.modules.isEmpty()){
            ModuleRegisterEvent event = new ModuleRegisterEvent();
            ReflectProcessor.fireEvent(event, this.getPlugin());
            this.modules.addAll(event.getModule());
        }
        return this.modules;
    }

    /**
     * Checks if the plugin is disabled
     * @return If the plugin is disabled
     */
    public boolean isDisabled(){
        return this.isDisabled;
    }

    /**
     * Gets the main of the plugin
     * @return The mains instance
     */
    public Object getPlugin(){
        return this.plugin;
    }

    /**
     * Gets the @Plugin from the mains instance
     * @return The mains instance
     */
    public Plugin getPluginMeta(){
        return this.plugin.getClass().getAnnotation(Plugin.class);
    }

    /**
     * Gets the location of the plugin
     * @return The location of the plugin
     */
    public File getFilePath(){
        return this.filePath;
    }

    /**
     * Gets the plugins directory for plugin specific general files such as config files.
     * These will be removed if the user uninstalls the plugin though the GUI with the option to remove config enabled
     * @return The folder location of the plugins directory for general files
     */
    public File getConfigDirectory(){
        return new File("config/" + this.getPluginMeta().id() + "/");
    }

    /**
     * Deletes the plugin
     * @param removeConfigFiles If true, then all general files for the plugin will be removed
     */
    public void delete(boolean removeConfigFiles){
        if(removeConfigFiles) {
            GeneralUntil.getFiles(this.getConfigDirectory()).parallelStream().forEach(f -> f.deleteOnExit());
        }
        this.getFilePath().deleteOnExit();
    }

    /**
     * This is used for the plugin loader, ignore
     */
    public void initInjects(){
        initInjects(new MapBuilder.MapArrayBuilder<InjectData, Object>()
                .put(InjectData.PLUGIN_CONTAINER, this)
                .put(InjectData.PLUGIN_LOCATION, this.filePath, this.filePath.toPath())
                .put(InjectData.PLUGIN_SETTINGS, this.getConfigDirectory(), this.getConfigDirectory().toPath())
                .build());
    }

    /**
     * This is used for the plugin loader, ignore
     * @param data
     */
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

    /**
     * Gives a order to all PluginContainers
     * Most plugins will be of equal value, however those that depend on other plugins will be at higher value
     * @param o The pluginContainer to compare
     * @return The compared value
     */
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
