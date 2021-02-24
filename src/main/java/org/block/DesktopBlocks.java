package org.block;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.block.panel.common.dialog.Dialog;

import java.util.HashMap;
import java.util.Map;

public class DesktopBlocks extends Blocks {

    private final Map<String, Scene> views = new HashMap<>();
    private final Stage stage;

    public DesktopBlocks(Stage stage, String main) {
        super(main);
        this.stage = stage;
        this.init();
    }

    @Override
    public Parent getWindow() {
        return this.stage.getScene().getRoot();
    }

    @Override
    public void setWindow(String title) {
        Scene scene = this.views.get(title);
        if (scene == null) {
            throw new IllegalStateException("The window of '" + title + "' has not been registered");
        }
        this.stage.setScene(scene);
    }

    @Override
    public void registerWindow(String title, Parent parent) {
        this.views.put(title, new Scene(parent));
    }

    @Override
    public void requestNewWidth(double width) {
        this.stage.setWidth(width);
    }

    @Override
    public void requestNewHeight(double height) {
        this.stage.setHeight(height);
    }

    @Override
    public double getHeight() {
        return this.stage.getHeight();
    }

    @Override
    public double getWidth() {
        return this.stage.getWidth();
    }

    protected Scene onSceneCreate(Scene scene, Parent parent) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (KeyCode.ESCAPE.equals(e.getCode())) {
                if (parent instanceof Dialog) {
                    this.setWindow(((Dialog) parent).getBackWindow());
                }
            }
        });
        return scene;
    }
}
