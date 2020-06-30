package org.block.panel.block;

import org.block.panel.block.assists.BlockList;
import org.block.panel.block.event.BlockEvent;
import org.block.panel.block.event.BlockListener;

import java.awt.*;
import java.util.*;

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
         * Removes a block from all attached list
         * @param block The block to remove
         */
        default void removeAttachment(Block block){
            for(String section : this.getSections()){
                this.getAttachments(section).removeAttachment(block);
            }
        }
    }

    /**
     * If the block has a output value, then this should be implemented which will allow the Block to be added as a parameter.
     * @param <V> The expected class type of the outputted value. This can be {@link Object} if unknown and {@link Void} if no value
     */
    interface ValueBlock<V> extends Block {

        interface ConnectedValueBlock<V> extends ValueBlock<V>{

            /**
             * Gets the value of the block
             * @return The actual value of the block
             */
            V getValue();

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
         * Provides the callables
         * The map should provide the code as the key with one of the provided Integer keys as its value
         * @return A map of all none caller code to insert
         */
        Map<String, Integer> writeBlockCode();

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
     * Gets the attached events
     * @return Gets a unordered unmodifiable list of BlockEvent
     */
    Collection<BlockListener<? extends BlockEvent>> getEvents();

    /**
     * Register a event to the block. Note that these are not saved by default, implementations may save some
     * @param event The new event to register
     */
    void registerEventListener(BlockListener<? extends BlockEvent> event);

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
     * @return The output code
     */
    String writeCode();

    /**
     * Gets the BlockType for this block, it may not be the block type that was used to create the block, however it will be of the same type.
     * @return The BlockType for the block
     */
    BlockType<?> getType();

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
            ((BlockListener<B>)b).onEvent(event);
        });
    }
}
