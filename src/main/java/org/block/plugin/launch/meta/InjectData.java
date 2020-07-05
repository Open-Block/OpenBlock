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

    InjectData(Class<?>... types){
        this.types = types;
    }

    public Class<?>[] getAcceptableTypes(){
        return this.types;
    }

    public void apply(Object holder, Field field, Object obj) throws IllegalAccessException {
        if(!field.isAnnotationPresent(Inject.class)){
            throw new IllegalArgumentException("Field does not contain @Inject");
        }
        if (!field.getAnnotation(Inject.class).as().equals(this)){
            throw new IllegalArgumentException("Field has a different @Inject as to " + this.name());
        }
        for(Class<?> clazz : this.getAcceptableTypes()){
            if(clazz.isInstance(obj)){
                field.setAccessible(true);
                field.set(holder, obj);
                field.setAccessible(false);
            }
        }
    }
}
