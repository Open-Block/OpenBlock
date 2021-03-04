package org.block.panel.launch;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.block.Blocks;
import org.block.panel.common.navigation.NavigationBar;
import org.block.panel.common.navigation.NavigationItem;
import org.block.panel.main.FXMainDisplay;
import org.block.plugin.Plugin;
import org.block.plugin.ResourcePlugin;
import org.block.plugin.file.PluginStreamReader;
import org.block.project.Project;
import org.block.project.UnloadedProject;
import org.block.serialization.ConfigImplementation;
import org.block.util.GeneralUntil;
import org.block.util.ToStringWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectsPanel extends VBox {

    private final File projectsDirectory;
    private final SplitPane splitPane = new SplitPane();
    private final NavigationBar navBar;
    private final TextField search;
    private final ListView<ToStringWrapper<UnloadedProject>> projectListView;
    private final Set<UnloadedProject> projects = new HashSet<>();
    private final Button delete = new Button("Delete");
    private final Button create = new Button("Load");

    public ProjectsPanel(@NotNull File projectsDirectory) throws IOException {
        this.projectsDirectory = projectsDirectory;
        this.navBar = this.createNavBar();
        this.projectListView = this.createProjectList();
        this.search = this.createSearch();
        this.init();
    }

    public ListView<ToStringWrapper<UnloadedProject>> getProjectListView() {
        return this.projectListView;
    }

    public Collection<UnloadedProject> getProjects() {
        return Collections.unmodifiableCollection(this.projects);
    }

    public File getProjectDirectory() {
        return this.projectsDirectory;
    }

    public SplitPane getSplitPane() {
        return this.splitPane;
    }

    public void searchForProjects() {
        this.projects.clear();
        String[] files = this.projectsDirectory.list();
        if (files == null) {
            System.out.println("No Files found in " + this.projectsDirectory.getAbsolutePath());
            return;
        }
        for (String fileName : files) {
            File folder = new File(this.projectsDirectory, fileName);
            if (!folder.isDirectory()) {
                continue;
            }
            System.out.println("Looking in folder: " + folder.getName());
            File openBlockFile = new File(folder, "OpenBlocks.json");
            if (!openBlockFile.exists()) {
                System.err.println("Invalid folder found: " + openBlockFile.getAbsolutePath());
                continue;
            }
            if (this.projects.parallelStream().anyMatch(p -> p.getDirectory().equals(folder))) {
                continue;
            }
            this.projects.add(new UnloadedProject(folder));
        }
        this.projectListView.getItems().clear();
        this.projectListView.getItems().addAll(this.projects.parallelStream().map(p -> new ToStringWrapper<>(p, u -> {
            try {
                return u.getDisplayName();
            } catch (IOException e) {
                return u.getFile().getName();
            }
        })).collect(Collectors.toSet()));
    }

    private void init() throws IOException {
        var vBox = new VBox(this.search, this.projectListView);
        this.splitPane.getItems().add(vBox);
        this.splitPane.getItems().add(new Pane());
        this.splitPane.setDividerPosition(0, this.splitPane.getDividerPositions()[0] / 2);
        VBox.setVgrow(this.splitPane, Priority.ALWAYS);
        this.getChildren().addAll(this.navBar, this.splitPane);
        new Thread(this::searchForProjects).start();

        var pluginPath = Blocks.getInstance().getSettings().getPluginPath().getValue();
        if (!pluginPath.exists()) {
            Files.createDirectories(pluginPath.toPath());
        }

        Stream.of(ResourcePlugin.values()).forEach(p -> {
            try {
                File file = p.copyTo(pluginPath);
                System.out.println("Created plugin: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Could not create plugin " + p.name());
                e.printStackTrace();
            }
        });

    }

    private String toDisplayName(UnloadedProject project) {
        try {
            return project.getDisplayName();
        } catch (IOException ioException) {
            return project.getFile().getName();
        }
    }

    private TextField createSearch() {
        var field = new TextField();
        field.setOnKeyTyped(e -> {
            var filtered = this.projects.parallelStream()
                    .filter(p -> {
                        var displayName = this.toDisplayName(p);
                        var compare = field.getText().toLowerCase();
                        System.out.println("\tComparing: " + displayName + " vs " + compare);
                        return displayName.toLowerCase().contains(compare.toLowerCase());
                    })
                    .sorted(Comparator.comparing(this::toDisplayName))
                    .map(p -> new ToStringWrapper<>(p, u -> this.toDisplayName(p)))
                    .collect(Collectors.toList());
            this.projectListView.getItems().clear();
            this.projectListView.getItems().addAll(filtered);
        });
        return field;
    }

    private Set<Plugin> searchForPlugins() {
        var plugins = new HashSet<Plugin>();
        var pluginFolder = Blocks.getInstance().getSettings().getPluginPath().getValue();
        var pluginFolders = pluginFolder.listFiles(File::isDirectory);
        for (var rootPluginFolder : pluginFolders) {
            try {
                var pluginLoader = new File(rootPluginFolder, rootPluginFolder.getName() + ".json");
                var pluginStreamReader = new PluginStreamReader(new FileInputStream(pluginLoader));
                plugins.add(pluginStreamReader.readPlugin());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return plugins;
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
                Region info;
                double[] pos = this.splitPane.getDividerPositions();
                splitItems.remove(1);

                try {
                    Plugin plugin = project.getExpectedPlugin();
                    info = plugin.createDisplayInfo(project);
                    this.create.setDisable(false);
                } catch (IllegalStateException e) {
                    var pluginId = ConfigImplementation.JSON.load(project.getFile().toPath()).getNode(Project.CONFIG_PLUGIN.getNode()).getString(Project.CONFIG_PLUGIN.getTitle()).orElse("");
                    info = new Label("Cannot find the attached plugin: " + pluginId);
                    this.create.setDisable(true);
                }
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
        this.create.setOnAction((event) -> {
            FXMainDisplay display = new FXMainDisplay();
            Blocks.getInstance().registerWindow(Blocks.getInstance().BLOCKS_WINDOW, display);
            Blocks.getInstance().setWindow(Blocks.getInstance().BLOCKS_WINDOW);
        });
        this.delete.setOnAction((event) -> {
            GeneralUntil.getFiles(project.getDirectory()).parallelStream().forEach(File::delete);
            project.getDirectory().delete();

            Optional<ToStringWrapper<UnloadedProject>> opWrapper = this.projectListView.getItems().parallelStream().filter(w -> w.getValue().equals(project)).findFirst();
            if (opWrapper.isEmpty()) {
                return;
            }
            this.projectListView.getItems().remove(opWrapper.get());
            this.searchForProjects();
        });
        HBox box = new HBox(this.delete, this.create);

        VBox area = new VBox(region, box);
        VBox.setVgrow(region, Priority.ALWAYS);
        area.setFillWidth(true);

        this.create.prefWidthProperty().bind(area.widthProperty());
        this.delete.prefWidthProperty().bind(area.widthProperty());

        region.prefWidthProperty().bind(area.widthProperty());
        region.prefHeightProperty().bind(area.heightProperty());
        return area;
    }

    private NavigationBar createNavBar() {
        var createOption = new NavigationItem.TreeNavigationItem("Create", () -> {
            var createMenuNavigation = new ArrayList<NavigationItem.EndNavigationItem>();
            var plugins = Blocks.getInstance().getPlugins();
            if (plugins.isEmpty()) {
                plugins = this.searchForPlugins();
            }
            plugins
                    .parallelStream()
                    .forEach(m -> createMenuNavigation.add(new NavigationItem.EndNavigationItem(m.getName(), (e) -> {
                        var panel = Blocks.getInstance().getPluginCreatorPanel();
                        panel.setPlugin(m);
                        Blocks.getInstance().setWindow(Blocks.getInstance().PLUGIN_CREATOR_WINDOW);
                    })));
            createMenuNavigation.sort(Comparator.comparing(Labeled::getText));
            return createMenuNavigation.toArray(new NavigationItem.EndNavigationItem[0]);
        });

        var networkJoin = new NavigationItem.EndNavigationItem("Join", (e) -> {
        });
        var networkOption = new NavigationItem.TreeNavigationItem("Network", networkJoin);

        var settingsOption = new NavigationItem.EndNavigationItem("Settings", (e) -> {
            Blocks.getInstance().setWindow(Blocks.getInstance().GENERAL_SETTINGS_WINDOW);
        });

        return new NavigationBar(createOption, networkOption, settingsOption);
    }
}
