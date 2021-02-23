package org.block.plugin;

import org.block.plugin.launch.meta.Dependent;
import org.block.plugin.launch.meta.Inject;
import org.block.plugin.launch.meta.InjectData;
import org.block.plugin.standard.OpenBlockModule;
import org.block.plugin.standard.OpenBlockPlugin;
import org.block.project.module.Module;
import org.block.util.GeneralUntil;
import org.block.util.MapBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This provides the standard data for every plugin launch. Every plugin will have one and should be
 * used for if a plugin is required as a return or a parameter.
 */
public final class PluginContainer implements Comparable<PluginContainer> {

    public static final OpenBlockModule OPEN_BLOCK_MODULE = new OpenBlockModule();

    public static final PluginContainer OPEN_BLOCK_CONTAINER = new PluginContainer(new OpenBlockPlugin(), null, OPEN_BLOCK_MODULE);


    private final Object plugin;
    private final boolean isDisabled;
    private final File filePath;
    private final Module[] modules;

    /**
     * Init
     *
     * @param plugin   The main of the plugin
     * @param filePath The file path to the plugin
     * @throws IllegalArgumentException If the "plugin" does not contain @Plugin
     */
    public PluginContainer(Object plugin, @Nullable File filePath, Module... modules) {
        this(plugin, filePath, false, modules);
    }

    /**
     * Init
     * Provided "isDisabled" for plugins that are specified within the plugins folder however are disabled
     *
     * @param plugin     The main of the plugin
     * @param filePath   The file path to the public
     * @param isDisabled If the plugin is disabled
     */
    public PluginContainer(Object plugin, @Nullable File filePath, boolean isDisabled, Module... modules) {
        if (!plugin.getClass().isAnnotationPresent(Plugin.class)) {
            throw new IllegalArgumentException("The provided plugin does not contain @Plugin");
        }
        if (modules.length == 0) {
            throw new IllegalArgumentException("The provided modules list cannot be empty");
        }
        this.plugin = plugin;
        this.isDisabled = isDisabled;
        this.filePath = filePath;
        this.modules = modules;
    }

    /**
     * Gets the modules from the plugin
     *
     * @return The module from the plugins
     */
    public Module[] getModules() {
        return this.modules;
    }

    /**
     * Checks if the plugin is disabled
     *
     * @return If the plugin is disabled
     */
    public boolean isDisabled() {
        return this.isDisabled;
    }

    /**
     * Gets the main of the plugin
     *
     * @return The mains instance
     */
    public Object getPlugin() {
        return this.plugin;
    }

    /**
     * Gets the @Plugin from the mains instance
     *
     * @return The mains instance
     */
    public Plugin getPluginMeta() {
        return this.plugin.getClass().getAnnotation(Plugin.class);
    }

    /**
     * Gets the location of the plugin
     *
     * @return The location of the plugin
     */
    public Optional<File> getFilePath() {
        return Optional.ofNullable(this.filePath);
    }

    /**
     * Gets the plugins directory for plugin specific general files such as config files.
     * These will be removed if the user uninstalls the plugin though the GUI with the option to remove config enabled
     *
     * @return The folder location of the plugins directory for general files
     */
    public File getConfigDirectory() {
        return new File("config/" + this.getPluginMeta().id() + "/");
    }

    /**
     * Deletes the plugin
     *
     * @param removeConfigFiles If true, then all general files for the plugin will be removed
     */
    public void delete(boolean removeConfigFiles) {
        if (removeConfigFiles) {
            GeneralUntil.getFiles(this.getConfigDirectory()).parallelStream().forEach(File::deleteOnExit);
        }
        this.getFilePath().ifPresent(File::deleteOnExit);
    }

    /**
     * This is used for the plugin loader, ignore
     */
    public void initInjects() {
        this.initInjects(new MapBuilder.MapArrayBuilder<InjectData, Object>()
                .put(InjectData.PLUGIN_CONTAINER, this)
                .put(InjectData.PLUGIN_LOCATION, this.filePath, this.filePath.toPath())
                .put(InjectData.PLUGIN_SETTINGS, this.getConfigDirectory(), this.getConfigDirectory().toPath())
                .build());
    }

    /**
     * This is used for the plugin loader, ignore
     *
     * @param data
     */
    public void initInjects(Map<InjectData, Object[]> data) {
        Stream
                .of(this.plugin.getClass().getDeclaredFields())
                .parallel()
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .forEach(f -> data.entrySet().parallelStream().forEach(entry -> {
                    if (!entry.getKey().equals(f.getAnnotation(Inject.class).as())) {
                        return;
                    }
                    for (Object d : entry.getValue()) {
                        try {
                            entry.getKey().apply(this.plugin, f, d);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    /**
     * Gives a order to all PluginContainers
     * Most plugins will be of equal value, however those that depend on other plugins will be at higher value
     *
     * @param o The pluginContainer to compare
     * @return The compared value
     */
    @Override
    public int compareTo(PluginContainer o) {
        for (Dependent dependent : this.getPluginMeta().dependsOn()) {
            if (Stream.of(o.getPluginMeta().dependsOn()).parallel().anyMatch(d -> d.equals(dependent))) {
                return 1;
            }
        }
        for (Dependent dependent : o.getPluginMeta().dependsOn()) {
            if (Stream.of(o.getPluginMeta().dependsOn()).parallel().anyMatch(d -> d.equals(dependent))) {
                return -1;
            }
        }
        return 0;
    }
}
