package org.block.plugin.standard.panel;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.block.Blocks;
import org.block.panel.common.dialog.Dialog;
import org.block.panel.launch.ProjectsPanel;
import org.block.plugin.PluginContainer;
import org.block.project.module.project.UnloadedProject;
import org.block.util.ToStringWrapper;

import java.io.File;
import java.io.IOException;


public class FXOpenBlockNewPanel extends VBox implements Dialog {

    private final TextField nameField = new TextField();
    private final TextField versionField = new TextField("1.0.0-SNAPSHOT");
    private final Label locationLabel = new Label(new File(Blocks.getInstance().getSettings().getProjectPath().valueProperty().getValue().getAbsolutePath() + "/OpenBlock.json").getAbsolutePath());
    private final Button cancelButton = new Button("Cancel");
    private final Button createButton = new Button("Create");
    private final ProjectsPanel origin;

    public FXOpenBlockNewPanel(ProjectsPanel panel) {
        this.origin = panel;
        this.init();
    }

    @Override
    public Parent getBackParent() {
        return this.origin;
    }

    private void init() {
        var grid = this.createGrid();
        var buttons = this.createButtons();
        grid.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.getChildren().addAll(grid, buttons);
    }

    private GridPane createGrid() {
        GridPane pane = new GridPane();
        this.createRow(pane, "Name", this.nameField, 0);
        this.createRow(pane, "Version", this.versionField, 1);
        this.createRow(pane, "Location", this.locationLabel, 2);
        return pane;
    }

    private void createRow(GridPane pane, String key, Region node, int row) {
        Label keyL = new Label(key);
        keyL.setPadding(new Insets(0, 10, 0, 10));
        pane.add(keyL, 0, row);
        pane.add(node, 1, row);
        node.prefWidthProperty().bind(pane.widthProperty().subtract(keyL.widthProperty().add(20)));
        GridPane.setVgrow(node, Priority.ALWAYS);
    }

    private HBox createButtons() {
        this.nameField.setOnKeyTyped((e) -> this.createKeyEvent(e, true));
        this.versionField.setOnKeyTyped((e) -> this.createKeyEvent(e, false));
        this.cancelButton.setCancelButton(true);

        this.createButton.setOnAction((event) -> {
            String path = this.locationLabel.getText();
            UnloadedProject project = new UnloadedProject(new File(path.substring(0, path.length() - "OpenBlocks.json".length())));
            project.setTempModule(PluginContainer.OPEN_BLOCK_MODULE);
            project.setTempName(this.nameField.getText());
            project.addTempVersions(this.versionField.getText());
            try {
                project.saveTempData();
                Blocks.getInstance().setWindow(this.origin);
                this.origin.getProjectListView().getItems().add(new ToStringWrapper<>(project, (pro) -> {
                    try {
                        return pro.getDisplayName();
                    } catch (IOException e) {
                        return pro.getDirectory().getName();
                    }
                }));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        this.cancelButton.setOnAction((event) -> {
            Blocks.getInstance().setWindow(this.origin);
        });

        HBox box = new HBox(this.cancelButton, this.createButton);
        box.setFillHeight(true);

        this.cancelButton.prefWidthProperty().bind(box.widthProperty().divide(2));
        this.createButton.prefWidthProperty().bind(box.widthProperty().divide(2));
        this.createButton.setDisable(true);
        return box;
    }

    private void createKeyEvent(KeyEvent event, boolean checkName) {
        if (checkName) {
            this.locationLabel.setText(new File(Blocks.getInstance().getSettings().getProjectPath().valueProperty().getValue().getAbsolutePath() + "/" + this.nameField.getText() + "/OpenBlock.json").getAbsolutePath());
        }
        if (this.nameField.getText().trim().length() == 0) {
            this.createButton.setDisable(true);
            return;
        }
        if (this.versionField.getText().trim().length() == 0) {
            this.createButton.setDisable(true);
            return;
        }
        this.createButton.setDisable(false);
    }
}
