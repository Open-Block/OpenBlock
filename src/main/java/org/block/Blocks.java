package org.block;

import javafx.scene.Parent;
import org.block.network.client.ClientConnection;
import org.block.network.server.ServerConnection;
import org.block.panel.plugin.PluginCreator;
import org.block.panel.settings.GeneralSettings;
import org.block.panel.settings.SettingsDisplay;
import org.block.plugin.Plugin;
import org.block.project.Project;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class Blocks {

    public static final int[] VERSION = {0, 0, 0, 1};
    private static Blocks instance;
    public final String LAUNCH_WINDOW;
    public final String GENERAL_SETTINGS_WINDOW = "GeneralSettings";
    public final String PLUGIN_CREATOR_WINDOW = "PluginCreator";
    public final String BLOCKS_WINDOW = "Blocks";
    private final Set<Plugin> plugins = new HashSet<>();
    private final GeneralSettings settings;
    private final PluginCreator pluginCreatorPanel = new PluginCreator();
    private Project.Loaded loadedProject;
    private ServerConnection server;
    private ClientConnection client;

    public Blocks(String titleOfMain) throws IOException {
        this.LAUNCH_WINDOW = titleOfMain;
        this.settings = new GeneralSettings();
    }

    public abstract Parent getWindow();

    public abstract void setWindow(String title);

    public abstract void registerWindow(String title, Parent parent);

    public abstract void requestNewWidth(double width);

    public abstract void requestNewHeight(double height);

    public abstract double getHeight();

    public abstract double getWidth();

    public GeneralSettings getSettings() {
        return this.settings;
    }

    public Optional<ClientConnection> getClient() {
        return Optional.ofNullable(this.client);
    }

    public void setClient(ClientConnection client) {
        this.client = client;
    }

    public Optional<ServerConnection> getServer() {
        return Optional.ofNullable(this.server);
    }

    public void setServer(ServerConnection connection) {
        this.server = connection;
    }

    public Optional<Project.Loaded> getLoadedProject() {
        return Optional.ofNullable(this.loadedProject);
    }

    public void setLoadedProject(Project.Loaded loaded) {
        this.loadedProject = loaded;
    }

    public Set<Plugin> getPlugins() {
        return this.plugins;
    }

    public void registerPlugin(Plugin container) {
        this.plugins.add(container);
    }

    public PluginCreator getPluginCreatorPanel() {
        return this.pluginCreatorPanel;
    }

    public static int[] parseVersion(String version) {
        String[] split = version.split("\\.");
        int[] ret = new int[4];
        for (int A = 0; A < Math.min(split.length, 4); A++) {
            ret[A] = Integer.parseInt(split[A]);
        }
        return ret;
    }

    public static int compareVersion(int master, int major, int minor, int patch) {
        if (VERSION[0] > master) {
            return 1;
        } else if (VERSION[0] < master) {
            return -1;
        }
        if (VERSION[1] > major) {
            return 1;
        } else if (VERSION[1] < major) {
            return -1;
        }
        if (VERSION[2] > minor) {
            return 1;
        } else if (VERSION[2] < minor) {
            return -1;
        }
        if (VERSION[3] > patch) {
            return 1;
        } else if (VERSION[3] < patch) {
            return -1;
        }
        return 0;
    }

    public static void setInstance(Blocks blocks) {
        instance = blocks;
    }

    public static Blocks getInstance() {
        return instance;
    }

    public static String exceptionToString(Throwable throwable) {
        var message = throwable.getClass().getName() + "\n\n" + throwable.getLocalizedMessage() + "\n" + throwable.getMessage() + "\n";
        for (var trace : throwable.getStackTrace()) {
            message = message + trace.toString() + "\n";
        }
        return message;
    }

    protected void init() {
        this.registerWindow(this.GENERAL_SETTINGS_WINDOW, new SettingsDisplay<>(this.settings, this.LAUNCH_WINDOW));
        this.registerWindow(this.PLUGIN_CREATOR_WINDOW, this.pluginCreatorPanel);
    }
}
