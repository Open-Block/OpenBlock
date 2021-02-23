package org.block.project.block.type.value;

import java.util.function.Consumer;

public interface MutableConnectedValueBlock<V> extends ConnectedValueBlock<V> {

    /**
     * Displays the dialog to set the value of the block
     */
    void showValueDialog(Consumer<V> consumer);

    /**
     * Sets the provided value
     *
     * @param value The new value
     */
    void setValue(V value);

}