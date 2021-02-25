package org.block.panel.settings;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import org.block.panel.settings.editor.FileEditor;

import java.io.File;
import java.util.Optional;

public class GeneralSettings extends SettingsPane {

    public static final File DEFAULT_PROJECT_PATH = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage("OpenBlocks").map(f -> new File(f, "Projects"))).orElse(new File("Projects"));
    public static final File DEFAULT_LOG_PATH = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage("OpenBlocks").map(f -> new File(f, "logs/log.txt"))).orElse(new File("logs/log.txt"));
    public static final File DEFAULT_DEBUG_PATH = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage("OpenBlocks").map(f -> new File(f, "logs/debug.txt"))).orElse(new File("logs/debug.txt"));
    public static final File DEFAULT_PLUGIN_PATH = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage("OpenBlocks").map(f -> new File(f, "Plugins"))).orElse(new File("Plugins"));

    private final Option<File> projectPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Projects", "Paths", "Paths", DEFAULT_PROJECT_PATH, true, o -> new FileEditor(o, File::isDirectory));
    private final Option<File> logPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Log", "Paths", "Paths", DEFAULT_LOG_PATH, true, FileEditor::new);
    private final Option<File> debugPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "debug", "Paths", "Paths", DEFAULT_DEBUG_PATH, true, FileEditor::new);
    private final Option<File> pluginPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "plugin", "Paths", "Paths", DEFAULT_PLUGIN_PATH, true, o -> new FileEditor(o, File::isDirectory));

    public GeneralSettings() {
        this.init();
    }

    public <T> T getValue(Option<T> option){
        return option.valueProperty().getValue();
    }

    public Option<File> getPluginPath(){
        return this.pluginPath;
    }

    public Option<File> getProjectPath() {
        return this.projectPath;
    }

    public Option<File> getLogPath() {
        return this.logPath;
    }

    public Option<File> getDebugPath() {
        return this.debugPath;
    }

    private void init() {
        this.getOptions().addAll(this.projectPath, this.logPath, this.debugPath);
    }
}
