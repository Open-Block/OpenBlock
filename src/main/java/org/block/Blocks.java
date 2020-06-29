package org.block;

import javax.swing.*;
import java.awt.*;

public class Blocks {

    private Font font;
    private FontMetrics metrics;
    private RootPaneContainer window;

    /**
     * Init a Blocks object with default values. <br>
     * Font: SANS_SERIF<br>
     * Font Size: <br>
     * FontMetrics: Default
     */
    public Blocks(){
        this(new Font(Font.SANS_SERIF, Font.BOLD, 18));
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
     */
    public RootPaneContainer getWindow(){
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

    public static void setInstance(Blocks blocks){
        instance = blocks;
    }

    public static Blocks getInstance(){
        return instance;
    }
}
