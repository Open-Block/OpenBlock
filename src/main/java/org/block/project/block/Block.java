package org.block.project.block;

import org.block.Blocks;
import org.block.project.panel.inproject.BlockDisplayPanel;
import org.block.project.panel.inproject.MainDisplayPanel;
import org.block.project.block.assists.BlockList;
import org.block.project.block.event.BlockEvent;
import org.block.plugin.event.EventListener;
import org.block.project.block.input.OpenBlockDialog;
import org.block.project.block.input.PanelDialog;
import org.block.project.block.input.type.ValueDialog;
import org.block.project.section.GUISection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * A Block is a shape that can have specific functions with code attached to it.
 * The idea of the Block is very abstract so please look at the classes that extend upon the Block interface for a more specific description
 */
public interface Block {

    /**
     * If a block is directly affected by another block then the former should implement this.
     */
    interface LinkedBlock extends Block {

        /**
         * Gets the linked block
         * @return A optional of the linked block, if no link is defined then {@link Optional#empty()} will be returned
         */
        Optional<Block> getLinkedBlock();

    }

    /**
     * If the Block requires additional information to function properly that is in code form, then it should implement Attachable.
     * This allows users to add blocks directly to this block
     */
    interface AttachableBlock extends Block {

        interface StatementBlock extends AttachableBlock {

        }

        /**
         * Gets all attached block of the the provided section
         * @param section The section to get
         * @param <B> The expected block, this isn't defined when called, specify in the variable or use &lt; &gt; before the call
         * @return The block list
         * @throws IllegalArgumentException thrown if section does not exist
         */
        <B extends Block> BlockList<B> getAttachments(String section);

        /**
         * Gets all sections supported by the block
         * @return A unmodifiable collection of sections
         */
        Collection<String> getSections();

        /**
         * Gets the section based upon the provided X and Y
         * @param x The X position
         * @param y The Y position
         * @return The section, if no section can be found will return {@link Optional#empty()}
         */
        Optional<String> containsSection(int x, int y);

        /**
         * Checks all sections to see if the block is attached
         * @param block The block to check
         * @return if the block is attached
         */
        default boolean containsAttachment(Block block){
            for(String section : this.getSections()){
                if(this.getAttachments(section).containsAttachment(block)){
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks if the provided block can be attached to this block in any way
         * @param block The block to check
         * @return All possible BlockList's the block can attach to
         */
        default Collection<BlockList<? extends Block>> canAttach(Block block){
            Set<BlockList<? extends Block>> set = new HashSet<>();
            for(String section : this.getSections()){
                BlockList<?> list = this.getAttachments(section);
                for(int A = 0; A < list.getMaxAttachments(); A++){
                    if(list.canAcceptAttachment(A, block)){
                        set.add(list);
                        break;
                    }
                }
            }
            return Collections.unmodifiableCollection(set);
        }

        /**
         * Removes a block from all attached list
         * @param block The block to remove
         */
        default void removeAttachment(Block block){
            for(String section : this.getSections()){
                this.getAttachments(section).removeAttachment(block);
            }
        }

        @Override
        default void delete() {
            Block.super.delete();
            for(String section : this.getSections()){
                BlockList<? extends Block> list = this.getAttachments(section);
                for(int A = 0; A < list.getMaxAttachments(); A++){
                    list.getAttachment(A).ifPresent(b -> b.delete());
                }
            }
        }

        @Override
        default void update() {
            int x = this.getX();
            int y = this.getY();
            for(String section : this.getSections()) {
                BlockList<? extends Block> b = this.getAttachments(section);
                for (int A = 0; A < b.getMaxAttachments(); A++) {
                    final int B = A;
                    b.getAttachment(A).ifPresent(block -> {
                        block.setX(x + b.getXPosition(B));
                        block.setY(y + b.getYPosition(B));
                        block.setLayer(this.getLayer() + 1);
                        block.update();
                    });
                }
            }
            Block.super.update();

        }
    }

    /**
     * If the block has a output value, then this should be implemented which will allow the Block to be added as a parameter.
     * @param <V> The expected class type of the outputted value. This can be {@link Object} if unknown and {@link Void} if no value
     */
    interface ValueBlock<V> extends Block {

        interface ConnectedValueBlock<V> extends ValueBlock<V> {

            interface MutableConnectedValueBlock<V> extends ConnectedValueBlock<V>{

                /**
                 * Creates the dialog to change this value
                 * @return The value dialog to change this value
                 */
                ValueDialog<V> createDialog();

                /**
                 * Sets the provided value
                 * @param value The new value
                 */
                void setValue(V value);

                @Override
                default JPopupMenu getRightClick(){
                    JPopupMenu menu = ConnectedValueBlock.super.getRightClick();
                    JMenuItem edit = new JMenuItem("Edit Value");
                    edit.addActionListener((e1) -> {
                        PanelDialog panel = (PanelDialog) this.createDialog();
                        OpenBlockDialog<? extends Container> dialog = new OpenBlockDialog<>((Window)Blocks.getInstance().getWindow(), panel);
                        panel.getAcceptButton().addActionListener((e) -> {
                            this.setValue((V)((ValueDialog<? extends Number>)panel).getOutput());
                            dialog.dispose();

                        });
                        dialog.setVisible(true);
                    });
                    menu.add(edit);
                    return menu;
                }

            }

            /**
             * Gets the value of the block
             * @return The actual value of the block
             */
            V getValue();

            @Override
            default Class<V> getExpectedValue(){
                return (Class<V>)getValue().getClass();
            }
        }

        /**
         * Gets the expected value type that the block returns
         * @return The expected class of the code output
         */
        Class<V> getExpectedValue();

    }

    /**
     * If the Block has some text to be shown then this should be implemented
     * Most blocks will implement this
     */
    interface TextBlock extends Block {

        /**
         * Gets the text that is being shown.
         * @return The text in string form
         */
        String getText();

        /**
         * Sets the text that is being shown. Please note that the panel needs to be repainted for changes to take affect
         * @param text The text to show
         */
        void setText(String text);

    }

    /**
     * If your code requires calls to be added into the output code project, then this should be implemented.
     *
     * Please note, if two callables are found to have the same name with the same parameters/location then
     * an exception will be thrown when outputting the code (This is not a good thing)
     */
    interface CalledBlock extends Block {

        int FIELD = 0;
        int METHOD = 1;
        int CONSTRUCTOR = 2;
        int CLASS = 3;

        /**
         * If the code provided is a method, constructor
         */
        interface CodeStartBlock extends CalledBlock {

            @Override
            default String writeCode(int tab){
                return "";
            }
        }

        /**
         * Provides the callable
         * The map should provide the code as the key with one of the provided Integer keys as its value
         * The first line should not include the tab indent however if it has more lines, then they should have the tab indent
         * @param tab The amount of space to indent
         * @return A map of all none caller code to insert
         */
        Map<String, Integer> writeBlockCode(int tab);

    }

    /**
     * If the block has specific sections to add to the chooser, then it requires to implement this.
     */
    interface SpecificSectionBlock extends Block {

        /**
         * Gets a list of the sections.
         * @return A unmodifiable list of sections
         */
        List<GUISection> getUniqueSections();

    }

    /**
     * Gets the pixel height of the block.
     * @return Gets the height in pixels
     */
    int getHeight();

    /**
     * Gets the pixel width of the block
     * @return Gets the width in pixels
     */
    int getWidth();

    /**
     * Gets the X position on the panel
     * @return Gets the X position in pixels
     */
    int getX();

    /**
     * Gets the Y position on the panel
     * @return Gets the Y position in pixels
     */
    int getY();

    /**
     * Sets the X position on the panel.
     * @param x The new X position
     * @throws IndexOutOfBoundsException If the new position is less then 0
     */
    void setX(int x);

    /**
     * Sets the Y position on the panel.
     * @param y the new Y position
     */
    void setY(int y);

    /**
     * Checks if the block is currently selected.
     * @return If the Block is selected
     */
    boolean isSelected();

    /**
     * Sets if the block is selected. Please note that the panel needs to be repainted for visual changes to take affect.
     * @param selected Sets if the block is selected
     */
    void setSelected(boolean selected);

    /**
     * Checks if the block is highlighted.
     * @return If the block is highlighted
     */
    boolean isHighlighted();

    /**
     * Sets if the block is highlighted. Please note that the panel needs ot be repainted for visual changes to take affect.
     * @param selected If the block will be selected
     */
    void setHighlighted(boolean selected);

    /**
     * Reports the block as having an error. Only the paint implementation should need to check this
     * @return Is showing an error
     */
    boolean isShowingError();

    /**
     * Sets if the block should be showing an error, the blocks own implementation should not be calling this
     * @param error If the block has an error.
     */
    void setShowingError(boolean error);

    /**
     * Gets the attached events
     * @return Gets a unordered unmodifiable list of BlockEvent
     */
    Collection<EventListener<? extends BlockEvent>> getEvents();

    /**
     * Register a event to the block. Note that these are not saved by default, implementations may save some
     * @param event The new event to register
     */
    void registerEventListener(EventListener<? extends BlockEvent> event);

    /**
     * Gets a unique id for the block.
     * This is used for checking if the block is equal as well as used for dependents when saving and loading.
     * When loaded/unloaded the block will maintain its ID
     * @return The unique id
     */
    UUID getUniqueId();

    /**
     * This draws the block to the provided graphics in a Vector like painting style
     * @param graphics2D The vector rendering local
     */
    void paint(Graphics2D graphics2D);

    /**
     * This writes the calling/in line code for the block.
     * The first line should not have a tab indent, however other lines should have the tab indent
     * @param tabs The indent to provide
     * @return The output code
     */
    String writeCode(int tabs);

    /**
     * Provides each import required in string form, no need for "import" therefore a example would be the following "java.long.String"
     * @return A collection of imports
     */
    Collection<String> getCodeImports();

    /**
     * Gets the block that this block is attached to
     * @return The block this is attached to, if none then {@link Optional#empty} will be returned
     */
    Optional<AttachableBlock> getAttachedTo();

    /**
     * Sets the block that this is attached to, to remove the block it is currently attached to provide 'null' (however it is prefered to use {@link Block#removeAttachedTo()} just in case this changes)
     * @param block The block that is attaching this
     */
    void setAttachedTo(Block.AttachableBlock block);

    /**
     * Gets the BlockType for this block, it may not be the block type that was used to create the block, however it will be of the same type.
     * @return The BlockType for the block
     */
    BlockType<?> getType();

    /**
     * This is the layer to show on the panel, the higher the layer then the more blocks this block will appear ontop of.
     * By default the layer should be 0, with the implementation that built the block then setting it the value to the highest current layer.
     * The layer will be changed based on other blocks being selected, hovered, dragged, etc however this will be done on the panels implementation.
     * This just needs to store the value
     * @return The current layer the block is on.
     */
    int getLayer();

    /**
     * Sets the layer to show on the panel, the higher the layer then the more blocks this block will appear ontop of.
     * Note. This value should not be changed while the block is on the BlockDisplayPanel as the layer is what is
     * compared using the TreeSet. Use {@link BlockDisplayPanel#updateBlockPosition(Consumer)} to update the layer.
     * @param layer The new layer.
     */
    void setLayer(int layer);

    /**
     * Updates the block and all things connected to the block
     * by default, this updates the layer on the panel
     */
    default void update(){
        //This is bugged, it will sometimes delete the block from the panel
        //((MainDisplayPanel)Blocks.getInstance().getWindow().getContentPane()).getBlockPanel().updateBlockPosition(this);
    }

    /**
     * Deletes the block from the panel, does all the checks to remove everything correctly
     */
    default void delete(){
        this.removeAttachedTo();
        BlockDisplayPanel panel = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlockPanel();
        panel.unregister(Block.this);
    }

    /**
     * Removes the definition of this block being attached to another
     */
    default void removeAttachedTo(){
        this.getAttachedTo().ifPresent(a -> {
            a.removeAttachment(this);
        });
        this.setAttachedTo(null);
    }

    /**
     * Checks to see if the provided X and Y is contained within the Block
     * @param x The X position to check
     * @param y The Y position to check
     * @return If contained, the result will be true.
     */
    default boolean contains(int x, int y){
        if(x < getX()){
            return false;
        }
        if(y < getY()){
            return false;
        }
        if(x > (getX() + getWidth())){
            return false;
        }
        return y <= (getY() + getHeight());
    }

    /**
     * Sends a BlockEvent to the block, all listeners which react to the provided event will be fired
     * @param event The event to fire
     * @param <B> The event type
     */
    default <B extends BlockEvent> void callEvent(B event){
        getEvents().stream().filter(l -> l.getEventClass().isInstance(event)).forEach(b -> {
            ((EventListener<B>)b).onEvent(event);
        });
    }

    /**
     * Gets the Popup menu options for when the user right clicks
     * @return The JPopupMenu options for when the user right clicks
     */
    default JPopupMenu getRightClick(){
        JPopupMenu menu = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener((e) -> {
            BlockDisplayPanel panel = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlockPanel();
            this.delete();
            panel.repaint();
            panel.revalidate();
        });
        menu.add(delete);
        return menu;
    }

    /**
     * State method for writing the init tab for lines
     * @param tab the amount of tabs to place
     * @return The gap
     */
    static String tab(int tab){
        String gap = "";
        for(int A = 0; A < tab; A++){
            gap += "\t";
        }
        return gap;
    }
}
