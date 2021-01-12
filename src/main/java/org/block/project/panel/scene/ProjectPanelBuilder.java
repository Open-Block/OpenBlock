package org.block.project.panel.scene;

import org.block.project.panel.launch.FXProjectsPanel;

import java.io.File;

public class ProjectPanelBuilder implements SourceBuilder {

    private File projectPath = new File("Projects");

    public File getProjectPath(){
        return this.projectPath;
    }

    public void setProjectPath(File file){
        this.projectPath = file;
    }

    @Override
    public FXProjectsPanel build() {
        return new FXProjectsPanel(this.projectPath);
    }
}
