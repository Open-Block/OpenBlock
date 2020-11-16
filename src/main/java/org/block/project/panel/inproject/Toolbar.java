package org.block.project.panel.inproject;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.exception.InvalidBlockException;
import org.block.project.module.project.Project;
import org.block.project.panel.network.NetworkServerPanel;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.ConfigNode;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Toolbar extends JMenuBar {

    public Toolbar(){
        init();
    }

    private void init(){
        this.add(createFile());
        this.add(createNetwork());
    }

    private JMenu createNetwork(){
        JMenu menu = new JMenu("Network");
        JMenuItem host = new JMenuItem("Host");
        RootPaneContainer window = Blocks.getInstance().getWindow();
        host.addActionListener((a) -> {
            JDialog dialog = null;
            if(window instanceof Window){
                dialog = new JDialog((Window)window, Dialog.ModalityType.APPLICATION_MODAL);
            }else if(window instanceof Frame){
                dialog = new JDialog((Frame)window, Dialog.ModalityType.APPLICATION_MODAL);
            }else{
                dialog = new JDialog();
            }
            NetworkServerPanel panel = new NetworkServerPanel();
            dialog.setContentPane(panel);
            dialog.setSize(300, 500);
            dialog.setVisible(true);
        });
        menu.add(host);
        return menu;
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
            List<Block> blocks = project.getPanel().getBlocksPanel().getBlocks();
            if(blocks.isEmpty()){
                blocks = ((MainDisplayPanel)Blocks.getInstance().getWindow().getContentPane()).getBlocksPanel().getBlocks();
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
        if(type == null){
            throw new IOException("Block does not specify a type, cannot save (" + block.getClass().getName() + " - " + block.getClass().getSimpleName() + ")");
        }
        File directory = Blocks.getInstance().getLoadedProject().get().getDirectory();
        String savePath = type.saveLocation().getPath();
        File file = new File(new File(directory, savePath), block.getUniqueId().toString() + ".json");
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
                code = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlocksPanel().writeCode();
            } catch (InvalidBlockException invalidBlockException) {
                JOptionPane.showMessageDialog(this, invalidBlockException.getReason(), "Error", JOptionPane.ERROR_MESSAGE);
                invalidBlockException.getBlock().setShowingError(true);
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
