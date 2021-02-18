package org.block.panel.common.navigation;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class NavigationBar extends HBox {

    public NavigationBar(Node... nodes) {
        this(20, nodes);
    }

    public NavigationBar(double v, Node... nodes) {
        super(v, nodes);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
    }
}
