package org.block.plugin.standard;

import org.block.plugin.PluginContainer;
import org.block.project.module.Module;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;
import org.block.project.legacypanel.inproject.MainDisplayPanel;
import org.block.project.legacypanel.inproject.Toolbar;

import java.io.File;

public class OpenBlockProject implements Project.Loaded {

    private File file;
    private MainDisplayPanel panel;
    private Toolbar toolbar;

    public OpenBlockProject(UnloadedProject project){
        this.file = project.getDirectory();
        init();
    }

    private void init(){
        this.panel = new MainDisplayPanel();
        this.toolbar = new Toolbar();
    }

    @Override
    public Module getModule() {
        return PluginContainer.OPEN_BLOCK_CONTAINER.getModules().iterator().next();
    }

    @Override
    public MainDisplayPanel getPanel() {
        return this.panel;
    }

    @Override
    public Toolbar getToolbar() {
        return this.toolbar;
    }

    @Override
    public File getDirectory() {
        return this.file;
    }
}
