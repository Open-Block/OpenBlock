package org.block.project.block.type.called;

import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;

import java.util.Map;

/**
 * If your code requires calls to be added into the output code project, then this should be implemented.
 *
 * Please note, if two callables are found to have the same name with the same parameters/location then
 * an exception will be thrown when outputting the code (This is not a good thing)
 */
public interface CalledBlock extends Block {

    int FIELD = 0;
    int METHOD = 1;
    int CONSTRUCTOR = 2;
    int CLASS = 3;

    /**
     * Provides the callable
     * The map should provide the code as the key with one of the provided Integer keys as its value
     * The first line should not include the tab indent however if it has more lines, then they should have the tab indent
     * @param tab The amount of space to indent
     * @return A map of all none caller code to insert
     */
    Map<String, Integer> writeBlockCode(int tab);

}