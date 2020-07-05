package org.block.project.module;

import org.block.plugin.PluginContainer;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.project.panel.inproject.Toolbar;

import java.awt.*;

public interface Module {

    /**
     * Gets the display name of the module
     * @return The display name of the module
     */
    String getDisplayName();

    /**
     * Gets the ID of the module, this will be used to determine what module a project belongs to
     * @return The ID of the module
     */
    String getId();

    /**
     * Gets the module version
     * @return The version of the module
     */
    String getVersion();

    /**
     * Gets the plugin that loaded the module, this is the instance of the plugins launch code {@link org.block.plugin.Plugin}
     * @return The instanceof the plugin that loaded the module
     */
    PluginContainer getPlugin();

    /**
     * Checks if the module can load the project
     * This is used if the original module for the project can not be found anymore, there is a chance that the project
     * can still be used. The project will not have your module's id and will throw an exception if you run
     * {@link Project#getExpectedModule()}
     * @param project The project to check
     * @return If your module can load the project
     */
    boolean canLoad(UnloadedProject project);

    /**
     * Loads the basic information of the project
     * @param project The project to load
     * @return The loaded project
     * @throws IllegalArgumentException If the project does not belong to the module
     */
    Project.Loaded load(UnloadedProject project);

    void loadBlocks(Project.Loaded loaded);

    /**
     * Creates the MainDisplayPanel and loads the project into the panel.
     * The loading of the project does not need to appear right away, meaning you are able to load the project asynced
     * @param project The project to load
     * @return The MainDisplayPanel to insert into the frame
     * @throws IllegalArgumentException If the project does not belong to the module
     */
    MainDisplayPanel createPanel(Project project);

    /**
     * Creates the Toolbar to display on the frame
     * All the options do not need to be loaded right away, meaning you are able to load project specific options asynced
     * @param project The project to load with the toolbar
     * @return The toolbar to display
     * @throws IllegalArgumentException If the project does not belong to the module
     */
    Toolbar createToolbar(Project project);

    Container createProjectCreator();
}
