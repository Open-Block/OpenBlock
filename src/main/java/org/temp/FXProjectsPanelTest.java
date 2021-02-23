package org.temp;

import com.gluonhq.charm.down.Services;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.DesktopBlocks;
import org.block.panel.launch.ProjectsPanel;
import org.util.print.ShinyOutputStream;
import org.util.storage.DesktopStorageFactory;
import org.util.storage.DesktopStorageService;

import java.io.File;

public class FXProjectsPanelTest extends Application {
    @Override
    public void start(Stage stage) {
        ShinyOutputStream.createDefault();
        Blocks.setInstance(new DesktopBlocks(stage));
        stage.setTitle("Test");
        stage.setScene(new Scene(new ProjectsPanel(new File("Projects"))));
        stage.setWidth(500);
        stage.setHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        Services.registerServiceFactory(new DesktopStorageFactory(new DesktopStorageService()));
        launch(args);
    }
}
