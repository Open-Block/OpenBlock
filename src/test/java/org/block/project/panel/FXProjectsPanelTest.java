package org.block.project.panel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.project.panel.scene.SourceBuilder;

public class FXProjectsPanelTest extends Application {
    @Override
    public void start(Stage stage) {
        Blocks.setInstance(new Blocks());
        stage.setTitle("Test");
        SceneSource sceneSource = SourceBuilder.projectPanel().build();
        Blocks.getInstance().setSceneSource(sceneSource);
        Blocks.getInstance().setFXWindow(stage);
        stage.setScene(sceneSource.build());
        stage.setWidth(500);
        stage.setHeight(500);
        stage.show();
    }
}
