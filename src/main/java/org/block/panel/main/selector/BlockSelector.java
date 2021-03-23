package org.block.panel.main.selector;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.block.project.block.Block;
import org.block.project.block.BlockNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockSelector extends Parent implements Selector {

    private final BlockNode<? extends Block> blockNode;
    private final Set<String> search = new HashSet<>();

    public BlockSelector(Block block, String... search) {
        this(block.getNode(), search);
    }

    public BlockSelector(BlockNode<? extends Block> blockNode, String... search) {
        this.blockNode = blockNode;
        Label label = new Label(blockNode.getBlock().getType().getName());
        VBox box = new VBox(label, (Node)blockNode);
        VBox.setVgrow((Node)blockNode, Priority.ALWAYS);
        this.getChildren().add(box);
        this.search.add(this.getTitle());
        this.search.addAll(Arrays.asList(search));

    }

    public BlockNode<? extends Block> getBlockNode() {
        return this.blockNode;
    }

    @Override
    public Set<String> getSearchTerms() {
        return this.search;
    }

    @Override
    public String getTitle() {
        return this.blockNode.getBlock().getType().getName();
    }
}
