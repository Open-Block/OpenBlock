package org.block.panel.block;


import org.block.panel.block.event.BlockEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A Block is a shape that can have specific functions with code attached to it.
 * The idea of the Block is very abstract so please look at the classes that extend upon the Block interface for a more specific description
 */
public interface Block {

    /**
     * If a Block requires more data that can be provided in a programmable way, then the Block should implement ParameterInsertBlock which is a standard method of Parameters.
     * This isn't descriptive as there is a issue which removes this and replaces it with something else.
     *
     * The main issue with this implementation is that it assumes that all parameters can be accepted but doesn't let all parameters to be accepted, resulting in a lot of errors.
     */
    interface ParameterInsertBlock extends Block {

        /**
         * Gets the max amount of parameters that the Block can accept. This can be 0.
         * If you wish to have unlimited, do {@link Integer#MAX_VALUE}
         * @return The max amount of parameters that the Block can accept
         */
        int getMaxCount();

        /**
         * Gets all the parameters the user has currently linked to the Block.
         * This also includes hovering Blocks (where the user is dragging an item which has hovered over this block and the program is suggesting the attachment).
         * @return A ordered unmodifiable list of all parameters currently linked
         */
        List<ValueBlock<?>> getCurrentParameters();

        /**
         * Adds the parameter to the next free slot.
         * Please note that the parameter is not shown until the panel is repainted.
         * @param block The block you wish to add
         * @throws IllegalArgumentException If the parameter you have given cannot be added to the slot then this will be thrown
         */
        void addParameter(ValueBlock<?> block);

        /**
         * Adds the parameter to the index value set, this will override the current block in its slot if there is one.
         * Please note that the parameter is not shown until the panel is repainted.
         * @param index The position to add to
         * @param block The block to add
         * @throws IllegalArgumentException If the parameter you have given cannot be added to the slot then this will be thrown
         */
        void addParameter(int index, ValueBlock<?> block);

        /**
         * Removes the provided parameter from the list of parameters.
         * Please note that the parameter will continue to show until the panel is repainted
         * @param block The block to remove
         */
        void removeParameter(ValueBlock<?> block);

        /**
         * Removes the parameter in the provided slot
         * Please note that the parameter will continue to show until the panel is repainted
         * @param space The slot you wish to remove
         */
        void removeParameter(int space);

        /**
         * Checks if the provided block can be accepted within the provided slot.
         * This is useful for if you need one parameter to be {@link Boolean} and the other to be {@link Integer}
         * @param slot The slot to check
         * @param block The block to check
         * @return if the provided block can be accepted within the provided slot.
         */
        boolean canAccept(int slot, ValueBlock<?> block);

        /**
         * Moves the provided block down the list.
         * @param block The block to move
         * @param spaces The amount of spaces to move down (this can be a negative number)
         * @throws IllegalArgumentException If the block can not be found
         * @throws IllegalArgumentException if the block (or a moved block from this block moving) cannot be accepted in its new slot
         */
        default void moveParameter(ValueBlock<?> block, int spaces){
            List<ValueBlock<?>> list = this.getCurrentParameters();
            int space = -1;
            for(int A = 0; A < list.size(); A++){
                if(list.get(A).equals(block)){
                    space = A;
                    break;
                }
            }
            if(space == -1){
                throw new IllegalArgumentException("Could not find block within parameter list");
            }
            int newSpace = (space + spaces) - 1;
            if(newSpace < 0){
                newSpace = 0;
            }
            if(newSpace >= list.size()){
                newSpace = list.size();
            }
            this.removeParameter(block);
            this.addParameter(newSpace, block);
        }

    }

    /**
     * If the block has a output value, then this should be implemented which will allow the Block to be added as a parameter.
     * @param <V> The expected class type of the outputted value. This can be {@link Object} if unknown and {@link Void} if no value
     */
    interface ValueBlock<V> extends Block {

        /**
         * Gets the expected value type that the block returns
         * @return The expected class of the code output
         */
        Class<V> getExpectedValue();

    }

    /**
     * This is being removed so once again not much info
     *
     * The attachable block is for Blocks that expect a single Block to be attached to it for more information
     * @param <V> The expected class type of the outputted value. See {@link ValueBlock} for more info
     */
    interface AttachableBlock<V> extends ValueBlock<V> {

        /**
         * Checks if the provided block can be attached to this block
         * @param block The block to check
         * @return If the provided block can be attached
         */
        boolean canAttach(Block block);

        /**
         * Gets the attached block.
         * @return A Optional of the attached block, if none then it will return {@link Optional#empty()}
         */
        Optional<Block> getAttached();

        /**
         * Removes the attached block if present. Please note that the panel will need to be repainted for changes visually to take affect
         */
        void removeAttached();

        /**
         * Sets (overrides if needed) the attached block. Please note that the panel will need ot be repainted for changes visually to take affect
         * @param block The new block
         * @throws IllegalArgumentException If the provided block is not accepted
         */
        void setAttached(Block block);

    }

    /**
     * Planned to remove so once again, not much information
     *
     * A SquenceBlock is a Block designed to connect multiple blocks together. This maybe for a method or a loop or something like that
     */
    interface SequenceBlock extends Block {

        /**
         * Gets all the attached blocks
         * @return Gets the Blocks in a ordered unmodified list
         */
        List<Block> getSequence();

        /**
         * Removes the provided block from the sequence
         * @param block The block to remove
         */
        void removeFromSequence(Block block);

        /**
         * Adds the provided block to the sequence
         * @param block The block to add
         */
        void addToSequence(Block block);

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
     * This will be changed. Ignore.
     */
    interface CallerBlock extends Block {

        String writeCall();

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
    Collection<BlockEvent> getEvents();

    /**
     * Register a event to the block. Note that these are not saved by default, implementations may save some
     * @param event The new event to register
     */
    void registerEvent(BlockEvent event);

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
        if(y > (getY() + getHeight())){
            return false;
        }
        return true;
    }
}
