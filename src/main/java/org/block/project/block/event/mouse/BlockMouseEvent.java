package org.block.project.block.event.mouse;

import org.block.project.block.event.BlockEvent;

public interface BlockMouseEvent extends BlockEvent {

    interface Button extends BlockMouseEvent {

        int getButton();
        int getClickCount();

    }

    int getX();
    int getScreenX();
    int getY();
    int getScreenY();
    void cancel();
}
