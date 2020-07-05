package org.block.project.panel.inproject;

import org.block.Blocks;
import org.block.project.exception.InvalidBlockException;

import javax.swing.*;
import java.util.Set;

public class Toolbar extends JMenuBar {

    public Toolbar(){
        init();
    }

    private void init(){
        this.add(createFile());
    }

    private JMenu createFile(){
        JMenu menu = new JMenu("File");
        menu.add(createExport());
        return menu;
    }

    private JMenuItem createExport(){
        JMenuItem item = new JMenuItem("Export");
        item.addActionListener((e) -> {
            Set<String> code = null;
            try {
                code = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlockPanel().writeCode();
            } catch (InvalidBlockException invalidBlockException) {
                invalidBlockException.printStackTrace();
                return;
            }
            for(String clazz : code){
                System.out.println(clazz);
            }
        });
        return item;
    }
}
