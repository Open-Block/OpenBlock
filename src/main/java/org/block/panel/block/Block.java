package org.block.panel.block;

import org.block.panel.block.event.BlockEvent;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * A Block is a shape that can have specific functions with code attached to it.
 * The idea of the Block is very abstract so please look at the classes that extend upon the Block interface for a more specific description
 */
public interface Block {

    /**
     * If the Block requires additional information to function properly that is in code form, then it should implement Attachable.
     * This allows users to add blocks directly to this block
     */
    interface AttachableBlock extends Block {

        /**
         * If the block is guaranteed to have only one attachment then the block should implement this instead of AttachableBlock.
         */
        interface Single extends AttachableBlock {

            /**
             * Gets the only attachment from the block
             * @return The attached block, if none are attached then it will return {@link Optional#empty()}
             */
            Optional<Block> getAttachment();

            /**
             * Sets the only attachment to the block
             * @param block The new block
             * @throws IllegalArgumentException If the block can not be accept then this will be thrown
             */
            void setAttachment(Block block);

            /**
             * Removes the only attachment from the block
             * @return The block that was removed, {@link Optional#empty()} is returned if nothing was returned
             */
            Optional<Block> removeAttachment();

            /**
             * Checks if the provided block can be attached to the block
             * @param block The block to check
             * @return if the block was acceptable or not
             */
            boolean canAcceptAttachment(Block block);

            /**
             * Gets the max amount of attachments that this block may have.
             * If unlimited attachments then it would return {@link Integer#MAX_VALUE}
             * @return The max amount of attachments this block can accept. This number will always be positive
             * @deprecated The only possible answer is 1, therefore if known it shouldn't need to be checked
             */
            @Deprecated
            @Override
            default int getMaxAttachments(){
                return 1;
            }

            /**
             * Gets the attachment in the slot provided
             * @param index The index to check
             * @return A optional of the block, if no block is found then it will be {@link Optional#empty()}
             * @throws IndexOutOfBoundsException if the provided index is out of range
             * @deprecated use {@link Single#getAttachment()} instead
             */
            @Deprecated
            @Override
            default Optional<Block> getAttachment(int index){
                if(index != 0){
                    throw new IndexOutOfBoundsException(index + " is out of range");
                }
                return this.getAttachment();
            }

            /**
             * Sets (overrides if needed) the block in its position
             * @param index The index value
             * @param block The new block
             * @throws IndexOutOfBoundsException if the provided index is out of range
             * @throws IllegalArgumentException If the block can not be accepted in that slot
             * @deprecated use {@link Single#setAttachment(Block)} instead
             */
            @Deprecated
            @Override
            default void setAttachment(int index, Block block){
                if(index != 0){
                    throw new IndexOutOfBoundsException(index + " is out of range");
                }
                this.setAttachment(block);
            }

            @Override
            default void removeAttachment(Block block){
                Optional<Block> opBlock = getAttachment();
                if(!opBlock.isPresent()){
                    return;
                }
                if(opBlock.get().equals(block)){
                    this.removeAttachment();
                }
            }

            /**
             * Removes the block in the provided slot
             * @param index The index slot to remove
             * @return The block that was removed, if it wasn't found then this will return {@link Optional#empty()}
             * @throws IndexOutOfBoundsException If the index is out of range
             * @deprecated use {@link Single#removeAttachment()} instead
             */
            @Deprecated
            @Override
            default Optional<Block> removeAttachment(int index){
                if(index != 0){
                    throw new IndexOutOfBoundsException(index + " is out of range");
                }
                return removeAttachment();
            }

            /**
             * Checks if the block can be accepted in the slot
             * @param index The index to check
             * @param block The block to check
             * @return If the block was accepted
             * @deprecated use {@link Single#canAcceptAttachment(Block)} instead
             */
            @Deprecated
            @Override
            default boolean canAcceptAttachment(int index, Block block){
                return this.canAcceptAttachment(block);
            }

            /**
             * Adds the provided block into the first free slot
             * @param block The block to add
             * @throws IllegalStateException If the block can not be accepted in any slot
             */
            default void addAttachment(Block block){
                if(this.getAttachment().isPresent()){
                    throw new IllegalStateException("Could not find a valid free slot");
                }
                if(this.canAcceptAttachment(block)) {
                    this.setAttachment(block);
                }
            }

        }

        /**
         * Gets the max amount of attachments that this block may have.
         * If unlimited attachments then it would return the current amount of attachments + 1
         * @return The max amount of attachments this block can accept. This number will always be positive
         */
        int getMaxAttachments();

        /**
         * Gets the attachment in the slot provided
         * @param index The index to check
         * @return A optional of the block, if no block is found then it will be {@link Optional#empty()}
         * @throws IndexOutOfBoundsException if the provided index is out of range
         */
        Optional<Block> getAttachment(int index);

        /**
         * Sets (overrides if needed) the block in its position
         * @param index The index value
         * @param block The new block
         * @throws IndexOutOfBoundsException if the provided index is out of range
         * @throws IllegalArgumentException If the block can not be accepted in that slot
         */
        void setAttachment(int index, Block block);

        /**
         * Removes the attachment
         * @param block The block to remove
         */
        void removeAttachment(Block block);

        /**
         * Checks if the block can be accepted in the slot
         * @param index The index to check
         * @param block The block to check
         * @return If the block was accepted
         */
        boolean canAcceptAttachment(int index, Block block);

        /**
         * Adds the provided block into the first free slot
         * @param block The block to add
         * @throws IllegalStateException If the block can not be accepted in any slot
         */
        default void addAttachment(Block block){
            for(int A = 0; A < this.getMaxAttachments(); A++){
                if(this.canAcceptAttachment(A, block)){
                    this.setAttachment(A, block);
                    return;
                }
            }
            throw new IllegalStateException("Could not find a valid free slot");
        }

        /**
         * Removes the block in the provided slot
         * @param index The index slot to remove
         * @return The block that was removed, if it wasn't found then this will return {@link Optional#empty()}
         * @throws IndexOutOfBoundsException If the index is out of range
         */
        default Optional<Block> removeAttachment(int index){
            Optional<Block> opBlock = this.getAttachment(index);
            if(!opBlock.isPresent()){
                return Optional.empty();
            }
            this.removeAttachment(opBlock.get());
            return opBlock;
        }

        /**
         * Checks if the provided block is contained within the attached set of blocks
         * @param block The block to test
         * @return If the block is contained
         */
        default boolean containsAttachment(Block block){
            for(int A = 0; A < this.getMaxAttachments(); A++){
                Optional<Block> opAttachment = this.getAttachment(A);
                if(!opAttachment.isPresent()){
                    continue;
                }
                if (opAttachment.get().equals(block)){
                    return true;
                }
            }
            return false;
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
         * @return
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
        return y <= (getY() + getHeight());
    }
}
