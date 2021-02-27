package org.block.panel.common.dialog;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.settings.GeneralSettings;

import java.io.File;
import java.io.FileFilter;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileViewer extends HBox implements Dialog {

    private String backWindow;
    private File startingFile;
    private File path;
    private FileFilter filter;
    private Consumer<File> onSelect;

    private final VBox commonButtons = createCommonButtons();

    public FileViewer(File startingFile, Consumer<File> consumer) {
        this.startingFile = startingFile;
        this.onSelect = consumer;
        this.removeFilter();
        updateGraphic();
    }

    private HBox getFileButton(File file) {
        if (file.isFile()) {
            Node image = MaterialDesignIcon.ADD_LOCATION.graphic();
            if (!Platform.isAndroid()) {
                image = new Region();
            }
            Label label = new Label(file.getName());
            HBox box = new HBox(image, label);
            box.setOnMouseClicked(e -> this.onSelect.accept(file));
            return box;
        }
        return new HBox();
    }

    private VBox createCommonButtons() {
        var home = new Button("Home");
        home.setOnAction((e) -> this.path = new File(System.getProperty("user.home")));
        var publicStorage = new Button("Public");
        publicStorage.setOnAction((e) -> this.path = GeneralSettings.ROOT_PUBLIC_PATH.get());
        var privateStorage = new Button("Private");
        privateStorage.setOnAction((e) -> this.path = GeneralSettings.ROOT_PRIVATE_PATH.get());
        return new VBox(home, publicStorage, privateStorage);
    }

    public void updateGraphic() {
        File target = this.path == null ? this.startingFile : this.path;
        var rawFiles = target.list();
        VBox show = new VBox();
        if (rawFiles != null) {
            var files = Stream.of(rawFiles)
                    .map(File::new)
                    .filter(f -> this.filter.accept(f))
                    .map(this::getFileButton)
                    .toArray(HBox[]::new);
            show = new VBox(files);
        }
        this.getChildren().clear();
        this.getChildren().addAll(this.commonButtons, new ScrollPane(show));
    }

    public void setOnSelect(Consumer<File> consumer) {
        this.onSelect = consumer;
    }

    public File getStartingFile() {
        return this.startingFile;
    }

    public void setStartingFile(File file) {
        this.startingFile = file;
    }

    public FileFilter getFilter() {
        return this.filter;
    }

    public void removeFilter() {
        this.setFilter((f) -> true);
    }

    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }


    public void setBackWindow(String window) {
        this.backWindow = window;
    }

    @Override
    public String getBackWindow() {
        return this.backWindow;
    }
}
