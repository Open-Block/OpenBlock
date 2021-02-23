package org.block.project.module;

import javafx.scene.layout.Region;
import org.block.plugin.PluginContainer;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

public interface Module {

    /**
     * Gets the display name of the module
     *
     * @return The display name of the module
     */
    String getDisplayName();

    /**
     * Gets the ID of the module, this will be used to determine what module a project belongs to
     *
     * @return The ID of the module
     */
    String getId();

    /**
     * Gets the module version
     *
     * @return The version of the module
     */
    String getVersion();

    /**
     * Gets the plugin that loaded the module, this is the instance of the plugins launch code {@link org.block.plugin.Plugin}
     *
     * @return The instanceof the plugin that loaded the module
     */
    PluginContainer getPlugin();

    /**
     * Checks if the module can load the project
     * This is used if the original module for the project can not be found anymore, there is a chance that the project
     * can still be used. The project will not have your module's id and will throw an exception if you run
     * {@link Project#getExpectedModule()}
     *
     * @param project The project to check
     * @return If your module can load the project
     */
    boolean canLoad(UnloadedProject project);

    /**
     * Loads the basic information of the project
     *
     * @param project The project to load
     * @return The loaded project
     * @throws IllegalArgumentException If the project does not belong to the module
     */
    Project.Loaded load(UnloadedProject project, ConfigImplementation<? extends ConfigNode> impl);

    void loadBlocks(Project.Loaded loaded, ConfigImplementation<? extends ConfigNode> impl);

    /**
     * Called when a project creator for this module is called. This is typically caused
     * when the ProjectPanel attempts to create a new project of this module.
     */
    void onProjectCreator();

    /**
     * Gets the display node for displaying the projects info before the project has loaded.
     * This does not include the buttons for loading and deleting.
     *
     * @param project The project to get the info for
     * @return The node to display
     */
    @NotNull Region createDisplayInfo(@NotNull UnloadedProject project);
}
