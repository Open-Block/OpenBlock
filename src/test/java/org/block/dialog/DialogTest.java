package org.block.dialog;

import org.block.panel.block.input.OpenBlockDialog;
import org.block.panel.block.input.type.DecimalNumberDialog;

import javax.swing.*;

public class DialogTest {

    public static void main(String[] args){
        JFrame frame = new JFrame("Test Dialog");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DecimalNumberDialog panel = new DecimalNumberDialog();
        OpenBlockDialog<DecimalNumberDialog> dialog = new OpenBlockDialog<>(frame, panel);

        frame.setVisible(true);
        dialog.setVisible(true);
    }
}
