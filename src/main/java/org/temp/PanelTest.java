package org.temp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.block.panel.common.dialog.FileViewer;

import java.io.File;

public class PanelTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FileViewer viewer = new FileViewer(new File(System.getProperty("user.home")), (f) -> {});
        viewer.setFilter((f) -> !f.getName().endsWith(".jar"));
        stage.setScene(new Scene(viewer));
        stage.show();
    }
}
