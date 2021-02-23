package org.block;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Parent;

public class MobileBlocks extends Blocks {

    private final View view;

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

    @Override
    public void requestNewWidth(double width) {
    }

    @Override
    public void requestNewHeight(double height) {
    }

    @Override
    public double getHeight() {
        return this.view.getHeight();
    }

    @Override
    public double getWidth() {
        return this.view.getWidth();
    }
}
