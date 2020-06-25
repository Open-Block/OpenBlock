package org.block.panel.section;

import org.block.panel.block.Block;
import org.block.panel.block.BlockType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DisplaySection<B extends BlockType<?>> extends JPanel implements GUISection{

    private class OnClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private final GUISection parent;
    private final String title;
    private final B block;

    public DisplaySection(GUISection parent, String title, B block){
        this.parent = parent;
        this.block = block;
        this.title = title;
        this.addMouseListener(new OnClick());
    }

    public B getBlock(){
        return this.block;
    }

    @Override
    public Optional<GUISection> getSectionsParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public List<GUISection> getSectionsChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Block block = this.block.buildDefault(5, 5);
        graphics.drawString(this.title, 0, 0);
        block.paint((Graphics2D) graphics);
    }
}
