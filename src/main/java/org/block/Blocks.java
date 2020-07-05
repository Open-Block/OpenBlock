package org.block;

import org.block.plugin.PluginContainer;
import org.block.plugin.PluginContainers;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Blocks {

    private Font font;
    private FontMetrics metrics;
    private RootPaneContainer window;
    private final PluginContainers plugins = new PluginContainers();

    /**
     * Init a Blocks object with default values. <br>
     * Font: SANS_SERIF<br>
     * Font Size: <br>
     * FontMetrics: Default
     */
    public Blocks(){
        this(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }

    /**
     * Init a Blocks object with specified font
     * @param font The font that all applicable text follows
     */
    public Blocks(Font font){
        this(font, new Canvas().getFontMetrics(font));
    }

    public Blocks(Font font, FontMetrics metrics){
        this.font = font;
        this.metrics = metrics;
    }

    private static Blocks instance;

    public Font getFont(){
        return this.font;
    }

    public void setFont(Font font){
        this.font = font;
    }

    /**
     * Gets the current GUI window. This maybe NULL if you attempt to get the Window before its init
     * @return The current GUI Window
     * @throws IllegalStateException If no project has loaded, this will be thrown
     */
    public RootPaneContainer getWindow(){
        if(this.window == null){
            throw new IllegalStateException("A project has not loaded yet");
        }
        return this.window;
    }

    public void setWindow(RootPaneContainer window){
        this.window = window;
    }

    public FontMetrics getMetrics(){
        return this.metrics;
    }

    public void setMetrics(FontMetrics metrics){
        this.metrics = metrics;
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
        System.out.println("Set size: " + set.size());
        return new PluginContainers(set);
    }

    public void register(PluginContainer container){
        this.plugins.add(container);
    }

    public static void setInstance(Blocks blocks){
        instance = blocks;
    }

    public static Blocks getInstance(){
        return instance;
    }
}
