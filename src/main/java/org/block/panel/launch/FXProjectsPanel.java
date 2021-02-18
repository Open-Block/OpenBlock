package org.block.panel.launch;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.block.Blocks;
import org.block.panel.SceneSource;
import org.block.panel.common.navigation.NavigationBar;
import org.block.panel.common.navigation.NavigationOption;
import org.block.panel.common.navigation.TabbedNavigationBar;
import org.block.panel.main.FXMainDisplay;
import org.block.plugin.PluginContainer;
import org.block.project.module.Module;
import org.block.project.module.project.UnloadedProject;
import org.block.util.GeneralUntil;
import org.block.util.ToStringWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FXProjectsPanel implements SceneSource {

    private final ListView<ToStringWrapper<UnloadedProject>> projectListView = new ListView<>();
    private final File projectsDirectory;
    private final SplitPane splitPane = new SplitPane();
    private final TabbedNavigationBar navBar = new TabbedNavigationBar();

    public FXProjectsPanel(@NotNull File projectsDirectory) {
        this.projectsDirectory = projectsDirectory;
    }

    public ListView<ToStringWrapper<UnloadedProject>> getProjectListView(){
        return this.projectListView;
    }

    public File getProjectDirectory(){
        return this.projectsDirectory;
    }

    public SplitPane getSplitPane(){
        return this.splitPane;
    }

    private void searchForProjects(){
        File[] files = FXProjectsPanel.this.projectsDirectory.listFiles(File::isDirectory);
        if (files == null) {
            return;
        }
        for (File folder : files) {
            File openBlockFile = new File(folder, "OpenBlocks.json");
            if (!openBlockFile.exists()) {
                System.err.println("Invalid folder found: " + openBlockFile.getAbsolutePath());
                continue;
            }
            ObservableList<ToStringWrapper<UnloadedProject>> projects = FXProjectsPanel.this.projectListView.getItems();
            if (projects.parallelStream().anyMatch(p -> p.getValue().getDirectory().equals(folder))) {
                continue;
            }
            projects.add(new ToStringWrapper<>(new UnloadedProject(folder), (p) -> {
                try {
                    return p.getDisplayName();
                } catch (IOException e) {
                    return p.getFile().getName();
                }
            }));
        }
    }

    private ListView<ToStringWrapper<UnloadedProject>> createProjectList() {
        new Thread(this::searchForProjects).start();
        this.projectListView.setOnMouseClicked((event) -> {
            ObservableList<Node> splitItems = this.splitPane.getItems();
            try {
                ToStringWrapper<UnloadedProject> wrapper = this.projectListView.getSelectionModel().getSelectedItem();
                if(wrapper == null){
                    return;
                }
                UnloadedProject project = wrapper.getValue();
                Module module = project.getExpectedModule();
                double[] pos = this.splitPane.getDividerPositions();
                splitItems.remove(1);
                Region info = module.createDisplayInfo(project);
                VBox wrapped = createProjectInfo(project, info);
                splitItems.add(wrapped);
                this.splitPane.setDividerPositions(pos);

                wrapped.prefWidthProperty().bind(this.projectListView.prefWidthProperty());
            } catch (IOException e) {
                e.printStackTrace();
                event.consume();
            }
        });
        this.projectListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return this.projectListView;
    }

    private Pane createBlankProjectInfo() {
        return new Pane();
    }

    private VBox createProjectInfo(UnloadedProject project, Region region){
        Button load = new Button("Load");
        load.setOnAction((event) -> {
            FXMainDisplay display = new FXMainDisplay();
            Blocks.getInstance().getFXWindow().setScene(display.build());
            Blocks.getInstance().setSceneSource(display);
        });
        Button delete = new Button("Delete");
        delete.setOnAction((event) -> {
            GeneralUntil.getFiles(project.getDirectory()).parallelStream().forEach(File::delete);

            Optional<ToStringWrapper<UnloadedProject>> opWrapper = this.projectListView.getItems().parallelStream().filter(w -> w.getValue().equals(project)).findFirst();
            if(!opWrapper.isPresent()){
                return;
            }
            this.projectListView.getItems().remove(opWrapper.get());
        });
        HBox box = new HBox(delete, load);

        VBox area = new VBox(region, box);
        VBox.setVgrow(region, Priority.ALWAYS);
        area.setFillWidth(true);

        load.prefWidthProperty().bind(area.widthProperty());
        delete.prefWidthProperty().bind(area.widthProperty());

        region.prefWidthProperty().bind(area.widthProperty());
        region.prefHeightProperty().bind(area.heightProperty());
        return area;
    }

    private TabbedNavigationBar createNavBar() {
        List<NavigationOption> createMenuNavigation = new ArrayList<>();
        Blocks.getInstance().getAllEnabledPlugins().getAll(PluginContainer::getModules).parallelStream().forEach(m -> {
            NavigationOption item = new NavigationOption(m.getDisplayName());
            item.setOnAction(e -> m.onProjectCreator());
            createMenuNavigation.add(item);
        });
        createMenuNavigation.sort(Comparator.comparing(n -> n.getText().getText()));
        NavigationBar createMenu = new NavigationBar();
        createMenu.getChildren().addAll(createMenuNavigation);
        this.navBar.getTabs().add(new Tab("Create", createMenu));

        NavigationOption joinNetworkOption = new NavigationOption("Join");
        //TODO JOIN
        NavigationBar networkBar = new NavigationBar(joinNetworkOption);

        this.navBar.getTabs().add(new Tab("Network", networkBar));

        return this.navBar;
    }

    @Override
    public Scene build() {
        TabbedNavigationBar navBar = this.createNavBar();
        ListView<ToStringWrapper<UnloadedProject>> projects = this.createProjectList();
        Pane blankProjectPane = new Pane();
        this.splitPane.getItems().add(projects);
        this.splitPane.getItems().add(blankProjectPane);
        this.splitPane.setDividerPosition(0, this.splitPane.getDividerPositions()[0] / 2);
        VBox box = new VBox(navBar, this.splitPane);
        VBox.setVgrow(this.splitPane, Priority.ALWAYS);
        return new Scene(box);
    }
}
