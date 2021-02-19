package org.temp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.panel.SceneSource;
import org.block.panel.scene.SourceBuilder;
import org.util.print.ShinyOutputStream;

public class FXProjectsPanelTest extends Application {
    @Override
    public void start(Stage stage) {
        ShinyOutputStream.createDefault();
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

    public static void main(String[] args) {
        launch(args);
    }
}
