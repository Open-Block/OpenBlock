package org.block.plugin.standard;

import org.block.Blocks;
import org.block.plugin.PluginContainer;
import org.block.plugin.standard.panel.OpenBlockNewPanel;
import org.block.project.module.project.Project;
import org.block.project.module.Module;
import org.block.project.module.project.UnloadedProject;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.project.panel.inproject.Toolbar;
import org.block.util.BlockUtils;

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
        OpenBlockProject project1 = new OpenBlockProject(project);
        return project1;
    }

    @Override
    public void loadBlocks(Project.Loaded project) {
        BlockUtils.load(project, b -> {
            project.getPanel().repaint();
            project.getPanel().revalidate();
        });
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
