package org.block.panel.block.input;

import javax.swing.*;
import java.awt.*;

public class PanelDialog extends JPanel {

    private Component interactionPanel;
    private JButton cancelButton;
    private JButton acceptButton;

    public PanelDialog(Component panel){
        this.interactionPanel = panel;
        this.cancelButton = new JButton("Cancel");
        this.acceptButton = new JButton("Accept");
        init();
    }

    private void init(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.interactionPanel, c);
        c.gridwidth = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        this.add(this.cancelButton, c);
        c.gridx = 1;
        this.add(this.acceptButton, c);
        this.cancelButton.addActionListener((e) -> {
            SwingUtilities.getWindowAncestor(this).dispose();
        });

    }

    public JButton getCancelButton(){
        return this.cancelButton;
    }

    public JButton getAcceptButton(){
        return this.acceptButton;
    }

    public Component getInteractionPanel(){
        return this.interactionPanel;
    }


}
