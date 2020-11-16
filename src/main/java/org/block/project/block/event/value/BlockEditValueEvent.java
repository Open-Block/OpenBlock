package org.block.project.block.event.value;

import org.block.project.block.Block;
import org.block.project.block.event.BlockEvent;

import javax.swing.*;

public class BlockEditValueEvent implements BlockEvent.Cancelable {

    private Block block;
    private JPanel edit;
    private boolean isCancelled;

    public BlockEditValueEvent(Block block, JPanel panel){
        this.block = block;
        this.edit = panel;
    }

    public void setEditPanel(JPanel panel){
        this.edit = edit;
    }

    public JPanel getEditPanel(){
        return this.edit;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean check) {
        this.isCancelled = check;
    }
}
