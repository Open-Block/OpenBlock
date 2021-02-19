package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LogFileSettings implements SettingsSector {

    private HBox createPathNode(){
        var textField = new TextField();
        var searchButton = new Button("...");
        var box = new HBox(textField, searchButton);
        HBox.setHgrow(textField, Priority.ALWAYS);

        searchButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Simple.txt");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", ".txt"), new FileChooser.ExtensionFilter("Log", ".log"));
            File file = fileChooser.showSaveDialog(Blocks.getInstance().getFXWindow());
            if(file == null){
                return;
            }
            try {
                Files.createDirectories(file.getParentFile().toPath());
                Files.createFile(file.toPath());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                //textField.setBorder(new Border());
                return;
            }
            textField.setText(file.getPath());

        });

        return box;
    }


    @Override
    public String getName() {
        return "Log";
    }

    @Override
    public Map<Text, Node> getSettings() {
        Map<Text, Node> map = new HashMap<>();
        map.put(new Text("Simple"), createPathNode());
        return map;
    }
}
