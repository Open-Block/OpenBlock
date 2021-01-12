package org.block.project.panel.main;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.block.project.block.Block;

public class BlockRender extends Pane {

    private final Block block;
    private final Canvas canvas = new Canvas();
    private Color selectedColour = Color.RED;
    private boolean isSelected;

    public BlockRender(Block block){
        this.block = block;
        this.getChildren().add(this.canvas);
        this.setHeight(block.getGraphicShape().getHeight());
        this.setWidth(block.getGraphicShape().getWidth());
        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
    }

    public Block getBlock(){
        return this.block;
    }

    public Canvas getCanvas(){
        return this.canvas;
    }

    public Color getSelectedColour(){
        return this.selectedColour;
    }

    public void setSelectedColour(Color colour){
        this.selectedColour = colour;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }

    @Override
    protected void layoutChildren(){
        super.layoutChildren();
        GraphicsContext graphics = this.canvas.getGraphicsContext2D();
        this.block.getGraphicShape().draw(graphics);
    }


}
