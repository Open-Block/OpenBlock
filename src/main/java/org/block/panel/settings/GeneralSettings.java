package org.block.panel.settings;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import org.block.panel.settings.editor.FileEditor;
import org.util.storage.DesktopStorageService;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

public class GeneralSettings extends SettingsPane {

    public static final Supplier<File> ROOT_PUBLIC_PATH = () -> {
        var opServices = Services.get(StorageService.class);
        if (opServices.isEmpty() && Platform.isDesktop()) {
            opServices = Optional.of(new DesktopStorageService());
        }
        if (opServices.isEmpty()) {
            System.out.println("No storage service");
            return new File("OpenBlocks");
        }
        var opPublicStorage = opServices.get().getPublicStorage("OpenBlocks");
        if (opPublicStorage.isPresent()) {
            return opPublicStorage.get();
        }
        var opPrivateStorage = opServices.get().getPrivateStorage();
        return opPrivateStorage.orElseGet(() -> new File("OpenBlocks"));
    };
    public static final Supplier<File> ROOT_PRIVATE_PATH = () -> {
        var opServices = Services.get(StorageService.class);
        if (opServices.isEmpty() && Platform.isDesktop()) {
            opServices = Optional.of(new DesktopStorageService());
        }
        if (opServices.isEmpty()) {
            System.out.println("No storage service");
            return new File("OpenBlocks");
        }
        var opPrivateStorage = opServices.get().getPrivateStorage();
        return opPrivateStorage.orElseGet(() -> new File("OpenBlocks"));
    };
    public static final File DEFAULT_PROJECT_PATH = new File(ROOT_PUBLIC_PATH.get(), "Projects");
    public static final File DEFAULT_LOG_PATH = new File(ROOT_PRIVATE_PATH.get(), "logs/log.txt");
    public static final File DEFAULT_DEBUG_PATH = new File(ROOT_PRIVATE_PATH.get(), "logs/debug.txt");
    public static final File DEFAULT_PLUGIN_PATH = new File(ROOT_PRIVATE_PATH.get(), "Plugins");

    private final Option<File> projectPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Projects", "Paths", "Paths", DEFAULT_PROJECT_PATH, true, o -> new FileEditor(o, File::isDirectory));
    private final Option<File> logPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "Log", "Paths", "Paths", DEFAULT_LOG_PATH, true, FileEditor::new);
    private final Option<File> debugPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "debug", "Paths", "Paths", DEFAULT_DEBUG_PATH, true, FileEditor::new);
    private final Option<File> pluginPath = new DefaultOption<>(MaterialDesignIcon.APPS.graphic(), "plugin", "Paths", "Paths", DEFAULT_PLUGIN_PATH, true, o -> new FileEditor(o, File::isDirectory));

    public GeneralSettings() {
        this.init();
    }

    public <T> T getValue(Option<T> option) {
        return option.valueProperty().getValue();
    }

    public Option<File> getPluginPath() {
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
        this.getOptions().addAll(this.projectPath, this.pluginPath, this.logPath, this.debugPath);
    }
}
