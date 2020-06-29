package org.block.panel.block.input.type;

import org.block.panel.block.input.PanelDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CharDialog extends PanelDialog implements ValueDialog<Character> {

    private class OnKey implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            String text = CharDialog.this.getInteractionPanel().getText();
            if(text.length() == 1){
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

    public CharDialog() {
        super(new JTextField());
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
    public Character getOutput() {
        String text = this.getInteractionPanel().getText();
        if(text.length() == 0){
            return ' ';
        }
        return text.charAt(0);
    }
}
