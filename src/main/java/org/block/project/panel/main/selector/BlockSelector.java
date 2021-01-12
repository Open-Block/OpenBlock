package org.block.project.panel.main.selector;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.block.project.block.Block;
import org.block.project.panel.main.BlockRender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockSelector extends Parent implements Selector {

    private final BlockRender render;
    private final Set<String> search = new HashSet<>();

    public BlockSelector(Block block, String... search){
        this(block.getGraphicRender(), search);
    }
    public BlockSelector(BlockRender render, String... search){
        this.render = render;
        Label label = new Label(render.getBlock().getType().getName());
        VBox box = new VBox(label, render);
        VBox.setVgrow(render, Priority.ALWAYS);
        this.getChildren().add(box);
        this.search.add(this.getTitle());
        this.search.addAll(Arrays.asList(search));

    }

    public BlockRender getRender(){
        return this.render;
    }

    @Override
    public Set<String> getSearchTerms() {
        return this.search;
    }

    @Override
    public String getTitle() {
        return this.render.getBlock().getType().getName();
    }
}
