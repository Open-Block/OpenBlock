package org.block;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Blocks {

    private Font font;
    private FontMetrics metrics;

    public Blocks(){
        this(new Font(Font.SANS_SERIF, Font.BOLD, 18));
    }

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
