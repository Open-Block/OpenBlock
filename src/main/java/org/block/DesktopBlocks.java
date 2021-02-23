package org.block;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.block.panel.common.dialog.Dialog;

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
            scene = this.onSceneCreate(new Scene(parent), parent);
        }
        this.stage.setScene(scene);
    }

    protected Scene onSceneCreate(Scene scene, Parent parent) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (KeyCode.ESCAPE.equals(e.getCode())) {
                if (parent instanceof Dialog) {
                    this.setWindow(((Dialog) parent).getBackParent());
                }
            }
        });
        return scene;
    }
}
