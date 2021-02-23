package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.common.dialog.Dialog;

public class SettingsDisplay<N extends Node> extends VBox implements Dialog {

    private final Parent origin;
    private final N display;

    public SettingsDisplay(N display, Parent origin) {
        this.display = display;
        this.origin = origin;
        this.init();
    }

    public N getDisplay() {
        return this.display;
    }

    @Override
    public Parent getBackParent() {
        return this.origin;
    }

    private void init() {
        VBox.setVgrow(this.display, Priority.ALWAYS);
        var button = new Button("Back");
        button.setOnAction(e -> {
            Blocks.getInstance().setWindow(this.origin);
        });
        this.getChildren().addAll(this.display, button);
    }
}
