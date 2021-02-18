package org.block.panel.common.navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public class TabbedNavigationBar extends TabPane {

    public TabbedNavigationBar() {
        this(new Tab[0]);
    }

    public TabbedNavigationBar(Tab... tabs) {
        super(tabs);
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> TabbedNavigationBar.this.getSelectionModel().getSelectedItem().getContent().setVisible(false));
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> TabbedNavigationBar.this.getSelectionModel().getSelectedItem().getContent().setVisible(true));

    }
}
