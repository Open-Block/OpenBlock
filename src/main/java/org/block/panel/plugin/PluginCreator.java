package org.block.panel.plugin;

import com.gluonhq.charm.glisten.control.ExceptionDialog;
import com.gluonhq.charm.glisten.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.common.dialog.Dialog;
import org.block.panel.launch.ProjectsPanel;
import org.block.plugin.Plugin;
import org.block.project.UnloadedProject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    public PluginCreator setPlugin(@NotNull Plugin plugin) {
        this.reset();
        this.updateGraphics();
        this.plugin = plugin;
        System.out.println("Set plugin as " + this.plugin);
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
        this.name.addEventHandler(KeyEvent.KEY_TYPED, e -> {
            this.path.setText(Blocks
                    .getInstance()
                    .getSettings()
                    .getProjectPath()
                    .getValue()
                    .getPath()
                    + "/" + this.name.getText() + "/OpenBlocks.json");
            this.submit.setDisable(!(this.name.getText().isEmpty() && this.version.getText().isEmpty()));
        });
        this.version.addEventHandler(KeyEvent.KEY_TYPED, e -> {
            this.submit.setDisable(!(this.name.getText().isEmpty() && this.version.getText().isEmpty()));
        });
        this.cancel.setOnAction(e -> Blocks.getInstance().setWindow(this.getBackWindow()));
        this.submit.setOnAction(e -> {
            if (this.name.getText().isEmpty() && this.version.getText().isEmpty()) {
                return;
            }
            var projectFilePath = new File(Blocks
                    .getInstance()
                    .getSettings()
                    .getProjectPath()
                    .getValue()
                    .getPath()
                    + "/" + this.name.getText() + "/OpenBlocks.json");
            var projectFolder = projectFilePath.getParentFile();
            try {
                Files.createDirectories(projectFolder.toPath());
                Files.createFile(projectFilePath.toPath());
                var unloadedProject = new UnloadedProject(projectFolder);
                unloadedProject.setTempPlugin(this.plugin);
                System.out.println("Plugin: " + this.plugin);
                unloadedProject.setTempName(this.name.getText());
                unloadedProject.addTempVersions(this.version.getText());
                unloadedProject.saveTempData();

            } catch (IOException ioException) {
                ioException.printStackTrace();
                var dialog = new ExceptionDialog();
                dialog.setException(ioException);
                return;
            }
            Blocks.getInstance().setWindow(this.getBackWindow());
            var window = Blocks.getInstance().getWindow();
            if (window instanceof ProjectsPanel) {
                var panel = ((ProjectsPanel) window);
                panel.searchForProjects();
            }
        });
    }
}
