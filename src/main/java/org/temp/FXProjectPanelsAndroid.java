package org.temp;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import org.block.Blocks;
import org.block.MobileBlocks;
import org.block.panel.launch.ProjectsPanel;
import org.util.print.ShinyOutputStream;

import java.io.File;

public class FXProjectPanelsAndroid extends MobileApplication {

    @Override
    public void init() {
        ShinyOutputStream.createDefault();
        this.addViewFactory(HOME_VIEW, () -> {
          View view = new View(new ProjectsPanel(new File("Projects")));
        Blocks.setInstance(new MobileBlocks(view));
          return view;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
