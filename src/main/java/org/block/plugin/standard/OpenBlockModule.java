package org.block.plugin.standard;

import org.block.plugin.PluginContainer;
import org.block.plugin.standard.panel.OpenBlockNewPanel;
import org.block.project.module.project.Project;
import org.block.project.module.Module;
import org.block.project.module.project.UnloadedProject;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.project.panel.inproject.Toolbar;

import java.awt.*;

public final class OpenBlockModule implements Module {

    @Override
    public String getDisplayName() {
        return "Standard Java";
    }

    @Override
    public String getId() {
        return "java_module";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    @Deprecated
    public PluginContainer getPlugin() {
        return PluginContainer.OPEN_BLOCK_CONTAINER;
    }

    @Override
    public boolean canLoad(UnloadedProject project) {
        return false;
    }

    @Override
    public OpenBlockProject load(UnloadedProject project) {
        return new OpenBlockProject(project);
    }

    @Override
    public MainDisplayPanel createPanel(Project project) {
        return new MainDisplayPanel();
    }

    @Override
    public Toolbar createToolbar(Project project) {
        return new Toolbar();
    }

    @Override
    public OpenBlockNewPanel createProjectCreator() {
        return new OpenBlockNewPanel();
    }
}
