package org.block.zizi;

import org.comp.jre.DrawGraphics;
import org.comp.jre.ZiZiPanel;

import javax.swing.*;

public class TestZiZi {

    public static void main(String[] args){
        JFrame frame = new JFrame("Test");
        frame.setSize(500, 500);


        ZiZiPanel panel = new ZiZiPanel(new TestZiZiPanel());

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
