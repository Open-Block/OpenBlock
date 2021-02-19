package org.block.panel.settings;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.SceneSource;

import java.util.*;
import java.util.stream.Collectors;

public class GeneralSettings implements SceneSource {

    private Scene goBackToScene;
    private SceneSource goBackToSource;
    private boolean useProjectSpecific;

    private ToolBar settingSections = createSettingSections();
    private Button backButton = createBackButton();


    public static final Set<SettingsSector> GENERAL_SETTINGS;
    public static final Set<SettingsSector> PROJECT_SETTINGS;


    static {
        GENERAL_SETTINGS = new HashSet<>();
        PROJECT_SETTINGS = new HashSet<>();
    }

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
        ToolBar bar = new ToolBar();
        Set<SettingsSector> settings = new HashSet<>(GENERAL_SETTINGS);
        if(this.useProjectSpecific){
            settings.addAll(PROJECT_SETTINGS);
        }
        List<Node> names = settings.parallelStream().map(s -> {
            Button button = new Button(s.getName());

            return button;
        }).sorted((b1, b2) -> b1.getText().compareToIgnoreCase(b2.getText())).collect(Collectors.toList());
        bar.getItems().addAll(names);
        bar.setOrientation(Orientation.VERTICAL);
        return bar;
    }


    @Override
    public Scene build() {
        Scene scene = new Scene(new VBox(createBackButton(), createSettingSections()));
        return scene;
    }
}
