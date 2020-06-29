package org.block.panel.block.event;

public interface BlockListener<E extends BlockEvent> {

    Class<E> getEventClass();
    void onEvent(E event);
}
