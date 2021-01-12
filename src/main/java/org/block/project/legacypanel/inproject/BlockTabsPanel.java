package org.block.project.legacypanel.inproject;

import org.block.project.block.Block;
import org.block.project.exception.InvalidBlockException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BlockTabsPanel extends JTabbedPane {

    private Map<String, BlockDisplayPanel> classes = new HashMap<>();

    public BlockTabsPanel() {
        init();
    }

    private void init() {
        this.classes.put("Global", new BlockDisplayPanel());
        this.updateTabs();
    }

    public Optional<BlockDisplayPanel> getTitledComponent(String title){
        for(int A = 0; A < this.getTabCount(); A++){
            if(this.getTitleAt(A).equals(title)){
                Component at = this.getTabComponentAt(A);
                if(at == null){
                    return Optional.ofNullable(this.classes.get(title));
                }
                return Optional.of((BlockDisplayPanel) at);
            }
        }
        return Optional.empty();
    }

    @Override
    public BlockDisplayPanel getSelectedComponent() {
        return (BlockDisplayPanel)super.getSelectedComponent();
    }

    public void updateTabs(){
        for(int A = 0; A < this.getTabCount(); A++) {
            this.removeTabAt(0);
        }
        this.classes.forEach(this::addTab);
        this.repaint();
        this.revalidate();
    }

    public List<Block> getBlocks(){
        List<Block> list = new ArrayList<>();
        this.classes.values().forEach(b -> list.addAll(b.getBlocks()));
        return list;
    }

    public Set<String> writeCode() throws InvalidBlockException{
        Set<String> set = new HashSet<>();
        for(Map.Entry<String, BlockDisplayPanel> entry : this.classes.entrySet()) {
            set.add(entry.getValue().writeCode(entry.getKey()));
        }
        return set;
    }

    public void remove(Block block){
        this.classes.values().forEach(v -> v.unregister(block));
    }

    public void register(String title, BlockDisplayPanel panel){
        this.classes.put(title, panel);
    }



}
