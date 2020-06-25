package org.block;

import org.block.panel.MainDisplayPanel;

import javax.swing.*;

public class FrameTest {

    public static void main(String[] args){
        Blocks.setInstance(new Blocks());
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new MainDisplayPanel());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
