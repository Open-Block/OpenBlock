package org.block;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopBlocks extends Blocks {

    private final Stage stage;

    public DesktopBlocks(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Parent getWindow() {
        return this.stage.getScene().getRoot();
    }

    @Override
    public void setWindow(Parent parent) {
        var scene = parent.getScene();
        if (scene == null) {
            scene = new Scene(parent);
        }
        this.stage.setScene(scene);
    }
}
