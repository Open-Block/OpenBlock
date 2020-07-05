package org.block.project.block.assists;

import org.block.project.block.Block;

import java.util.Optional;

/**
 * A list of blocks, this is used for attachable blocks
 * @param <T> The expected attachment type
 */
public interface BlockList<T extends Block> extends Iterable<T>{

    /**
     * If the block is guaranteed to have only one attachment then the block should implement this instead of AttachableBlock.
     */
    interface Single<T extends Block> extends BlockList<T> {

        /**
         * Gets the only attachment from the block
         * @return The attached block, if none are attached then it will return {@link Optional#empty()}
         */
        Optional<T> getAttachment();

        /**
         * Sets the only attachment to the block
         * @param block The new block
         * @throws IllegalArgumentException If the block can not be accept then this will be thrown
         */
        void setAttachment(T block);

        /**
         * Removes the only attachment from the block
         * @return The block that was removed, {@link Optional#empty()} is returned if nothing was returned
         */
        Optional<T> removeAttachment();

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
        default Optional<T> getAttachment(int index){
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
        default void setAttachment(int index, T block){
            if(index != 0){
                throw new IndexOutOfBoundsException(index + " is out of range");
            }
            this.setAttachment(block);
        }

        @Override
        default void removeAttachment(Block block){
            Optional<T> opBlock = getAttachment();
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
        default Optional<T> removeAttachment(int index){
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
        default void addAttachment(T block){
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
    Optional<T> getAttachment(int index);

    /**
     * Gets the {@link Block.AttachableBlock} that created this list in the first place
     * @return The parent block
     */
    Block.AttachableBlock getParent();

    /**
     * Gets the X pixel position relative to the parent block {@link BlockList#getParent()}
     * @param slot The slots index
     * @return The X pixel position relative to the parent block
     * @throws IndexOutOfBoundsException Slot is out of range
     */
    int getXPosition(int slot);

    /**
     * Gets the Y pixel position relative to the parent block {@link BlockList#getParent()}
     * @param slot The slots index
     * @return The Y pixel position relative to the parent block
     * @throws IndexOutOfBoundsException Slot is out of range
     */
    int getYPosition(int slot);

    /**
     * Sets (overrides if needed) the block in its position
     * @param index The index value
     * @param block The new block
     * @throws IndexOutOfBoundsException if the provided index is out of range
     * @throws IllegalArgumentException If the block can not be accepted in that slot
     */
    void setAttachment(int index, T block);

    /**
     * Gets the best slot based on the coordinates
     * @param x The X position relative to the parent block {@link BlockList#getParent()}
     * @param y The Y position relative to the parent block {@link BlockList#getParent()}
     * @return The best slot relative to the position
     * @throws IllegalArgumentException if the position is unable to provide a slot due to it being way out of bounds
     */
    int getSlot(int x, int y);

    /**
     * Gets the pixel height of the provided slot
     * @param slot The provided slot
     * @return The pixel height
     * @throws IndexOutOfBoundsException Slot is out of range
     */
    int getSlotHeight(int slot);

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
    default void addAttachment(T block){
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
    default Optional<T> removeAttachment(int index){
        Optional<T> opBlock = this.getAttachment(index);
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
    default boolean containsAttachment(T block){
        for(int A = 0; A < this.getMaxAttachments(); A++){
            Optional<T> opAttachment = this.getAttachment(A);
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