package org.block.panel.common.navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class NavigationItem extends Button {

    public static class EndNavigationItem extends NavigationItem {

        public EndNavigationItem(String text, EventHandler<ActionEvent> event) {
            super(text, event);
        }
    }

    public static class TreeNavigationItem extends NavigationItem {

        private NavigationItem[] children;

        public TreeNavigationItem(String text, NavigationItem... items) {
            super(text, e -> {});
            this.children = items;
            this.setOnAction((e) -> ((NavigationBar)TreeNavigationItem.this.getParent().getParent()).onAction(TreeNavigationItem.this));

        }

        public void setNextRow(NavigationItem... row){
this.children = row;
        }

        public NavigationItem[] getNextRow(){
            return this.children;
        }
    }

    public NavigationItem(String text, EventHandler<ActionEvent> event){
        super(text);
        this.setOnAction(event);
    }
}
