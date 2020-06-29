package org.block;

import org.block.panel.MainDisplayPanel;

import javax.swing.*;

public class FrameTest {

    public static void main(String[] args){
        Blocks blocks = new Blocks();
        Blocks.setInstance(blocks);
        JFrame frame = new JFrame("Test");
        blocks.setWindow(frame);
        frame.setContentPane(new MainDisplayPanel());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
