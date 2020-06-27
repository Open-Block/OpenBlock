package org.block.panel.block;

/**
 * The BlockType provides global data between all Blocks it produces.
 * The BlockType is used with the display chooser to produce new blocks with default values
 * @param <B> The Blocks class type it is connected with
 */
public interface BlockType<B extends Block> {

    /**
     * Creates a new instanceof a block with the provided position
     * @param x The X position
     * @param y The Y position
     * @return The newly created Block
     */
    B build(int x, int y);

    /**
     * Creates a new instanceof a block with the provided position.
     * This function is used in the chooser, therefore you can have different settings for the chooser version
     * if the implementation allows for it, by default it uses the {@link BlockType#build(int x, int y)}
     * @param x The X position
     * @param y The Y position
     * @return The newly created Block
     */
    default B buildDefault(int x, int y){
        return build(x, y);
    }
}
