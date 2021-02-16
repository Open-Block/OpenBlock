package org.block;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.block.network.client.ClientConnection;
import org.block.network.server.ServerConnection;
import org.block.plugin.PluginContainer;
import org.block.plugin.PluginContainers;
import org.block.project.module.project.Project;
import org.block.project.panel.SceneSource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Blocks {

    private final PluginContainers plugins = new PluginContainers();
    private Project.Loaded loadedProject;
    private ServerConnection server;
    private ClientConnection client;

    private Stage win;
    private SceneSource source;

    public static final int[] VERSION = {0, 0, 0, 1};

    private static Blocks instance;

    public SceneSource getSceneSource(){
        return this.source;
    }

    public void setSceneSource(SceneSource source){
        this.source = source;
    }

    public Stage getFXWindow(){
        return this.win;
    }

    public void setFXWindow(Stage window){
        this.win = window;
    }

    public Optional<ClientConnection> getClient(){
        return Optional.ofNullable(this.client);
    }

    public void setClient(ClientConnection client){
        this.client = client;
    }

    public Optional<ServerConnection> getServer(){
        return Optional.ofNullable(this.server);
    }

    public void setServer(ServerConnection connection){
        this.server = connection;
    }

    public Optional<Project.Loaded> getLoadedProject(){
        return Optional.ofNullable(this.loadedProject);
    }

    public void setLoadedProject(Project.Loaded loaded){
        this.loadedProject = loaded;
    }

    public PluginContainers getPlugins(){
        return new PluginContainers(this.plugins);
    }

    public PluginContainers getAllPlugins(){
        Set<PluginContainer> set = new HashSet<>(this.plugins);
        set.add(PluginContainer.OPEN_BLOCK_CONTAINER);
        return new PluginContainers(set);
    }

    public PluginContainers getEnabledPlugins(){
        return new PluginContainers(this.plugins.parallelStream().filter(p -> p.isDisabled()).collect(Collectors.toSet()));
    }

    public PluginContainers getAllEnabledPlugins(){
        Set<PluginContainer> set = new HashSet<>(this.getEnabledPlugins());
        set.add(PluginContainer.OPEN_BLOCK_CONTAINER);
        return new PluginContainers(set);
    }

    public void register(PluginContainer container){
        this.plugins.add(container);
    }

    public static int[] parseVersion(String version){
        String[] split = version.split("\\.");
        int[] ret = new int[4];
        for(int A = 0; A < Math.min(split.length, 4); A++){
            ret[A] = Integer.parseInt(split[A]);
        }
        return ret;
    }

    public static int compareVersion(int master, int major, int minor, int patch){
        if(VERSION[0] > master){
            return 1;
        }else if(VERSION[0] < master){
            return -1;
        }
        if(VERSION[1] > major){
            return 1;
        }else if(VERSION[1] < major){
            return -1;
        }
        if(VERSION[2] > minor){
            return 1;
        }else if(VERSION[2] < minor){
            return -1;
        }
        if(VERSION[3] > patch){
            return 1;
        }else if(VERSION[3] < patch){
            return -1;
        }
        return 0;
    }

    public static void setInstance(Blocks blocks){
        instance = blocks;
    }

    public static Blocks getInstance(){
        return instance;
    }
}
