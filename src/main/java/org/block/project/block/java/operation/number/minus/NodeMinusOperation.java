package org.block.project.block.java.operation.number.minus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.block.project.block.java.operation.number.NodeNumberOperation;
import org.block.project.block.type.attachable.NodeBlockSelector;

import java.util.stream.Collectors;


public class NodeMinusOperation extends VBox implements NodeNumberOperation<MinusOperation>, NodeBlockSelector<MinusOperation> {

    private final MinusOperation block;

    public NodeMinusOperation(MinusOperation numberOperation) {
        this.block = numberOperation;
        updateBlock();
    }

    @Override
    protected double computePrefWidth(double v) {
        var width = this.getBlock().getGroups().parallelStream().map(g -> (MinusOperation.MinusBlockGroup)g).map(g -> g.getBlockNode().getWidth()).reduce(Double::sum).orElseThrow();
        width = width + (this.getBlock().getGroups().size() * 5);

        var labelWidth = new Label("minus").getWidth();
        if(labelWidth > width){
            return labelWidth;
        }
        return width;
    }

    @Override
    protected double computePrefHeight(double v) {
        return super.computePrefHeight(v);
    }

    @Override
    public MinusOperation getBlock() {
        return this.block;
    }

    @Override
    public void updateBlock() {
        this.getChildren().clear();
        var block = this.getBlock();
        var label = new Label("Minus");
        var minusValues = block.getGroups().parallelStream().map(g -> (Node)g.getBlockNode()).collect(Collectors.toList());
        var minusValueGroups = new HBox(5);
        minusValueGroups.setAlignment(Pos.CENTER);
        minusValueGroups.getChildren().addAll(minusValues);
        this.getChildren().addAll(label, minusValueGroups);
        this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
    }
}
