package org.block.panel.common.navigation;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabbedNavigationBar extends TabPane {

    public TabbedNavigationBar() {
    }

    public TabbedNavigationBar(Tab... tabs) {
        super(tabs);
    }
}