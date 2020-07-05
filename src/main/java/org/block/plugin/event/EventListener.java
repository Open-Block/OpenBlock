package org.block.plugin.event;


public interface EventListener<E extends Event> {

    Class<E> getEventClass();
    void onEvent(E event);
}
