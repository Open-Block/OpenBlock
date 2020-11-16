package org.block.project.panel.inproject;

import javax.swing.*;
import java.awt.*;

public class MainDisplayPanel extends JPanel {

    private final BlockTabsPanel blocks = new BlockTabsPanel();
    private final ChooserDisplayPanel chooser = new ChooserDisplayPanel();

    public MainDisplayPanel(){
        init();
    }

    private void init(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 0.0;
        c.insets = new Insets(0, 0, 0,  4);
        c.fill = GridBagConstraints.BOTH;
        this.add(this.chooser, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        c.weightx = 1.0;
        this.add(this.blocks, c);
        this.setBackground(Color.DARK_GRAY);
    }

    /**
     * Gets the BlockDisplayPanel section of this panel
     * @return The BlockDisplayPanel
     */
    public BlockTabsPanel getBlocksPanel(){
        return this.blocks;
    }

    public ChooserDisplayPanel getChooserPanel(){
        return this.chooser;
    }

}
