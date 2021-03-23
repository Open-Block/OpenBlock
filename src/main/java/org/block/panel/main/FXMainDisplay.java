package org.block.panel.main;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.array.utils.ArrayUtils;
import org.block.panel.main.selector.BlockSelector;
import org.block.panel.main.selector.GroupSelector;
import org.block.panel.main.selector.Selector;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.BlockNode;

import java.util.List;
import java.util.stream.Collectors;

public class FXMainDisplay extends VBox {

    private final VBox blocks = this.createBlocksView();
    private final MenuBar navBar = this.createNavBar();
    private final Pane panel = this.createBlockView();

    public FXMainDisplay() {
        this.init();
    }

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
                .filter((node) -> node instanceof BlockNode)
                .map((node) -> ((BlockNode<? extends Block>) node).getBlock())
                .collect(Collectors.toList());
    }

    private void init() {
        SplitPane splitPane = new SplitPane(this.blocks, this.panel);
        this.getChildren().addAll(this.navBar, splitPane);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        double divide = ArrayUtils.getBest(Double::intValue, (c, c1) -> c > c1, this.blocks.getChildren().parallelStream().filter(n -> (n instanceof Selector)).map(n -> n.getBoundsInLocal().getWidth()).collect(Collectors.toSet())).orElse(25.0);
        this.blocks.setMaxWidth(divide);
    }

    private MenuBar createNavBar() {
        Menu file = new Menu("File");
        Menu network = new Menu("Network");
        return new MenuBar(file, network);
    }

    private void registerBlocks(VBox box) {
        BlockSelector minusBlockSelector = new BlockSelector(BlockType.BLOCK_TYPE_MINUS.buildDefault(), "Negate", "Take");

        GroupSelector mathSelector = new GroupSelector("Math", "Mathematics");
        mathSelector.addChild(minusBlockSelector);

        box.getChildren().add(mathSelector);
    }

    private VBox createBlocksView() {
        var box = new VBox();
        this.registerBlocks(box);
        return box;
    }

    private Pane createBlockView() {
        return new Pane();
    }
}
