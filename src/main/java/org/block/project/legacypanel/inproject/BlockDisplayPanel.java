package org.block.project.legacypanel.inproject;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.assists.BlockList;
import org.block.project.block.event.BlockEvent;
import org.block.project.block.event.mouse.BlockMouseClickEvent;
import org.block.project.context.DragContext;
import org.block.project.exception.InvalidBlockException;
import org.block.project.section.SpecificSection;
import org.block.util.OrderedUniqueList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class BlockDisplayPanel extends JPanel {

    private class ContainsSectionConsumer<B extends Block> implements Consumer<String>{

        private final Block.AttachableBlock target;
        private final B block;
        private final MouseEvent e;

        public ContainsSectionConsumer(B block, Block.AttachableBlock target, MouseEvent event){
            this.target = target;
            this.block = block;
            this.e = event;
        }

        @Override
        public void accept(String s) {
            BlockList<B> attachment = this.target.getAttachments(s);
            int relX = this.e.getX() - this.target.getX();
            int relY = this.e.getY() - this.target.getY();
            try {
                attachment.removeAttachment(this.block);
                int slot = attachment.getSlot(relX, relY);
                if (!attachment.getAttachment(slot).isPresent() && attachment.canAcceptAttachment(slot, this.block)) {
                    attachment.setAttachment(slot, this.block);
                    this.block.setAttachedTo(this.target);
                    target.update();
                } else {
                    block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                    block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                }
            } catch (IllegalArgumentException ignore) {
                block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
            }
            block.update();
            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }
    }

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


                OrderedUniqueList<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
                Block target = null;
                if(!blocks.isEmpty() && blocks.get(0).equals(block) && (blocks.size() > 1)) {
                    target = blocks.get(1);
                }else if(!blocks.isEmpty() && !blocks.get(0).equals(block)) {
                    target = blocks.get(0);
                }
                if (target == null){
                    block.setX(e.getX() + BlockDisplayPanel.this.context.getOffX());
                    block.setY(e.getY() + BlockDisplayPanel.this.context.getOffY());
                    block.update();
                }else{
                    if(target instanceof Block.AttachableBlock){
                        Block.AttachableBlock target2 = (Block.AttachableBlock) target;
                        target2.containsSection(e.getX(), e.getY()).ifPresent(new ContainsSectionConsumer<>(block, target2, e));
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
                BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(block -> map.put(block, block.isSelected()));
                BlockDisplayPanel.this.getBlocks().forEach(b -> b.setSelected(false));
                boolean newRegister = false;
                SpecificSection section = null;
                Optional<SpecificSection> opSection = Blocks.getInstance().getLoadedProject().get().getPanel().getChooserPanel().getSpecificSection();
                if(opSection.isPresent()){
                    section = opSection.get();
                }else{
                    section = new SpecificSection(null, "Temp", Collections.emptyList());
                    newRegister = true;
                }
                section.unregisterAll();
                final SpecificSection finalSection = section;
                map.entrySet().stream().filter(entry -> !entry.getValue()).filter(entry -> entry.getKey() instanceof Block.SpecificSectionBlock).findAny().ifPresent(entry -> {
                    Block b = entry.getKey();
                    if(b instanceof Block.TextBlock) {
                        finalSection.setTitle(((Block.TextBlock)b).getText());
                    }
                    ((Block.SpecificSectionBlock)b).getUniqueSections(finalSection).forEach(finalSection::register);
                });
                if(newRegister) {
                    Blocks.getInstance().getLoadedProject().get().getPanel().getChooserPanel().register(finalSection);
                    Blocks.getInstance().getLoadedProject().get().getPanel().getChooserPanel().updatePanel();
                }
                map.forEach((key, value) -> key.setSelected(!value));
            }else if(e.getClickCount() == 1 && e.getButton() == 3){
                OrderedUniqueList<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
                if(blocks.isEmpty()){
                    return;
                }
                blocks.get(0).getRightClick().show(BlockDisplayPanel.this, e.getX(), e.getY());
            }else{
                sendEvent(e);
            }
            BlockDisplayPanel.this.repaint();
            BlockDisplayPanel.this.revalidate();
        }

        public void sendEvent(MouseEvent e){
            BlockDisplayPanel.this.getBlocks(e.getX(), e.getY()).forEach(b -> {
                BlockMouseClickEvent event = new BlockMouseClickEvent(b, e);
                BlockDisplayPanel.this.sendEvent(event);
            });

        }

        @Override
        public void mousePressed(MouseEvent e) {
            BlockDisplayPanel.this.mouseDown = true;
            OrderedUniqueList<Block> blocks = BlockDisplayPanel.this.getBlocks(e.getX(), e.getY());
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

    private OrderedUniqueList<Block> blocks = new OrderedUniqueList<>(Comparator.comparingInt(Block::getLayer));
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
        entries.sort(Comparator.comparingInt(Map.Entry::getValue));
        OrderedUniqueList<Block> set = new OrderedUniqueList<>(Comparator.comparingInt(Block::getLayer));
        for(int A = 0; A < entries.size(); A++){
            Map.Entry<Block, Integer> entry = entries.get(A);
            Block block = entry.getKey();
            block.setLayer(A);
            while(set.contains(block)){
                block.setLayer(block.getLayer() + 1);
            }
            set.add(block);
        }
        System.out.println("Resetting blocks");
        this.blocks = set;
    }

    public <E extends BlockEvent> E sendEvent(E event){
        BlockDisplayPanel.this.getBlocks().forEach(b -> {
            b.callEvent(event);
        });
        return event;
    }

    public OrderedUniqueList<Block> getBlocks(){
        return this.blocks;
    }

    public OrderedUniqueList<Block> getBlocks(int x, int y){
        OrderedUniqueList<Block> set = new OrderedUniqueList<>(Comparator.comparingInt(Block::getLayer).reversed());
        this.getBlocks().stream().filter(b -> b.contains(x, y)).forEach(set::add);
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


    public String writeCode(String className) throws InvalidBlockException {
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
                clazz.append("import ").append(impor).append(";\n");
            }
        }
        clazz.append("\npublic class ").append(className).append(" {\n\n");
        int tab = 1;
        for(Block.CalledBlock.CodeStartBlock block : set){
            block.writeBlockCode(tab).entrySet().stream().filter(e -> e.getValue() == Block.CalledBlock.METHOD).forEach(e -> {
                clazz.append(e.getKey()).append("\n\n");
            });
        }
        clazz.append("}");
        return clazz.toString();
    }

    @Override
    public Dimension getPreferredSize(){
        if(this.blocks.isEmpty()){
            return super.getPreferredSize();
        }

        Block heightBlock = ArrayUtils.getBest(Block::getHeight,(x1, x2) -> x1 > x2 , this.blocks).get();
        Block widthBlock = ArrayUtils.getBest(Block::getWidth,(x1, x2) -> x1 > x2 , this.blocks).get();
        return new Dimension(widthBlock.getX() + widthBlock.getWidth(), heightBlock.getY() + heightBlock.getHeight());
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        for(Block block : this.getBlocks()){
            block.paint(graphics2D);
        }
    }
}
