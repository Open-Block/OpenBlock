package org.block.panel.section;

import org.block.Blocks;
import org.block.panel.MainDisplayPanel;
import org.block.panel.block.Block;
import org.block.panel.block.BlockType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockSection extends JPanel implements GUISection {

    public class OnClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            MainDisplayPanel panel = (MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane();
            panel.getBlockPanel().register(BlockSection.this.getBlockType().build(25, 25));
            panel.repaint();
            panel.revalidate();
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

    public static class BlockPanel extends JPanel {

        private final Block block;

        public BlockPanel(Block block){
            this.block = block;
            this.setBackground(Color.BLACK);
            this.setOpaque(true);
            init();
        }

        private void init(){
            this.setPreferredSize(new Dimension(block.getWidth(), block.getHeight()));
        }

        public Block getBlock(){
            return this.block;
        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            this.block.paint((Graphics2D) graphics);
        }
    }

    private final JLabel label;
    private final BlockType<? extends Block> type;
    private final BlockPanel panel;
    private final GUISection parent;

    public BlockSection(GUISection parent, BlockType<? extends Block> block, String title){
        this.label = new JLabel(title, SwingUtilities.CENTER);
        this.panel = new BlockPanel(block.build(0, 0));
        this.type = block;
        this.parent = parent;
        init();
    }

    private void init(){
        this.addMouseListener(new OnClick());
        this.label.setBackground(Color.BLACK);
        this.label.setOpaque(true);
        this.label.setForeground(Color.WHITE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
        add(this.label, c);
        c.gridy = 1;
        c.weighty = 1.0;
        add(this.panel, c);
    }

    public BlockType<? extends Block> getBlockType(){
        return this.type;
    }

    public Block getBlock(){
        return this.panel.block;
    }

    @Override
    public Optional<GUISection> getSectionsParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public List<GUISection> getSectionsChildren() {
        return new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return this.label.getText();
    }
}
