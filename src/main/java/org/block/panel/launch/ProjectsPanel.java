package org.block.panel.launch;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.block.Blocks;
import org.block.panel.common.navigation.NavigationBar;
import org.block.panel.common.navigation.NavigationItem;
import org.block.panel.main.FXMainDisplay;
import org.block.panel.settings.GeneralSettings;
import org.block.project.module.Module;
import org.block.project.module.project.UnloadedProject;
import org.block.util.GeneralUntil;
import org.block.util.ToStringWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectsPanel extends VBox {

    private final File projectsDirectory;
    private final SplitPane splitPane = new SplitPane();
    private final NavigationBar navBar;
    private final ListView<ToStringWrapper<UnloadedProject>> projectListView;

    public ProjectsPanel(@NotNull File projectsDirectory) {
        this.projectsDirectory = projectsDirectory;
        this.navBar = this.createNavBar();
        this.projectListView = this.createProjectList();
        this.init();
    }

    public ListView<ToStringWrapper<UnloadedProject>> getProjectListView() {
        return this.projectListView;
    }

    public File getProjectDirectory() {
        return this.projectsDirectory;
    }

    public SplitPane getSplitPane() {
        return this.splitPane;
    }

    private void init() {
        this.splitPane.getItems().add(this.projectListView);
        this.splitPane.getItems().add(new Pane());
        this.splitPane.setDividerPosition(0, this.splitPane.getDividerPositions()[0] / 2);
        VBox.setVgrow(this.splitPane, Priority.ALWAYS);
        this.getChildren().addAll(this.navBar, this.splitPane);
        new Thread(this::searchForProjects).start();

    }

    private void searchForProjects() {
        System.out.println("ProjectDir: " + this.projectsDirectory);
        File[] files = this.projectsDirectory.listFiles(File::isDirectory);
        if (files == null) {
            return;
        }
        for (File folder : files) {
            File openBlockFile = new File(folder, "OpenBlocks.json");
            if (!openBlockFile.exists()) {
                System.err.println("Invalid folder found: " + openBlockFile.getAbsolutePath());
                continue;
            }
            ObservableList<ToStringWrapper<UnloadedProject>> projects = this.projectListView.getItems();
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
        ListView<ToStringWrapper<UnloadedProject>> projectListView = new ListView<>();
        projectListView.setOnMouseClicked((event) -> {
            ObservableList<Node> splitItems = this.splitPane.getItems();
            try {
                ToStringWrapper<UnloadedProject> wrapper = projectListView.getSelectionModel().getSelectedItem();
                if (wrapper == null) {
                    return;
                }
                UnloadedProject project = wrapper.getValue();
                Module module = project.getExpectedModule();
                double[] pos = this.splitPane.getDividerPositions();
                splitItems.remove(1);
                Region info = module.createDisplayInfo(project);
                VBox wrapped = this.createProjectInfo(project, info);
                splitItems.add(wrapped);
                this.splitPane.setDividerPositions(pos);

                wrapped.prefWidthProperty().bind(projectListView.prefWidthProperty());
            } catch (IOException e) {
                e.printStackTrace();
                event.consume();
            }
        });
        projectListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return projectListView;
    }

    private VBox createProjectInfo(UnloadedProject project, Region region) {
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
            if (opWrapper.isEmpty()) {
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

    private NavigationBar createNavBar() {
        var createMenuNavigation = new ArrayList<NavigationItem.EndNavigationItem>();
        Blocks.getInstance()
                .getAllEnabledPlugins()
                .getAll(c -> {
                    var modules = c.getModules();
                    System.out.println("Modules for " + c.getPluginMeta().id() + ": " + Stream.of(modules).parallel().map(Module::getId).collect(Collectors.toSet()));
                    return Arrays.asList(modules);
                })
                .parallelStream()
                .forEach(m -> createMenuNavigation.add(new NavigationItem.EndNavigationItem(m.getDisplayName(), (e) -> m.onProjectCreator())));
        createMenuNavigation.sort(Comparator.comparing(Labeled::getText));
        var createOption = new NavigationItem.TreeNavigationItem("Create", createMenuNavigation.toArray(new NavigationItem[0]));

        var networkJoin = new NavigationItem.EndNavigationItem("Join", (e) -> {
        });
        var networkOption = new NavigationItem.TreeNavigationItem("Network", networkJoin);

        var settingsOption = new NavigationItem.EndNavigationItem("Settings", (e) -> {
            var stage = Blocks.getInstance().getFXWindow();
            var settings = new GeneralSettings();
            stage.setScene(new Scene(settings));
        });

        return new NavigationBar(createOption, networkOption, settingsOption);
    }
}
