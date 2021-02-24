package org.block.plugin.standard;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.block.Blocks;
import org.block.panel.launch.ProjectsPanel;
import org.block.plugin.PluginContainer;
import org.block.plugin.standard.panel.FXOpenBlockNewPanel;
import org.block.project.module.Module;
import org.block.project.module.project.Project;
import org.block.project.module.project.UnloadedProject;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;
import org.block.util.BlockUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class OpenBlockModule implements Module {

    @Override
    public String getDisplayName() {
        return "Standard Java";
    }

    @Override
    public String getId() {
        return "java_module";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    @Deprecated
    public PluginContainer getPlugin() {
        return PluginContainer.OPEN_BLOCK_CONTAINER;
    }

    @Override
    public boolean canLoad(UnloadedProject project) {
        return false;
    }

    @Override
    public OpenBlockProject load(UnloadedProject project, ConfigImplementation<? extends ConfigNode> impl) {
        OpenBlockProject project1 = new OpenBlockProject(project);
        return project1;
    }

    @Override
    public void loadBlocks(Project.Loaded project, ConfigImplementation<? extends ConfigNode> impl) {
        BlockUtils.load(project, impl, b -> {
        });
    }

    @Override
    public void onProjectCreator(ProjectsPanel panel) {
        var newPanel = new FXOpenBlockNewPanel(Blocks.getInstance().LAUNCH_WINDOW);
        Blocks.getInstance().registerWindow(FXOpenBlockNewPanel.OPEN_BLOCK_CREATOR_WINDOW, newPanel);
        Blocks.getInstance().setWindow(FXOpenBlockNewPanel.OPEN_BLOCK_CREATOR_WINDOW);
    }

    @Override
    public @NotNull GridPane createDisplayInfo(@NotNull UnloadedProject project) {
        String displayName = "Corrupted";
        try {
            displayName = project.getDisplayName();
        } catch (IOException e) {
        }

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        this.addGridRowToDisplayInfo(gridPane, "name", displayName, 0);

        return gridPane;
    }

    private void addGridRowToDisplayInfo(GridPane gridPane, String key, String value, int row) {
        Label keyL = new Label(key);
        Label valueL = new Label(value);
        valueL.setPadding(new Insets(0, 10, 0, 10));
        gridPane.add(keyL, 0, row);
        gridPane.add(valueL, 1, row);
        GridPane.setHgrow(keyL, Priority.ALWAYS);
    }
}
