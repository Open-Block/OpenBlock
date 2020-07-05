package org.block.project.block.input.type;

import org.block.project.block.input.PanelDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DecimalNumberDialog extends PanelDialog implements ValueDialog<Double>{

    private static class OnKey implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if(!(e.getKeyChar() == '.' || Character.isDigit(e.getKeyChar()))){
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

    public DecimalNumberDialog(){
        this(null);
    }

    public DecimalNumberDialog(Long value) {
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
    public Double getOutput() {
        return Double.parseDouble(getInteractionPanel().getText());
    }
}
