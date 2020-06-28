package org.block.panel.block;

import java.awt.*;

/**
 * Assists with building common shapes.
 */
public interface Shapes {

    int ATTACHABLE_WIDTH = 10;
    int ATTACHABLE_HEIGHT = 10;

    static Polygon drawAttachableConnector(int x, int y, int width, int height){
        Polygon poly = new Polygon();
        poly.addPoint(x, y + (height / 4));
        poly.addPoint(x + width, y + (height / 4));
        poly.addPoint(x + width, y + (height - (height / 4)));
        poly.addPoint(x, y + (height - (height / 4)));
        return poly;
    }

    static Polygon drawAttachingConnector(int x, int y, int width, int height){
        Polygon poly = new Polygon();
        poly.addPoint(x, y);
        poly.addPoint(x + width, y);
        poly.addPoint(x + width, y + height);
        poly.addPoint(x, y + height);
        poly.addPoint(x, y + (height - (height / 4)));
        poly.addPoint(x + width, y + (height - (height / 4)));
        poly.addPoint(x + width, y + (height / 4));
        poly.addPoint(x, y + (height / 4));
        return poly;

    }
}
