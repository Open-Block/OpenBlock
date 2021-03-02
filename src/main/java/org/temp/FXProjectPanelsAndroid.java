package org.temp;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.TextArea;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.scene.control.ScrollPane;
import org.block.Blocks;
import org.block.MobileBlocks;
import org.block.panel.launch.ProjectsPanel;
import org.block.panel.settings.GeneralSettings;

import java.io.File;

public class FXProjectPanelsAndroid extends MobileApplication {

    @Override
    public void init() {
        this.addViewFactory(HOME_VIEW, () -> {
            try {
                GeneralSettings.ROOT_PUBLIC_PATH.get().mkdirs();


                Blocks.setInstance(new MobileBlocks(this, HOME_VIEW));
                View view = new View(new ProjectsPanel(new File("Projects")));
                //ShinyOutputStream.createDefault();
                this.setTitle("Open Blocks");
                return view;
            } /*catch (AccessDeniedException e) {
                FileViewer viewer = new FileViewer();
                viewer.setBackWindow(HOME_VIEW);
                viewer.setStartingFile(GeneralSettings.ROOT_PUBLIC_PATH.get());
                viewer.setAcceptFolders(true);
                viewer.setFilter((f) -> false);
                viewer.setSelected((f) -> {
                    GeneralSettings.ROOT_PRIVATE_PATH = () -> f;
                    GeneralSettings.ROOT_PUBLIC_PATH = () -> f;
                });
                return new View(viewer);
            }*/ catch (Exception e) {
                var area = new TextArea(Blocks.exceptionToString(e));
                area.setDisable(true);
                return new View(new ScrollPane(area));
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
