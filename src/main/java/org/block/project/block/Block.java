package org.block.project.block;

import org.block.Blocks;
import org.block.project.Project;
import org.block.project.block.type.called.CodeStartBlock;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * A Block is a shape that can have specific functions with code attached to it.
 * The idea of the Block is very abstract so please look at the classes that extend upon the Block interface for a more specific description
 */
public interface Block {

    /**
     * Gets the X position on the panel
     *
     * @return Gets the X position in pixels
     */
    default double getX() {
        throw new IllegalStateException("Not Implemented");
    }

    /**
     * Gets the Y position on the panel
     *
     * @return Gets the Y position in pixels
     */
    default double getY() {
        throw new IllegalStateException("Not Implemented");
    }

    /**
     * Sets the X position on the panel.
     *
     * @param x The new X position
     * @throws IndexOutOfBoundsException If the new position is less then 0
     */
    default void setX(double x){
        setPosition(x, getY());
    }

    /**
     * Sets the Y position on the panel.
     *
     * @param y the new Y position
     */
    default void setY(double y){
        setPosition(getX(), y);
    }

    default void setPosition(double x, double y){
        throw new IllegalStateException("Not Implemented");
    }

    /**
     * Gets a unique id for the block.
     * This is used for checking if the block is equal as well as used for dependents when saving and loading.
     * When loaded/unloaded the block will maintain its ID
     *
     * @return The unique id
     */
    UUID getUniqueId();

    /**
     * This writes the calling/in line code for the block.
     * The first line should not have a tab indent, however other lines should have the tab indent
     *
     * @param tabs The indent to provide
     * @return The output code
     */
    String writeCode(int tabs);

    /**
     * Provides each import required in string form, no need for "import" therefore a example would be the following "java.long.String"
     *
     * @return A collection of imports
     */
    Collection<String> getCodeImports();

    /**
     * Gets the BlockType for this block, it may not be the block type that was used to create the block, however it will be of the same type.
     *
     * @return The BlockType for the block
     */
    BlockType<? extends Block> getType();

    Optional<CodeStartBlock> getParent();

    void setParent(CodeStartBlock block);

    /**
     * Updates the block and all things connected to the block
     * by default, this updates the layer on the panel
     */
    default void update() {
    }

    /**
     * Gets the pixel height of the block.
     *
     * @return Gets the height in pixels
     */
    default double getHeight() {
        return this.getNode().getBoundsInLocal().getHeight();
    }

    /**
     * Gets the pixel width of the block
     *
     * @return Gets the width in pixels
     */
    default double getWidth() {
        return this.getNode().getBoundsInLocal().getWidth();
    }


    BlockNode<? extends Block> getNode();

    /**
     * Deletes the block from the panel, does all the checks to remove everything correctly
     */
    default void delete() {
        this.getFile().ifPresent(File::deleteOnExit);
        /*BlockDisplayPanel panel = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlocksPanel().getSelectedComponent();
        panel.unregister(this);*/
    }

    /**
     * Checks to see if the provided X and Y is contained within the Block
     *
     * @param x The X position to check
     * @param y The Y position to check
     * @return If contained, the result will be true.
     */
    default boolean contains(int x, int y) {
        if (x < getX()) {
            return false;
        }
        if (y < getY()) {
            return false;
        }
        if (x > (getX() + getWidth())) {
            return false;
        }
        return y <= (getY() + getHeight());
    }

    default Optional<File> getFile() {
        Optional<Project.Loaded> opProject = Blocks.getInstance().getLoadedProject();
        if (opProject.isEmpty()) {
            return Optional.empty();
        }
        String path = new File(this.getType().saveLocation(), this.getUniqueId().toString() + ".json").getPath();
        return Optional.of(new File(opProject.get().getFile().getParentFile(), path));
    }

    /**
     * State method for writing the init tab for lines
     *
     * @param tab the amount of tabs to place
     * @return The gap
     */
    static String tab(int tab) {
        String gap = "";
        for (int A = 0; A < tab; A++) {
            gap += "\t";
        }
        return gap;
    }
}
