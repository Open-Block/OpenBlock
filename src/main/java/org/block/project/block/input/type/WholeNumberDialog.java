package org.block.project.block.input.type;

import org.block.project.block.input.PanelDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class WholeNumberDialog<T extends Number> extends PanelDialog implements ValueDialog<T>{

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

    public static class IntegerDialog extends WholeNumberDialog<Integer> {

        public IntegerDialog(){

        }

        public IntegerDialog(Integer value){
            super(value);
        }

        @Override
        public Integer getOutput() {
            return Integer.parseInt(this.getInteractionPanel().getText());
        }
    }

    public WholeNumberDialog(){
        this(null);
    }

    public WholeNumberDialog(T value) {
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
}
