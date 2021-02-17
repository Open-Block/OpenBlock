package org.block.project.panel.main;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.array.utils.ArrayUtils;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.panel.SceneSource;
import org.block.project.panel.main.selector.BlockSelector;
import org.block.project.panel.main.selector.GroupSelector;
import org.block.project.panel.main.selector.Selector;

import java.util.List;
import java.util.stream.Collectors;

public class FXMainDisplay implements SceneSource {

    private VBox blocks = new VBox();
    private MenuBar navBar = new MenuBar();
    private Pane panel = new Pane();

    public MenuBar getNavBar() {
        return this.navBar;
    }

    public VBox getSelectionBlocks() {
        return this.blocks;
    }

    public Pane getBlockPanel() {
        return this.panel;
    }

    public List<Block> getDisplayingBlocks() {
        return this.panel.getChildren().stream()
                .filter((node) -> node instanceof BlockRender)
                .map((node) -> ((BlockRender) node).getBlock())
                .collect(Collectors.toList());
    }

    private MenuBar createNavBar() {
        Menu file = new Menu("File");
        Menu network = new Menu("Network");
        this.navBar.getMenus().add(file);
        this.navBar.getMenus().add(network);
        return this.navBar;
    }

    private void registerBlocks(VBox box) {
        BlockSelector minusBlockSelector = new BlockSelector(BlockType.BLOCK_TYPE_MINUS.buildDefault(0, 0), "Negate", "Take");

        GroupSelector mathSelector = new GroupSelector("Math", "Mathematics");
        mathSelector.addChild(minusBlockSelector);

        box.getChildren().add(mathSelector);
    }

    private VBox createBlocksView() {
        registerBlocks(this.blocks);
        return this.blocks;
    }

    private Pane createBlockView() {
        return this.panel;
    }

    @Override
    public Scene build() {
        MenuBar navBar = createNavBar();
        VBox blocks = createBlocksView();
        Pane panel = createBlockView();

        SplitPane splitPane = new SplitPane(blocks, panel);
        VBox mainBox = new VBox(navBar, splitPane);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        double divide = ArrayUtils.getBest(Double::intValue, (c, c1) -> c > c1, blocks.getChildren().parallelStream().filter(n -> (n instanceof Selector)).map(n -> n.getBoundsInLocal().getWidth()).collect(Collectors.toSet())).orElse(25.0);
        blocks.setMaxWidth(divide);
        return new Scene(mainBox);
    }
}
