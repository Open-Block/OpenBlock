package org.block.project.block;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class ShapeTest {

    public static class PolyTest extends JPanel {

        private Collection<Polygon> poly;

        public PolyTest(Polygon... gons){
            this(Arrays.asList(gons));
        }

        public PolyTest(Collection<Polygon> collection){
            this.poly = collection;
        }

        @Override
        public void paint(Graphics graphics){
            Random random = new Random();
            this.poly.forEach(p -> {
                Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                graphics.setColor(color);
                graphics.fillPolygon(p);
            });
        }

    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new PolyTest(Shapes.drawAttachableConnector(200, 200, 50, 50), Shapes.drawAttachingConnector(200, 200, 50, 50)));
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
