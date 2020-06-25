package org.block.panel;

import org.block.panel.block.java.operation.SumOperation;
import org.block.panel.block.java.value.NumberBlock;
import org.block.panel.section.ConnectedSection;
import org.block.panel.section.DisplaySection;

import javax.swing.*;
import java.awt.*;

public class MainDisplayPanel extends JPanel {

    private final BlockDisplayPanel block = new BlockDisplayPanel();
    private final ChooserDisplayPanel chooser = new ChooserDisplayPanel();

    public MainDisplayPanel(){
        this.chooser.register(new DisplaySection<>(null, "Sum", new SumOperation.SumOperationType()));
        this.chooser.register(new DisplaySection<>(null, "Integer", new NumberBlock.IntegerBlockType()));
        init();
    }

    private void init(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 0.1;
        c.insets = new Insets(0, 0, 0,  4);
        c.fill = GridBagConstraints.BOTH;
        this.add(this.chooser, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        c.weightx = 1.0;
        this.add(this.block, c);
        this.setBackground(Color.DARK_GRAY);
    }

}
