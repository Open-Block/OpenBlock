package org.block.panel.common.dialog;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.panel.settings.GeneralSettings;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileViewer extends HBox implements Dialog {

    private String backWindow;
    private File startingFile;
    private File path;

    private Consumer<File> selected;
    private boolean acceptFolders;
    private FileFilter filter;
    private TextField fileName;
    private Button onAction ;
    private ComboBox<String> fileType;
    private VBox commonButtons;

    public FileViewer() {
        this.reset();

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
            return box;
        }
        Label label = new Label(file.getName());
        return new HBox(label);
    }

    private VBox createCommonButtons() {
        var home = new Button("Home");
        home.setOnAction((e) -> this.path = new File(System.getProperty("user.home")));
        var publicStorage = new Button("Public");
        publicStorage.setOnAction((e) -> this.path = GeneralSettings.ROOT_PUBLIC_PATH.get());
        var privateStorage = new Button("Private");
        privateStorage.setOnAction((e) -> this.path = GeneralSettings.ROOT_PRIVATE_PATH.get());

        var array = new Region[]{new Label("Common"), home, publicStorage, privateStorage};
        var vBox = new VBox(array);
        for(var button : Stream.of(array).filter(b -> b instanceof Button).map(b -> (Button)b).collect(Collectors.toSet())){
           button.minWidthProperty().bind(vBox.widthProperty());
        }
        return vBox;
    }

    private HBox createSaveButtons(){
        var box = new HBox(this.fileName, this.fileType, this.onAction);
        HBox.setHgrow(this.fileName, Priority.ALWAYS);
        return box;
    }

    private ScrollPane createView(File target){
        var rawFiles = target.list();
        if(rawFiles == null){
            return new ScrollPane(new Label("No files"));
        }
        var files = Stream.of(rawFiles)
                .map(File::new)
                .filter(f -> f.isDirectory() || this.filter.accept(f))
                .map(this::getFileButton)
                .toArray(HBox[]::new);
        if(files.length == 0){
            return new ScrollPane(new Label("No files"));
        }
        var show = new ListView<Node>();
        show.getItems().addAll(files);
        return new ScrollPane(show);
    }

    public void updateGraphic() {
        File target = this.path == null ? this.startingFile : this.path;
        var currentPath = new Label(target.getPath());
        if(target.isFile()){
            currentPath = new Label(target.getParentFile().getPath());
        }
        var listView = this.createView(target);
        var saveBox = this.createSaveButtons();
        VBox.setVgrow(listView, Priority.ALWAYS);
        var fileView = new VBox(currentPath, listView, saveBox);
        HBox.setHgrow(fileView, Priority.ALWAYS);
        this.getChildren().clear();
        this.getChildren().addAll(this.commonButtons, fileView);
    }

    public void reset(){
        this.acceptFolders = false;
        this.removeFilter();
        this.fileName = new TextField();
        this.onAction = new Button("Open");
        this.fileType = new ComboBox<>();
        this.commonButtons = createCommonButtons();
        this.startingFile = GeneralSettings.ROOT_PUBLIC_PATH.get();
    }

    public File getPath() {
        return path;
    }

    public Consumer<File> getSelected() {
        return selected;
    }

    public FileViewer setSelected(Consumer<File> selected) {
        this.selected = selected;
        return this;
    }

    public boolean isAcceptFolders() {
        return acceptFolders;
    }

    public FileViewer setAcceptFolders(boolean acceptFolders) {
        this.acceptFolders = acceptFolders;
        return this;
    }

    public TextField getFileName() {
        return fileName;
    }

    public FileViewer setFileName(TextField fileName) {
        this.fileName = fileName;
        return this;
    }

    public Button getOnAction() {
        return onAction;
    }

    public FileViewer setOnAction(Button onAction) {
        this.onAction = onAction;
        return this;
    }

    public ComboBox<String> getFileType() {
        return fileType;
    }

    public FileViewer setFileType(ComboBox<String> fileType) {
        this.fileType = fileType;
        return this;
    }

    public VBox getCommonButtons() {
        return commonButtons;
    }

    public FileViewer setCommonButtons(VBox commonButtons) {
        this.commonButtons = commonButtons;
        return this;
    }

    public Consumer<File> getOnSelect(){
        return this.selected;
    }

    public void setOnSelect(Consumer<File> consumer) {
        this.selected = consumer;
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
