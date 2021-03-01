package org.block.panel.common.dialog;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.glisten.control.ExceptionDialog;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.block.Blocks;
import org.block.panel.settings.GeneralSettings;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileViewer extends HBox implements Dialog {

    private String backWindow;
    private File startingFile;
    private File path;

    private Consumer<File> selected;
    private boolean acceptFolders;
    private boolean saveMode;
    private FileFilter filter;
    private TextField fileName;
    private Button onAction;
    private Button cancel;
    private ComboBox<String> fileType;
    private VBox commonButtons;
    private VBox commonActionButtons;
    private ListView<File> listView;

    public FileViewer() {
        this.reset();

        this.updateGraphic();
    }

    public void updateGraphic() {
        File target = this.path == null ? this.startingFile : this.path;
        var currentPath = new Label(target.getPath());
        if (target.isFile()) {
            currentPath = new Label(target.getParentFile().getPath());
        }
        currentPath.setMaxWidth(Double.MAX_VALUE);
        currentPath.setAlignment(Pos.CENTER);
        var listView = this.createView(target);
        var scrollListView = new ScrollPane(listView);
        listView.minWidthProperty().bind(scrollListView.widthProperty().subtract(15));

        var saveBox = this.createSaveButtons();
        var newFolderBox = this.createNewFolder();
        VBox.setVgrow(scrollListView, Priority.ALWAYS);
        newFolderBox.setMaxWidth(Double.MAX_VALUE);
        var fileView = new VBox(currentPath, newFolderBox, scrollListView, saveBox);
        HBox.setHgrow(fileView, Priority.ALWAYS);

        var buttons = new VBox(this.commonActionButtons, this.commonButtons);
        this.getChildren().clear();
        this.getChildren().addAll(buttons, fileView);
    }

    public void reset() {
        this.acceptFolders = false;
        this.saveMode = false;
        this.removeFilter();
        this.fileName = new TextField();
        this.onAction = new Button("Open");
        this.cancel = new Button("Cancel");
        this.fileType = new ComboBox<>();
        this.commonButtons = this.createCommonLocationsButtons();
        this.commonActionButtons = this.createCommonActionButtons();
        this.startingFile = GeneralSettings.ROOT_PUBLIC_PATH.get();
        this.cancel.setOnAction(e -> Blocks.getInstance().setWindow(this.backWindow));
        this.onAction.setOnAction(e -> {
            File file = this.listView.getSelectionModel().getSelectedItem();
            if (file == null) {
                return;
            }
            if (!this.acceptFolders && file.isDirectory()) {
                this.path = file;
                this.updateGraphic();
                return;
            }
            this.selected.accept(file);
            Blocks.getInstance().setWindow(this.backWindow);
        });
        this.listView = new ListView<>();
        this.listView.setCellFactory(lv -> new ListCell<>() {

            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                this.setText(null);
                this.setGraphic(null);
                if (empty) {
                    return;
                }
                this.setGraphic(FileViewer.this.createFileButton(file));
                this.setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        FileViewer.this.onAction.fire();
                    }
                });
            }
        });
    }

    public File getPath() {
        return this.path;
    }

    public Consumer<File> getSelected() {
        return this.selected;
    }

    public FileViewer setSelected(Consumer<File> selected) {
        this.selected = selected;
        return this;
    }

    public boolean isAcceptFolders() {
        return this.acceptFolders;
    }

    public FileViewer setAcceptFolders(boolean acceptFolders) {
        this.acceptFolders = acceptFolders;
        return this;
    }

    public TextField getFileName() {
        return this.fileName;
    }

    public FileViewer setFileName(TextField fileName) {
        this.fileName = fileName;
        return this;
    }

    public Button getOnAction() {
        return this.onAction;
    }

    public FileViewer setOnAction(Button onAction) {
        this.onAction = onAction;
        return this;
    }

    public ComboBox<String> getFileType() {
        return this.fileType;
    }

    public FileViewer setFileType(ComboBox<String> fileType) {
        this.fileType = fileType;
        return this;
    }

    public VBox getCommonButtons() {
        return this.commonButtons;
    }

    public FileViewer setCommonButtons(VBox commonButtons) {
        this.commonButtons = commonButtons;
        return this;
    }

    public Consumer<File> getOnSelect() {
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

    private HBox createNewFolder() {
        var nameFolder = new TextField();
        var createButton = new Button("new folder");
        createButton.setOnAction(e -> {
            File file = new File(this.path, nameFolder.getText());
            try {
                Files.createDirectories(file.toPath());
                this.updateGraphic();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                var dialog = new ExceptionDialog();
                dialog.setException(ioException);
                dialog.showAndWait();
            }
        });
        var box = new HBox(nameFolder, createButton);
        VBox.setVgrow(nameFolder, Priority.ALWAYS);
        return box;
    }

    private HBox createFileButton(File file) {
        if (file.isFile()) {
            Node image = MaterialDesignIcon.ADD_LOCATION.graphic();
            if (!Platform.isAndroid()) {
                image = new Region();
            }
            Label label = new Label(file.getName());
            HBox box = new HBox(image, label);
            HBox.setHgrow(label, Priority.ALWAYS);
            return box;
        }
        Label label = new Label(file.getName());
        return new HBox(label);
    }

    private VBox createCommonLocationsButtons() {
        var home = new Button("Home");
        home.setOnAction((e) -> {
            this.path = new File(System.getProperty("user.home"));
            this.updateGraphic();
        });
        var publicStorage = new Button("Public");
        publicStorage.setOnAction((e) -> {
            this.path = GeneralSettings.ROOT_PUBLIC_PATH.get();
            this.updateGraphic();
        });
        var privateStorage = new Button("Private");
        privateStorage.setOnAction((e) -> {
            this.path = GeneralSettings.ROOT_PRIVATE_PATH.get();
            this.updateGraphic();
        });

        var array = new Region[]{new Label("Common"), home, publicStorage, privateStorage};
        var vBox = new VBox(array);
        for (var button : Stream.of(array).filter(b -> b instanceof Button).map(b -> (Button) b).collect(Collectors.toSet())) {
            button.minWidthProperty().bind(vBox.widthProperty());
        }
        return vBox;
    }

    private VBox createCommonActionButtons() {
        var parentButton = new Button("Parent");
        parentButton.setOnAction(e -> {
            if (this.path == null) {
                this.path = this.startingFile.getParentFile();
            } else {
                this.path = this.path.getParentFile();
            }
            this.updateGraphic();
        });
        var vBox = new VBox(parentButton);
        parentButton.minWidthProperty().bind(vBox.widthProperty());

        return vBox;
    }

    private HBox createSaveButtons() {
        Node cancel = this.cancel;
        Node fileType = this.fileType;
        if (this.backWindow == null || Blocks.getInstance() == null) {
            cancel = new Region();
        }
        if (!this.saveMode || this.fileType.getItems().isEmpty()) {
            fileType = new Region();
        }
        var box = new HBox(cancel, this.fileName, fileType, this.onAction);
        HBox.setHgrow(this.fileName, Priority.ALWAYS);
        return box;
    }

    private Region createView(File target) {
        var rawFiles = target.list();
        if (rawFiles == null) {
            return new Label("No files");
        }
        var files = Stream.of(rawFiles)
                .map(File::new)
                .filter(f -> f.isDirectory() || this.filter.accept(f))
                .toArray(File[]::new);
        if (files.length == 0) {
            return new Label("No files");
        }
        this.listView.getItems().clear();
        this.listView.getItems().addAll(files);
        return this.listView;
    }
}
