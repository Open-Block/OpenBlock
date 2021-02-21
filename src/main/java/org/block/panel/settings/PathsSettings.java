package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.gluonhq.attach.util.Services;
import com.gluonhq.attach.storage.StorageService;

public class PathsSettings implements SettingsSector {

    public final TextField projectPath = new TextField(Services.get(StorageService.class).flatMap(storage -> storage.getPrivateStorage().map(f -> new File(f, "/projects"))).orElse(new File("projects")).getAbsolutePath());
    public final TextField logPath = new TextField("logs/simple.txt");
    public final TextField debugPath = new TextField("logs/debug.text");

    private HBox createPathNode(TextField textField){
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
        return "Path";
    }

    @Override
    public Map<Text, Node> getSettings() {
        Map<Text, Node> map = new HashMap<>();
        map.put(new Text("Log"), createPathNode(this.logPath));
        map.put(new Text("Debug"), createPathNode(this.debugPath));
        map.put(new Text("Project"), createPathNode(this.projectPath));

        return map;
    }
}
