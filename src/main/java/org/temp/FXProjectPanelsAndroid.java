package org.temp;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.ExceptionDialog;
import com.gluonhq.charm.glisten.control.TextArea;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.MobileBlocks;
import org.block.panel.launch.ProjectsPanel;

import java.io.File;

public class FXProjectPanelsAndroid extends MobileApplication {

    @Override
    public void init() {
        this.addViewFactory(HOME_VIEW, () -> {
            while(true) {
                try {
                    Blocks.setInstance(new MobileBlocks(this, HOME_VIEW));
                    View view = new View(new ProjectsPanel(new File("Projects")));
                    //ShinyOutputStream.createDefault();
                    this.setTitle("Open Blocks");
                    return view;
                } catch (Exception e) {
                    var dialog = new ExceptionDialog();
                    dialog.setException(e);
                    dialog.showAndWait();
                }
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
