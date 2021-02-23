package org.block;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Parent;

public class MobileBlocks extends Blocks {

    private View view;

    public MobileBlocks(View view) {
        this.view = view;
    }

    @Override
    public Parent getWindow() {
        return (Parent) this.view.getChildren().get(0);
    }

    @Override
    public void setWindow(Parent parent) {
        this.view.getChildren().clear();
        this.view.getChildren().add(parent);
    }
}
