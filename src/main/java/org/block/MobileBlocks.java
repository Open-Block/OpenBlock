package org.block;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Parent;
import org.block.panel.common.AboutToRender;
import org.temp.FXProjectPanelsAndroid;

import java.io.IOException;

public class MobileBlocks extends Blocks {

    private final FXProjectPanelsAndroid launch;

    public MobileBlocks(FXProjectPanelsAndroid launch, String main) throws IOException {
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
        var opResult = this.launch.switchView(title);
        if (opResult.isEmpty()) {
            return;
        }
        opResult.get().getChildren().stream().filter(n -> n instanceof AboutToRender).forEach(n -> ((AboutToRender) n).onRender());
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
        var view = this.launch.getView();
        if (view == null) {
            return 0;
        }
        return view.getHeight();
    }

    @Override
    public double getWidth() {
        var view = this.launch.getView();
        if (view == null) {
            return 0;
        }
        return view.getWidth();
    }
}
