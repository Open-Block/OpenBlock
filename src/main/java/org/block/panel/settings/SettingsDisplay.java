package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.block.panel.common.dialog.Dialog;

public class SettingsDisplay<N extends Node> extends VBox implements Dialog {

    private final Parent origin;
    private final N display;

    public SettingsDisplay(N display, Parent origin) {
        this.display = display;
        this.origin = origin;
        VBox.setVgrow(this.display, Priority.ALWAYS);
        this.getChildren().addAll(this.display, this.origin);
    }

    public N getDisplay() {
        return this.display;
    }

    @Override
    public Parent getBackParent() {
        return this.origin;
    }
}
