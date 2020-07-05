package org.block.project.block.input.type;

import org.block.project.block.input.PanelDialog;

import javax.swing.*;

public class StringDialog extends PanelDialog implements ValueDialog<String>{

    public StringDialog() {
        super(new JTextField());
    }

    @Override
    public JTextField getInteractionPanel() {
        return (JTextField) super.getInteractionPanel();
    }

    @Override
    public String getOutput() {
        return this.getInteractionPanel().getText();
    }
}
