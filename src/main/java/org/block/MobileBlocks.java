package org.block;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Parent;
import org.temp.FXProjectPanelsAndroid;

public class MobileBlocks extends Blocks {

    private final FXProjectPanelsAndroid launch;

    public MobileBlocks(FXProjectPanelsAndroid launch, String main) {
        super(main);
        this.launch = launch;
        this.init();
    }

    @Override
    public Parent getWindow() {
        return (Parent) this.launch.getView().getChildren().get(0);
    }


    @Override
    public void setWindow(String title) {
        this.launch.switchView(title);
    }

    @Override
    public void registerWindow(String title, Parent parent) {
        this.launch.addViewFactory(title, () -> new View(parent));
    }

    @Override
    public void requestNewWidth(double width) {
    }

    @Override
    public void requestNewHeight(double height) {
    }

    @Override
    public double getHeight() {
        return this.launch.getView().getHeight();
    }

    @Override
    public double getWidth() {
        return this.launch.getView().getWidth();
    }
}
