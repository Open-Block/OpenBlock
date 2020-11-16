package org.block.plugin;

import org.array.utils.ArrayUtils;
import org.block.plugin.launch.LaunchEvent;
import org.block.plugin.launch.LaunchListener;
import org.block.util.ReflectProcessor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A collection of PluginContainer's with methods for all plugin containers such as the ability to fire events
 */
public class PluginContainers extends TreeSet<PluginContainer> {

    public PluginContainers(){
    }

    public PluginContainers(Collection<PluginContainer> collection){
        super(collection);
    }



    /**
     * Gets a value from each PluginContainer as a set
     * @param function The conversion of PluginContainer to what you wish
     * @param <M> The class type of what you want
     * @return A set of what you want
     */
    public <M> Set<M> get(Function<PluginContainer, M> function){
        return ArrayUtils.convert(Collectors.toSet(), function, this);
    }

    /**
     * Gets a value for each PluginContainer as a set, the received value is expected to be a Collection
     * @param function The conversion of PluginContainer to a Collection of what you want
     * @param <M> The class type of what you want
     * @return A set of what you want
     */
    public <M> Set<M> getAll(Function<PluginContainer, Collection<M>> function){
        Set<M> set = new HashSet<>();
        this.stream().forEach(v -> set.addAll(function.apply(v)));
        return set;
    }

    /**
     * Fires a event to all plugins in parallel, this doesn't respect {@link LaunchListener#dependsOn()}
     * @param getter A getter for a new instance of the LaunchEvent
     * @param consumer Fires after the event is fired
     */
    public void fireParallelEvent(Supplier<LaunchEvent> getter, Consumer<LaunchEvent> consumer){
        ReflectProcessor.fireEventsAsynced(getter, consumer, ArrayUtils.convert(p -> p.getPlugin(), this));
    }

    /**
     * Fires a event to all plugins in sync. This respects the {@link LaunchListener#dependsOn()}
     * @param event The event to fire
     */
    public void fireEvent(LaunchEvent event){
        ReflectProcessor.fireEventsSynced(event, ArrayUtils.convert(p -> p.getPlugin(), this));
    }
}
