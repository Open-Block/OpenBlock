package org.block.project.panel.scene;

import javafx.scene.Scene;
import org.block.project.panel.SceneSource;

public interface SourceBuilder {

    SceneSource build();

    static ProjectPanelBuilder projectPanel(){
        return new ProjectPanelBuilder();
    }
}
