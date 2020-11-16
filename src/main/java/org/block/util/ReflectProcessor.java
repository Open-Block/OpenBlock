package org.block.util;

import org.block.plugin.Plugin;
import org.block.plugin.launch.LaunchEvent;
import org.block.plugin.launch.LaunchListener;
import org.block.plugin.launch.meta.Dependent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface ReflectProcessor {

    static List<Method> getDirectMethods(Object obj) {
        return getDirect(Class::getDeclaredMethods, obj);
    }

    /**
     * Gets all the reflect objects directly specified within the class of the object
     *
     * @param function To get all of the specified
     * @param obj      The object to check
     * @param <T>      The end result
     * @return A list of all objects directly specified within the class
     */
    static <T> List<T> getDirect(Function<Class<?>, T[]> function, Object obj) {
        return Arrays.asList(function.apply(obj.getClass()));
    }

    static void fireEventsSynced(LaunchEvent event, Collection<Object> search) {
        getOrderedEventMethods(event, search).forEach((m, s) -> fireEvent(event, s, m));
    }

    static void fireEventsAsynced(LaunchEvent event, Collection<Object> search) {
        fireEventsAsynced(() -> event, (a) -> {
        }, search);
    }

    /**
     * Fires the event to all provided event classes
     * @param getter Creates a new instanceof the the event
     * @param after The code to run after the event is fired
     * @param search The class to search
     */
    static void fireEventsAsynced(Supplier<LaunchEvent> getter, Consumer<LaunchEvent> after, Collection<Object> search) {
        search.forEach(e -> new Thread(() -> {
            LaunchEvent event = getter.get();
            fireEvent(event, e);
            after.accept(event);
        }).start());
    }

    /**
     * Fires the provided event to the object
     *
     * @param event The event to fire
     * @param search Where to fire the event
     */
    static void fireEvent(LaunchEvent event, Object search) {
        getEventMethods(event, search).forEach(m -> {
            fireEvent(event, search, m);
        });
    }

    /**
     * Fires the provided event to the provided handler
     * @param event The event to fire
     * @param search The instance of the object holding the handler
     * @param handler The event handler
     */
    static void fireEvent(LaunchEvent event, Object search, Method handler){
        try {
            if (handler.getParameterCount() == 1) {
                handler.invoke(search, event);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static TreeMap<Method, Object> getOrderedEventMethods(LaunchEvent event, Collection<Object> search){
        TreeMap<Method, Object> map = new TreeMap<>((o1, o2) -> {
            Class<?> a1 = o1.getDeclaringClass();
            Class<?> a2 = o2.getDeclaringClass();
            Dependent[] d1 = o1.getAnnotation(LaunchListener.class).dependsOn();
            Dependent[] d2 = o2.getAnnotation(LaunchListener.class).dependsOn();
            Plugin p1 = a1.getAnnotation(Plugin.class);
            Plugin p2 = a2.getAnnotation(Plugin.class);
            for(Dependent dept : d1) {
                if (isDependent(dept, p2)) {
                    return 1;
                }
            }
            for(Dependent dept : d2) {
                if (isDependent(dept, p1)) {
                    return -1;
                }
            }
            return 0;
        });
        getEventMethods(event, search).entrySet().forEach(e -> e.getValue().forEach(method -> map.put(method, e.getKey())));
        return map;
    }

    /**
     * Gets all applicable methods for each class
     * @param event The event to fire
     * @param search The object to search
     * @return Every event handler for the class, Key is the class, with value being all the methods.
     */
    static Map<Object, Set<Method>> getEventMethods(LaunchEvent event, Collection<Object> search){
        Map<Object, Set<Method>> map = new HashMap<>();
        search.forEach(s -> map.put(s, getEventMethods(event, s)));
        return map;
    }

    /**
     * Gets all applicable methods
     * @param event The event to fire
     * @param search The object to search
     * @return All methods that are of criteria
     */
    static Set<Method> getEventMethods(LaunchEvent event, Object search) {
        return getDirectMethods(search)
                .stream()
                .filter(m -> m.isAnnotationPresent(LaunchListener.class))
                .filter(m -> m.getParameterCount() >= 1)
                .filter(m -> m.getParameters()[0].getType().isInstance(event)).collect(Collectors.toSet());
    }

    /**
     * Checks if the found plugin is the plugin that is being dependent on
     *
     * @param dependent The original plugin
     * @param plugin    The plugin to check against
     * @return If the plugin is the depending plugin
     */
    static boolean isDependent(Dependent dependent, Plugin plugin) {
        if(!plugin.id().equals(dependent.getId())){
            return false;
        }
        if (plugin.majorVersion() < dependent.getMinMajorVersion()) {
            return false;
        }
        if (plugin.majorVersion() > dependent.getMaxMajorVersion()) {
            return false;
        }
        if (plugin.majorVersion() == dependent.getMinMajorVersion() && plugin.minorVersion() < dependent.getMinMinorVersion()) {
            return false;
        }
        if (plugin.majorVersion() == dependent.getMaxMajorVersion() && plugin.minorVersion() > dependent.getMaxMinorVersion()) {
            return false;
        }
        if (plugin.majorVersion() == dependent.getMinMajorVersion() && plugin.minorVersion() == dependent.getMinMinorVersion() && plugin.patchVersion() < dependent.getMinPatchVersion()) {
            return false;
        }
        if (plugin.majorVersion() == dependent.getMaxMajorVersion() && plugin.minorVersion() == dependent.getMaxMinorVersion() && plugin.patchVersion() > dependent.getMaxPatchVersion()) {
            return false;
        }
        return true;
    }
}
