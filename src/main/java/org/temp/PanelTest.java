package org.temp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.panel.common.dialog.FileViewer;

import java.io.File;

public class PanelTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(500);
        stage.setHeight(500);
        FileViewer viewer = new FileViewer();
        viewer.setStartingFile(new File(System.getProperty("user.home")));
        viewer.setFilter((f) -> !f.getName().endsWith(".jar"));
        try {
            String test = null;
            test.toLowerCase();
            stage.setScene(new Scene(viewer));
        }catch (Exception e){
            stage.setScene(new Scene(new TextArea(Blocks.exceptionToString(e))));
        }
        stage.show();
    }
}
