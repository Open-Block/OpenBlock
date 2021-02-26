package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.common.dialog.Dialog;

import java.io.IOException;

public class SettingsDisplay<N extends Node & Settings> extends VBox implements Dialog {

    private final String origin;
    private final N display;

    public SettingsDisplay(N display, String origin) {
        this.display = display;
        this.origin = origin;
        this.init();
    }

    public N getDisplay() {
        return this.display;
    }

    @Override
    public String getBackWindow() {
        return this.origin;
    }

    private void init() {
        VBox.setVgrow(this.display, Priority.ALWAYS);
        var button = new Button("Back");
        button.setOnAction(e -> {
            try {
                this.display.save();
            } catch (IOException ioException) {
                throw new IllegalStateException(ioException);
            }
            Blocks.getInstance().setWindow(this.origin);
        });
        this.getChildren().addAll(this.display, button);
    }
}
