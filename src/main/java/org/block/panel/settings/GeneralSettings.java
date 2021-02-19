package org.block.panel.settings;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.block.Blocks;
import org.block.panel.SceneSource;

import java.util.*;
import java.util.stream.Collectors;

public class GeneralSettings implements SceneSource {

    private final Scene goBackToScene;
    private final SceneSource goBackToSource;
    private final boolean useProjectSpecific;

    private final GridPane display = new GridPane();
    private final ToolBar settingSections = createSettingSections();
    private final Button backButton = createBackButton();

    public GeneralSettings(boolean useProjectSpecific){
        this(Blocks.getInstance().getFXWindow().getScene(), Blocks.getInstance().getSceneSource(), useProjectSpecific);
    }

    public GeneralSettings(Scene goBackToScene, SceneSource source, boolean useProjectSpecific){
        this.goBackToScene = goBackToScene;
        this.goBackToSource = source;
        this.useProjectSpecific = useProjectSpecific;
    }

    private Button createBackButton() {
        var button = new Button("Button");
        button.setOnAction(e -> {
            Blocks.getInstance().getFXWindow().setScene(this.goBackToScene);
            Blocks.getInstance().setSceneSource(this.goBackToSource);
        });
        return button;
    }

    private ToolBar createSettingSections() {
        var bar = new ToolBar();
        var background = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));
        bar.setBackground(background);
        Set<SettingsSector> settings = new HashSet<>(Settings.GENERAL_SETTINGS);
        if(this.useProjectSpecific){
            settings.addAll(Settings.PROJECT_SETTINGS);
        }
        var grid = GeneralSettings.this.display;
        grid.setBackground(background);
        grid.setGridLinesVisible(false);
        grid.getColumnConstraints().clear();
        grid.getColumnConstraints().add(new ColumnConstraints());
        var constraints = new ColumnConstraints();
        constraints.setFillWidth(true);
        constraints.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(constraints);
        List<Node> names = settings.parallelStream().map(s -> {
            Button button = new Button(s.getName());
            button.setOnAction(e -> {
                grid.getChildren().clear();
                s.getSettings().forEach((t, v) -> grid.addRow(grid.getRowCount(), t, v));
            });
            return button;
        }).sorted((b1, b2) -> b1.getText().compareToIgnoreCase(b2.getText())).collect(Collectors.toList());
        bar.getItems().addAll(names);
        bar.setOrientation(Orientation.VERTICAL);
        return bar;
    }


    @Override
    public Scene build() {
        var vbox = new VBox(this.backButton, this.settingSections);
        VBox.setVgrow(this.settingSections, Priority.ALWAYS);
        var hbox = new HBox(vbox, this.display);
        hbox.setSpacing(5.0);
        HBox.setHgrow(this.display, Priority.ALWAYS);
        return new Scene(hbox);
    }
}
