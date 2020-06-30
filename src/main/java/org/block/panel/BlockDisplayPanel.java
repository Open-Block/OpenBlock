package org.block.panel;

import org.array.utils.ArrayUtils;
import org.block.panel.block.Block;
import org.block.panel.block.assists.BlockList;
import org.block.panel.block.event.mouse.BlockMouseClickEvent;
import org.block.panel.block.java.operation.SumOperation;
import org.block.panel.block.java.value.NumberBlock;
import org.block.panel.block.java.value.number.IntegerBlock;
import org.block.panel.context.DragContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BlockDisplayPanel extends JPanel {

    private class OnMouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (BlockDisplayPanel.this.context != null){
                Block block = BlockDisplayPanel.this.context.getDragging();
                BlockDisplayPanel.this.context.getAttached().ifPresent(b -> {
                    if(b instanceof Block.AttachableBlock){
                        ((Block.AttachableBlock) b).removeAttachment(block);
                    }
                    BlockDisplayPanel.this.context.setAttached(null);
                });


                List<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
                Block target = null;
                if(!blocks.isEmpty() && blocks.get(0).equals(block) && (blocks.size() > 1)) {
                    target = blocks.get(1);
                }else if(!blocks.isEmpty() && !blocks.get(0).equals(block)) {
                    target = blocks.get(0);
                }
                if (target == null){
                    block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                    block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                }else{
                    if(target instanceof Block.AttachableBlock){
                        Block.AttachableBlock target2 = (Block.AttachableBlock) target;
                        target2.containsSection(e.getX(), e.getY()).ifPresent(s -> {
                            BlockList<Block> attachment = target2.getAttachments(s);
                            int relX = e.getX() - target2.getX();
                            int relY = e.getY() - target2.getY();
                            try {
                                attachment.removeAttachment(block);
                                int slot = attachment.getSlot(relX, relY);
                                if (!attachment.getAttachment(slot).isPresent()) {
                                    attachment.setAttachment(slot, block);
                                }
                            }catch (IllegalArgumentException ignore){

                            }
                        });
                    }
                }
            }

            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            BlockDisplayPanel.this.getBlocks().forEach(b -> b.setHighlighted(false));
            BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(b -> b.setHighlighted(true));

            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }
    }

    private class OnMouseClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 1) {
                Map<Block, Boolean> map = new HashMap<>();
                BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(block -> {
                    map.put(block, block.isSelected());
                });
                BlockDisplayPanel.this.getBlocks().forEach(b -> b.setSelected(false));
                map.entrySet().forEach(entry -> entry.getKey().setSelected(!entry.getValue()));
            }else{
                BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(b -> {
                    BlockMouseClickEvent event = new BlockMouseClickEvent(b, e);
                    b.callEvent(event);
                });
            }
            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            BlockDisplayPanel.this.mouseDown = true;
            List<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
            if(blocks.isEmpty()){
                return;
            }
            Block block = blocks.get(0);
            BlockDisplayPanel.this.context = new DragContext().setDragging(block).setOffX(block.getX() - e.getX()).setOffY(block.getY() - e.getY());
            for(Block check : BlockDisplayPanel.this.getBlocks()){
                if(check instanceof Block.AttachableBlock){
                    Block.AttachableBlock target2 = (Block.AttachableBlock) check;
                    if(target2.containsAttachment(block)){
                        BlockDisplayPanel.this.context.setAttached(target2);
                    }
                }
            }
            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            BlockDisplayPanel.this.mouseDown = false;
            BlockDisplayPanel.this.context = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private List<Block> blocks = new ArrayList<>(Arrays.asList(
            new SumOperation(200, 80),
            new SumOperation(100, 80),
            new IntegerBlock(30, 40, 100),
            new IntegerBlock(50, 40, 200),
            new IntegerBlock(40, 40, 50)));
    private boolean mouseDown;
    private DragContext context;

    public BlockDisplayPanel(){
        init();
    }

    private void init(){
        this.addMouseListener(new OnMouseClick());
        this.addMouseMotionListener(new OnMouseMove());
    }

    public List<Block> getBlocks(){
        return this.blocks;
    }

    public List<Block> getBlocks(int x, int y){
        return this.blocks.stream().filter(b -> b.contains(x, y)).collect(Collectors.toList());
    }

    public void register(Block block){
        this.blocks.add(block);
    }

    public void unregister(Block block){
        this.blocks.remove(block);
    }

    @Override
    public Dimension getPreferredSize(){
        int height = ArrayUtils.getBest(b -> b.getHeight(),(x1, x2) -> x1 > x2 , this.blocks).get().getHeight();
        int width = ArrayUtils.getBest(b -> b.getWidth(),(x1, x2) -> x1 > x2 , this.blocks).get().getWidth();
        return new Dimension(height, width);
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        this.blocks.forEach(b -> b.paint(graphics2D));
    }
}
