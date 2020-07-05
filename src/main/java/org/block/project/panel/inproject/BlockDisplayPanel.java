package org.block.project.panel.inproject;

import org.array.utils.ArrayUtils;
import org.block.project.block.Block;
import org.block.project.block.assists.BlockList;
import org.block.project.block.event.mouse.BlockMouseClickEvent;
import org.block.project.context.DragContext;
import org.block.project.exception.InvalidBlockException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class BlockDisplayPanel extends JPanel {

    private class OnMouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (BlockDisplayPanel.this.context != null){
                Block block = BlockDisplayPanel.this.context.getDragging();
                BlockDisplayPanel.this.context.getAttached().ifPresent(b -> {
                    if(b instanceof Block.AttachableBlock){
                        ((Block.AttachableBlock) b).removeAttachment(block);
                        block.removeAttachedTo();
                    }
                    BlockDisplayPanel.this.context.setAttached(null);
                });


                NavigableSet<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
                Block target = null;
                if(!blocks.isEmpty() && blocks.first().equals(block) && (blocks.size() > 1)) {
                    target = blocks.higher(block);
                }else if(!blocks.isEmpty() && !blocks.first().equals(block)) {
                    target = blocks.first();
                }
                if (target == null){
                    block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                    block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                    block.update();
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
                                if (!attachment.getAttachment(slot).isPresent() && attachment.canAcceptAttachment(slot, block)) {
                                    attachment.setAttachment(slot, block);
                                    block.setAttachedTo(target2);
                                    target2.update();
                                }else{
                                    block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                                    block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                                }
                            }catch (IllegalArgumentException ignore){
                                block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                                block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                            }
                            block.update();
                            BlockDisplayPanel.this.repaint();
                            BlockDisplayPanel.this.revalidate();
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
            if(e.getClickCount() == 1 && e.getButton() == 1) {
                Map<Block, Boolean> map = new HashMap<>();
                BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(block -> {
                    map.put(block, block.isSelected());
                });
                BlockDisplayPanel.this.getBlocks().forEach(b -> b.setSelected(false));
                map.entrySet().forEach(entry -> entry.getKey().setSelected(!entry.getValue()));
            }else if(e.getClickCount() == 1 && e.getButton() == 3){
                NavigableSet<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
                if(blocks.isEmpty()){
                    return;
                }
                blocks.first().getRightClick().show(BlockDisplayPanel.this, e.getX(), e.getY());
            }else{
                sendEvent(e);
            }
            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }

        private void sendEvent(MouseEvent e){
            BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(b -> {
                BlockMouseClickEvent event = new BlockMouseClickEvent(b, e);
                b.callEvent(event);
            });
        }

        @Override
        public void mousePressed(MouseEvent e) {
            BlockDisplayPanel.this.mouseDown = true;
            NavigableSet<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
            if(blocks.isEmpty()){
                return;
            }
            Block block = blocks.first();
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

    private TreeSet<Block> blocks = new TreeSet<>(Comparator.comparingInt(l -> l.getLayer()));
    private boolean mouseDown;
    private DragContext context;

    public BlockDisplayPanel(){
        init();
    }

    private void init(){
        this.addMouseListener(new OnMouseClick());
        this.addMouseMotionListener(new OnMouseMove());
        this.requestFocusInWindow(false);
    }

    /**
     * Updates the layers for every block found within the BlockDisplayPanel.
     * The Consumer is used for any modification to blocks with the key being the block in question and then value
     * being the layer the block is on. Modifying the value is safe to change and will take affect on a
     * {@link JPanel#repaint()} and {@link JPanel#revalidate()}
     * @param preChanges The changes to make
     */
    public void updateBlockPosition(Consumer<Map<Block, Integer>> preChanges){
        Map<Block, Integer> map = new HashMap<>();
        this.blocks.forEach(b -> map.put(b, b.getLayer()));
        preChanges.accept(map);
        List<Map.Entry<Block, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparingInt(b -> b.getValue()));
        TreeSet<Block> set = new TreeSet<>(Comparator.comparingInt(l -> l.getLayer()));
        for(int A = 0; A < entries.size(); A++){
            Map.Entry<Block, Integer> entry = entries.get(A);
            Block block = entry.getKey();
            block.setLayer(A);
            while(set.contains(block)){
                block.setLayer(block.getLayer() + 1);
            }
            set.add(block);
        }
        this.blocks = set;
    }

    public NavigableSet<Block> getBlocks(){
        return Collections.unmodifiableNavigableSet(this.blocks);
    }

    public NavigableSet<Block> getBlocks(int x, int y){
        TreeSet<Block> set = new TreeSet<>(Comparator.comparingInt(b -> ((Block)b).getLayer()).reversed());
        this.blocks.stream().filter(b -> b.contains(x, y)).forEach(b -> set.add(b));
        return set;
    }

    /**
     * Adds the block to the panel, note that if the layer is the same as another one then this block will fail
     * to be added
     * @param block The block to add
     * @return If it added or not
     */
    public boolean register(Block block){
        return this.blocks.add(block);
    }

    /**
     * Removes the block from the panel
     * @param block The block to remove
     */
    public void unregister(Block block){
        this.blocks.remove(block);
    }

    /**
     * Writes the code from the programming blocks found within the panel
     * @return The code for each class, class written in each entry
     * @throws InvalidBlockException Thrown when a block provided on the page is in a invalid state
     */
    public Set<String> writeCode() throws InvalidBlockException {
        Set<String> classes = new HashSet<>();

        //Currently only supports a single class

        Set<Block.CalledBlock.CodeStartBlock> set = new HashSet<>();

        for(Block block : this.blocks){
            Block target = block;
            while(target.getAttachedTo().isPresent()){
                target = target.getAttachedTo().get();
            }
            if(target instanceof Block.CalledBlock.CodeStartBlock){
                set.add((Block.CalledBlock.CodeStartBlock)target);
            }else{
                throw new InvalidBlockException(target, "Invalid block (X: " + target.getX() + " Y: " + target.getY() + " Type: " + target.getType().getName() + "): This block required to be put in a starting block");
            }
        }

        StringBuilder clazz = new StringBuilder();
        clazz.append("package org.openblock;\n\n");
        for(Block.CalledBlock.CodeStartBlock block : set){
            for(String impor : block.getCodeImports()){
                clazz.append("import " + impor + ";\n");
            }
        }
        clazz.append("\npublic class Main {\n\n");
        int tab = 1;
        for(Block.CalledBlock.CodeStartBlock block : set){
            block.writeBlockCode(tab).entrySet().stream().filter(e -> e.getValue() == Block.CalledBlock.METHOD).forEach(e -> {
                clazz.append(e.getKey() + "\n\n");
            });
        }
        clazz.append("}");
        classes.add(clazz.toString());
        return classes;
    }

    @Override
    public Dimension getPreferredSize(){
        if(this.blocks.isEmpty()){
            return super.getPreferredSize();
        }
        int height = ArrayUtils.getBest(b -> b.getHeight(),(x1, x2) -> x1 > x2 , this.blocks).get().getHeight();
        int width = ArrayUtils.getBest(b -> b.getWidth(),(x1, x2) -> x1 > x2 , this.blocks).get().getWidth();
        return new Dimension(height, width);
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        this.blocks.stream().forEachOrdered(b -> b.paint(graphics2D));
    }
}
