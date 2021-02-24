package org.temp;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.TextArea;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.array.utils.ArrayUtils;
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
            try {
                View view = new View();
                Blocks.setInstance(new MobileBlocks(view));
                view.getChildren().add(new ProjectsPanel(new File("Projects")));
                return view;
            } catch (Throwable e) {
                String stackTrace = ArrayUtils.toString("\n\t", t -> t.getFileName() + "[" + t.getLineNumber() + "]", e.getStackTrace());
                return new View(new VBox(new Label(e.getMessage()), new TextArea(stackTrace)));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
