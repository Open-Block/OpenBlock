package org.block.project.block;

import javafx.scene.canvas.GraphicsContext;

public interface BlockGraphics {

    void draw(GraphicsContext context);
    int getWidth();
    int getHeight();
}
