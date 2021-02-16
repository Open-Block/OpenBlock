package org.block.plugin.standard;

import org.block.plugin.PluginContainer;
import org.block.project.module.Module;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;

import java.io.File;

public class OpenBlockProject implements Project.Loaded {

    private File file;

    public OpenBlockProject(UnloadedProject project){
        this.file = project.getDirectory();
        init();
    }

    private void init(){
    }

    @Override
    public Module getModule() {
        return PluginContainer.OPEN_BLOCK_CONTAINER.getModules().iterator().next();
    }


    @Override
    public File getDirectory() {
        return this.file;
    }
}
