package org.block.project.block;

import org.block.Blocks;
import org.block.project.block.type.attachable.AttachableBlock;
import org.block.project.module.project.Project;
import org.block.project.panel.main.BlockRender;

import java.io.File;
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
     * If the block has a output value, then this should be implemented which will allow the Block to be added as a parameter.
     * @param <V> The expected class type of the outputted value. This can be {@link Object} if unknown and {@link Void} if no value
     */
    interface ValueBlock<V> extends Block {

        interface ConnectedValueBlock<V> extends ValueBlock<V> {

            interface MutableConnectedValueBlock<V> extends ConnectedValueBlock<V>{

                /**
                 * Sets the provided value
                 * @param value The new value
                 */
                void setValue(V value);

            }

            /**
             * Gets the value of the block
             * @return The actual value of the block
             */
            V getValue();

            @Override
            default Optional<Class<V>> getExpectedValue(){
                return Optional.of((Class<V>)getValue().getClass());
            }
        }

        /**
         * Gets the expected value type that the block returns
         * @return The expected class of the code output
         */
        Optional<Class<V>> getExpectedValue();

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


    }

    BlockGraphics getGraphicShape();

    default BlockRender getGraphicRender(){
        return new BlockRender(this);
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
     * Gets a unique id for the block.
     * This is used for checking if the block is equal as well as used for dependents when saving and loading.
     * When loaded/unloaded the block will maintain its ID
     * @return The unique id
     */
    UUID getUniqueId();

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
     * Gets the BlockType for this block, it may not be the block type that was used to create the block, however it will be of the same type.
     * @return The BlockType for the block
     */
    BlockType<? extends Block> getType();

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
     * compared using the TreeSet.
     * @param layer The new layer.
     */
    void setLayer(int layer);

    /**
     * Updates the block and all things connected to the block
     * by default, this updates the layer on the panel
     */
    default void update(){
    }

    /**
     * Deletes the block from the panel, does all the checks to remove everything correctly
     */
    default void delete(){
        this.getFile().ifPresent(File::deleteOnExit);
        /*BlockDisplayPanel panel = ((MainDisplayPanel) Blocks.getInstance().getWindow().getContentPane()).getBlocksPanel().getSelectedComponent();
        panel.unregister(this);*/
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

    default Optional<File> getFile(){
        Optional<Project.Loaded> opProject = Blocks.getInstance().getLoadedProject();
        if(opProject.isEmpty()){
            return Optional.empty();
        }
        String path = new File(this.getType().saveLocation(), this.getUniqueId().toString() + ".json").getPath();
        System.out.println("Path: " + path);
        System.out.println("File: " + opProject.get().getFile().getParentFile());
        return Optional.of(new File(opProject.get().getFile().getParentFile(), path));
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
