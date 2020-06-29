package org.block.panel.block.input.type;

import org.block.panel.block.input.PanelDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WholeNumberDialog extends PanelDialog implements ValueDialog<Long>{

    private static class OnKey implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if(!Character.isDigit(e.getKeyChar())){
                e.consume();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public WholeNumberDialog(){
        this(null);
    }

    public WholeNumberDialog(Long value) {
        super(new JTextField(value == null ? "" : value.toString()));
        init();
    }

    private void init(){
        this.getInteractionPanel().addKeyListener(new OnKey());
    }

    @Override
    public JTextField getInteractionPanel() {
        return (JTextField) super.getInteractionPanel();
    }

    @Override
    public Long getOutput() {
        return Long.parseLong(getInteractionPanel().getText());
    }
}
