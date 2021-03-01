package org.block.panel.settings;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.control.SettingsPane;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import org.block.Blocks;
import org.block.panel.common.AboutToRender;
import org.block.panel.settings.editor.FileEditor;
import org.block.serialization.ConfigImplementation;
import org.util.storage.DesktopStorageService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public class GeneralSettings extends SettingsPane implements Settings, AboutToRender {

    public static Supplier<File> ROOT_PUBLIC_PATH = () -> {
        var opServices = Services.get(StorageService.class);
        if (opServices.isEmpty() && Platform.isDesktop()) {
            opServices = Optional.of(new DesktopStorageService());
        }
        if (opServices.isEmpty()) {
            System.out.println("No storage service");
            return new File("OpenBlocks");
        }
        var opPublicStorage = opServices.get().getPublicStorage("OpenBlocks");
        if (opPublicStorage.isPresent() && opServices.get().isExternalStorageReadable() && opServices.get().isExternalStorageWritable()) {
            return opPublicStorage.get();
        }
        var opPrivateStorage = opServices.get().getPrivateStorage();
        return opPrivateStorage.orElseGet(() -> new File("OpenBlocks"));
    };
    public static final File DEFAULT_PROJECT_PATH = new File(ROOT_PUBLIC_PATH.get(), "Projects");
    public static Supplier<File> ROOT_PRIVATE_PATH = () -> {
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
    public static final File DEFAULT_LOG_PATH = new File(Platform.isDesktop() ? ROOT_PRIVATE_PATH.get() : ROOT_PUBLIC_PATH.get(), "logs/log.txt");
    public static final File DEFAULT_DEBUG_PATH = new File(Platform.isDesktop() ? ROOT_PRIVATE_PATH.get() : ROOT_PUBLIC_PATH.get(), "logs/debug.txt");
    public static final File DEFAULT_PLUGIN_PATH = new File(Platform.isDesktop() ? ROOT_PRIVATE_PATH.get() : ROOT_PUBLIC_PATH.get(), "Plugins");
    public static final File SETTINGS_PATH = new File(ROOT_PRIVATE_PATH.get(), "Settings.json");

    private final BlockOption<String, File, FileEditor> projectPath;
    private final BlockOption<String, File, FileEditor> logPath;
    private final BlockOption<String, File, FileEditor> debugPath;
    private final BlockOption<String, File, FileEditor> pluginPath;
    private File projectPathInitValue = DEFAULT_PROJECT_PATH;
    private File logPathInitValue = DEFAULT_LOG_PATH;
    private File debugPathInitValue = DEFAULT_DEBUG_PATH;
    private File pluginPathInitValue = DEFAULT_PLUGIN_PATH;


    public GeneralSettings() throws IOException {
        this.load();
        this.projectPath = new BlockOption<>(MaterialDesignIcon.APPS.graphic(), "Projects", "Paths", "Paths", this.projectPathInitValue, File::getPath, new FileEditor(File::isDirectory));
        this.logPath = new BlockOption<>(MaterialDesignIcon.APPS.graphic(), "Log", "Paths", "Paths", this.logPathInitValue, File::getPath, new FileEditor());
        this.debugPath = new BlockOption<>(MaterialDesignIcon.APPS.graphic(), "debug", "Paths", "Paths", this.debugPathInitValue, File::getPath, new FileEditor());
        this.pluginPath = new BlockOption<>(MaterialDesignIcon.APPS.graphic(), "plugin", "Paths", "Paths", this.pluginPathInitValue, File::getPath, new FileEditor(File::isDirectory));
        this.init();
    }

    public BlockOption<String, File, FileEditor> getPluginPath() {
        return this.pluginPath;
    }

    public BlockOption<String, File, FileEditor> getProjectPath() {
        return this.projectPath;
    }

    public BlockOption<String, File, FileEditor> getLogPath() {
        return this.logPath;
    }

    public BlockOption<String, File, FileEditor> getDebugPath() {
        return this.debugPath;
    }

    public File getSettingsPath() {
        return SETTINGS_PATH;
    }

    @Override
    public void save() throws IOException {
        var root = ConfigImplementation.JSON.createEmptyNode();
        var path = root.getNode("paths");
        var logs = path.getNode("logs");
        path.setValue("Project", this.getProjectPath().getValue().getAbsolutePath());
        path.setValue("Plugin", this.getPluginPath().getValue().getAbsolutePath());
        logs.setValue("Log", this.getLogPath().getValue().getAbsolutePath());
        logs.setValue("Debug", this.getDebugPath().getValue().getAbsolutePath());
        ConfigImplementation.JSON.write(root, this.getSettingsPath().toPath());
    }

    @Override
    public void load() throws IOException {
        if (!SETTINGS_PATH.exists()) {
            System.out.println("\tNo File");
            return;
        }
        var root = ConfigImplementation.JSON.load(SETTINGS_PATH.toPath());
        var path = root.getNode("paths");
        var logs = path.getNode("logs");
        var opProject = path.getString("Project");
        var opPlugin = path.getString("Plugin");
        var opLog = logs.getString("Log");
        var opDebug = logs.getString("Debug");
        opProject.ifPresent(s -> this.projectPathInitValue = new File(s));
        opPlugin.ifPresent(s -> this.pluginPathInitValue = new File(s));
        opLog.ifPresent(s -> this.logPathInitValue = new File(s));
        opDebug.ifPresent(s -> this.debugPathInitValue = new File(s));
    }

    @Override
    public void onRender() {
        this.getOptions().parallelStream()
                .filter(o -> o instanceof BlockOption)
                .map(o -> (BlockOption<?, ?, ?>) o)
                .filter(o -> o.getEditor().getEditor() instanceof TextField)
                .map(o -> (TextField) o.getEditor().getEditor())
                .forEach(f -> f.setPrefWidth(Blocks.getInstance().getWidth()));
    }

    private void init() {
        this.getOptions().addAll(this.projectPath, this.pluginPath, this.logPath, this.debugPath);
    }
}
