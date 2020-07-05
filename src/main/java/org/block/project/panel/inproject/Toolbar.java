package org.block.project.panel.inproject;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.exception.InvalidBlockException;
import org.block.project.module.project.Project;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;
import sun.applet.Main;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.NavigableSet;
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
        menu.add(createSave());
        menu.add(createExport());
        return menu;
    }

    private JMenuItem createSave(){
        JMenuItem item = new JMenuItem("Save");
        item.addActionListener((e) -> {
            Project.Loaded project = Blocks.getInstance().getLoadedProject().get();
            NavigableSet<Block> blocks = project.getPanel().getBlockPanel().getBlocks();
            if(blocks.isEmpty()){
                blocks = ((MainDisplayPanel)Blocks.getInstance().getWindow().getContentPane()).getBlockPanel().getBlocks();
            }
            blocks.parallelStream().forEach(b -> {
                try {
                    saveBlock(b);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        });
        return item;
    }

    private <B extends Block> void saveBlock(B block) throws IOException{
        BlockType<B> type = (BlockType<B>) block.getType();
        File file = new File(new File(Blocks.getInstance().getLoadedProject().get().getDirectory(), type.saveLocation().getPath()), block.getUniqueId().toString() + ".json");
        System.out.println("File: " + file.getAbsolutePath());
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        ConfigNode node = ConfigImplementation.JSON.createEmptyNode();
        type.write(node, block);
        ConfigImplementation.JSON.write(node, file.toPath());
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
