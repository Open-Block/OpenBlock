package org.block.project.block.type;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;


/**
 * If the Block has some text to be shown then this should be implemented
 * Most blocks will implement this
 */
public interface TextBlock extends Block {

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
