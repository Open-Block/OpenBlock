package org.block.panel.plugin;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.common.dialog.Dialog;
import org.block.plugin.Plugin;

public class PluginCreator extends VBox implements Dialog {

    private Plugin plugin;
    private TextField name;
    private TextField version;
    private Label path;
    private Button cancel;
    private Button submit;

    public PluginCreator() {
        this.reset();
        this.updateGraphics();

    }

    public PluginCreator setPlugin(Plugin plugin) {
        this.plugin = plugin;
        this.reset();
        this.updateGraphics();
        return this;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getBackWindow() {
        return Blocks.getInstance().LAUNCH_WINDOW;
    }

    public void updateGraphics() {
        this.getChildren().clear();
        var nameLabel = new Label("Name");
        var versionLabel = new Label("Version");
        var pathLabel = new Label("Path");
        var nameBox = new HBox(nameLabel, this.name);
        var versionBox = new HBox(versionLabel, this.version);
        var pathBox = new HBox(pathLabel, this.path);
        var inputBox = new VBox(nameBox, versionBox, pathBox);

        var inputs = new Region[]{nameLabel, pathLabel};
        for (var region : inputs) {
            region.minWidthProperty().bind(versionLabel.widthProperty());
        }

        var buttonBox = new HBox(this.cancel, this.submit);
        buttonBox.setPrefWidth(Double.MAX_VALUE);
        buttonBox.setPrefHeight(Double.MAX_VALUE);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(this.name, Priority.ALWAYS);
        HBox.setHgrow(this.version, Priority.ALWAYS);
        HBox.setHgrow(this.path, Priority.ALWAYS);
        this.getChildren().addAll(inputBox, buttonBox);
    }

    public void reset() {
        this.name = new TextField();
        this.cancel = new Button("Cancel");
        this.path = new Label();
        this.version = new TextField();
        this.submit = new Button("Create");
        this.submit.setDefaultButton(true);
        this.cancel.setCancelButton(true);
        this.path.setDisable(true);
        this.name.setOnKeyTyped(e -> {
            this.path.setText(Blocks
                    .getInstance()
                    .getSettings()
                    .getProjectPath()
                    .getValue()
                    .getPath()
                    + "/" + this.name.getText() + "/project.json");
            this.submit.setDisable(!(this.name.getText().isEmpty() && this.version.getText().isEmpty()));
        });
    }
}
