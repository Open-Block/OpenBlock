package org.block.panel.common.navigation;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class NavigationBar extends VBox {

    private final List<HBox> options = new ArrayList<>();

    @Deprecated
    public NavigationBar() {
        throw new IllegalArgumentException("Navigation bar must have at least one child");
    }

    public NavigationBar(NavigationItem... items) {
        this(new HBox(items));
    }

    public NavigationBar(HBox... box) {
        this(Arrays.asList(box));
    }

    public NavigationBar(Collection<HBox> box) {
        if (box.isEmpty()) {
            throw new IllegalArgumentException("Navigation bar must have at least one child");
        }
        this.options.addAll(box);
        this.updateRows();
    }

    public void updateRows() {
        this.getChildren().clear();
        if (this.options.size() > 1) {
            this.getChildren().add(this.options.get(this.options.size() - 2));
        }
        this.getChildren().add(this.options.get(this.options.size() - 1));
    }

    public Optional<Integer> getRow(Node child) {
        for (int A = 0; A < this.options.size(); A++) {
            if (this.options.get(A).getChildren().contains(child)) {
                return Optional.of(A);
            }
        }
        return Optional.empty();
    }

    protected void onAction(NavigationItem.TreeNavigationItem item) {
        int row = this.getRow(item).orElseThrow();
        while ((this.options.size() - 1) != row) {
            this.options.remove(this.options.size() - 1);
        }
        this.options.add(new HBox(item.getNextRow()));
        this.updateRows();
    }


}
