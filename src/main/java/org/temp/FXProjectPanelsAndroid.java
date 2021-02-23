package org.temp;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.panel.launch.ProjectsPanel;
import org.util.print.ShinyOutputStream;

import java.io.File;

public class FXProjectPanelsAndroid extends MobileApplication {

    @Override
    public void init() {
        ShinyOutputStream.createDefault();
        Blocks.setInstance(new Blocks());
        View view = new View(new ProjectsPanel(new File("Projects")));
        addViewFactory(HOME_VIEW, () -> view);
    }

    @Override
    public void postInit(Scene scene) {
        super.postInit(scene);
        Blocks.getInstance().setFXWindow((Stage) scene.getWindow());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
