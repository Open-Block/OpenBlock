package org.block.panel.scene;

import org.block.panel.SceneSource;

public interface SourceBuilder {

    SceneSource build();

    static ProjectPanelBuilder projectPanel(){
        return new ProjectPanelBuilder();
    }
}
