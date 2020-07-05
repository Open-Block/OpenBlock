package org.block.plugin;

import org.array.utils.ArrayUtils;
import org.block.plugin.launch.LaunchEvent;
import org.block.project.module.Module;
import org.block.util.ReflectProcessor;
import org.block.util.functions.Getter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginContainers extends TreeSet<PluginContainer> {

    public PluginContainers(){
    }

    public PluginContainers(Collection<PluginContainer> collection){
        super(collection);
    }

    public <M> Set<M> get(Function<PluginContainer, M> function){
        return ArrayUtils.convert(Collectors.toSet(), function, this);
    }

    public <M> Set<M> getAll(Function<PluginContainer, Collection<M>> function){
        Set<M> set = new HashSet<>();
        this.stream().forEach(v -> set.addAll(function.apply(v)));
        return set;
    }

    public void fireParallelEvent(Getter<LaunchEvent> getter, Consumer<LaunchEvent> consumer){
        ReflectProcessor.fireEventsAsynced(getter, consumer, ArrayUtils.convert(p -> p.getPlugin(), this));
    }

    public void fireEvent(LaunchEvent event){
        ReflectProcessor.fireEventsSynced(event, ArrayUtils.convert(p -> p.getPlugin(), this));
    }
}
