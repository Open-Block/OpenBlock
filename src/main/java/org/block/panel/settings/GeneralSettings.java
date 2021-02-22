package org.block.panel.settings;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import org.block.panel.settings.editor.FileEditor;

import java.io.File;

public class GeneralSettings extends SettingsPane {

    private Option<File> projectPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Projects", "Paths", "Paths", DEFAULT_PROJECT_PATH, true, o -> new FileEditor(o, File::isDirectory));
    private Option<File> logPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Log", "Paths", "Paths", DEFAULT_LOG_PATH, true, FileEditor::new);
    private Option<File> debugPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "debug", "Paths", "Paths", DEFAULT_DEBUG_PATH, true, FileEditor::new);

    public static final File DEFAULT_PROJECT_PATH = Services.get(StorageService.class).flatMap(s -> s.getPrivateStorage().map(f -> new File(f, "Projects"))).orElse(new File("Project"));
    public static final File DEFAULT_LOG_PATH = Services.get(StorageService.class).flatMap(s -> s.getPrivateStorage().map(f -> new File(f, "logs/log.txt"))).orElse(new File("logs/log.txt"));
    public static final File DEFAULT_DEBUG_PATH = Services.get(StorageService.class).flatMap(s -> s.getPrivateStorage().map(f -> new File(f, "logs/debug.txt"))).orElse(new File("logs/debug.txt"));

    public GeneralSettings(){
init();
    }

    private void init(){
        this.getOptions().addAll(this.projectPath, this.logPath, this.debugPath);
    }

    public Option<File> getProjectPath(){
        return this.projectPath;
    }
}
