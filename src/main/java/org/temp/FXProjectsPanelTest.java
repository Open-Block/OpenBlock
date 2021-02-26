package org.temp;

import com.gluonhq.charm.down.Services;
import javafx.application.Application;
import javafx.stage.Stage;
import org.block.Blocks;
import org.block.DesktopBlocks;
import org.block.panel.launch.ProjectsPanel;
import org.util.print.ShinyOutputStream;
import org.util.storage.DesktopStorageFactory;
import org.util.storage.DesktopStorageService;

import java.io.File;
import java.io.IOException;

public class FXProjectsPanelTest extends Application {
    @Override
    public void start(Stage stage) {
        try {
            ShinyOutputStream.createDefault();
            Blocks.setInstance(new DesktopBlocks(stage, "Home"));
            stage.setTitle("Test");
            var panel = new ProjectsPanel(new File("Projects"));
            Blocks.getInstance().registerWindow("Home", panel);
            Blocks.getInstance().setWindow("Home");
            stage.setWidth(500);
            stage.setHeight(500);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Services.registerServiceFactory(new DesktopStorageFactory(new DesktopStorageService()));
        launch(args);
    }
}
