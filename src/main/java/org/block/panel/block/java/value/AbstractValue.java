package org.block.panel.block.java.value;

import org.block.Blocks;
import org.block.panel.block.AbstractBlock;
import org.block.panel.block.Block;
import org.block.panel.block.Shapes;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Function;

public abstract class AbstractValue<V> extends AbstractBlock implements Block.ValueBlock<V> {

    private Function<V, String> toString;
    private V value;
    private int marginX = 2;
    private int marginY = 2;


    public AbstractValue(int x, int y, V value, Function<V, String> toString) {
        super(x, y, 10, 10, "");
        this.toString = toString;
        this.setValue(value);
    }

    public V getValue(){
        return this.value;
    }

    public void setValue(V value){
        this.value = value;
        this.text = this.toString.apply(value);
        int width = Blocks
                .getInstance()
                .getMetrics()
                .stringWidth(this.text);
        this.height = Blocks.getInstance().getFont().getSize() + this.marginY;
        this.width = width + this.marginX + Shapes.ATTACHABLE_WIDTH;
        if(this.height < Shapes.ATTACHABLE_HEIGHT){
            this.height = Shapes.ATTACHABLE_HEIGHT;
        }
    }

    @Override
    public Class<V> getExpectedValue() {
        return (Class<V>)this.value.getClass();
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        Polygon attachable = Shapes.drawAttachableConnector(getX(), getY(), Shapes.ATTACHABLE_WIDTH, Shapes.ATTACHABLE_HEIGHT);

        Color outline = null;
        if(this.isHighlighted() && this.isSelected()){
            outline = new Color(0, 255, 0);
        }else if(this.isSelected()){
            outline = new Color(255, 0, 0);
        }else if(this.isHighlighted()){
            outline = new Color(0, 0, 255);
        }
        if(outline != null){
            graphics2D.setColor(outline);
            graphics2D.fillRect(getX() + Shapes.ATTACHABLE_WIDTH + this.marginX, getY() + this.marginY, (getWidth() + this.marginX), (getHeight() + this.marginY));
        }
        graphics2D.setColor(new Color(255, 255, 0));
        graphics2D.fillPolygon(attachable);
        graphics2D.fillRect(getX() + Shapes.ATTACHABLE_WIDTH, getY(), getWidth(), getHeight());
        graphics2D.setColor(new Color(0,0,0));
        graphics2D.setFont(Blocks.getInstance().getFont());
        graphics2D.drawString(getText(), getX() + Shapes.ATTACHABLE_WIDTH + this.marginX, (getY() + Blocks.getInstance().getFont().getSize()) - this.marginY);
    }
}
