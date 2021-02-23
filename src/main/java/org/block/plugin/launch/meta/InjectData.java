package org.block.plugin.launch.meta;

import org.block.plugin.PluginContainer;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

public enum InjectData {

    /**
     * The plugins file location
     */
    PLUGIN_LOCATION(File.class, Path.class),
    /**
     * The path to the plugins settings directory
     */
    PLUGIN_SETTINGS(File.class, Path.class),
    /**
     * The plugin container created for your plugin
     */
    PLUGIN_CONTAINER(PluginContainer.class);

    private Class<?>[] types;

    InjectData(Class<?>... types) {
        this.types = types;
    }

    /**
     * Gets the acceptable class types this implementation takes
     *
     * @return The acceptable types of classes
     */
    public Class<?>[] getAcceptableTypes() {
        return this.types;
    }

    /**
     * Applies the provided value to the provided field
     *
     * @param holder The holder of the field
     * @param field  The field found within the holder
     * @param obj    the object to set
     * @throws IllegalAccessException Shouldn't happen
     */
    public void apply(Object holder, Field field, Object obj) throws IllegalAccessException {
        if (!field.isAnnotationPresent(Inject.class)) {
            throw new IllegalArgumentException("Field does not contain @Inject");
        }
        if (!field.getAnnotation(Inject.class).as().equals(this)) {
            throw new IllegalArgumentException("Field has a different @Inject as to " + this.name());
        }
        for (Class<?> clazz : this.getAcceptableTypes()) {
            if (clazz.isInstance(obj)) {
                field.setAccessible(true);
                field.set(holder, obj);
                field.setAccessible(false);
            }
        }
    }
}
