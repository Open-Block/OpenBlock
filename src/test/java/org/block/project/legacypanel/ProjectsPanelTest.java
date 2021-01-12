package org.block.project.legacypanel;

import org.block.Blocks;

import javax.swing.*;

public class ProjectsPanelTest {

    public static void main(String[] args){
        Blocks blocks = new Blocks();
        Blocks.setInstance(blocks);

        JFrame frame = new JFrame("Test Projects");
        blocks.setWindow(frame);
        ProjectsPanel panel = new ProjectsPanel();
        ProjectsPanel.Bar bar = new ProjectsPanel.Bar();
        frame.setContentPane(panel);
        frame.setJMenuBar(bar);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}