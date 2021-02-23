package org.block.plugin;

import org.block.plugin.launch.meta.Author;
import org.block.plugin.launch.meta.Dependent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is required for a plugin to launch. When a file in the plugins folder is being checked, it is searched for a
 * class that has this annotation, that class will then be used as the main class and the project will attempt to load
 * from that class.
 * <p>
 * A single file may have multiple @Plugin's as it may wish to include multiple plugins in a single file, this is
 * completely valid, however it is recommended to split out the plugins.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {

    /**
     * Gets the ID of the plugin
     *
     * @return The ID of the plugin
     */
    String id();

    /**
     * Gets the display name of the plugin
     *
     * @return The display name of the plugin
     */
    String displayName();

    /**
     * Gets the major version of the plugin (version X.0.0)
     *
     * @return The major version of the plugin
     */
    int majorVersion();

    /**
     * Gets the minor version of the plugin (version 0.X.0)
     *
     * @return The minor version of the plugin
     */
    int minorVersion() default 0;

    /**
     * Gets the patch version of the plugin (version 0.0.X)
     *
     * @return The patch version of the plugin
     */
    int patchVersion() default 0;

    /**
     * Gets the authors of the plugin. Defaults to no authors if no authors are specified
     *
     * @return Gets the authors of the plugin
     */
    Author[] contributors() default {};

    /**
     * Checks if the plugin should be classed as a launcher, if disabled, then the plugin will not be loaded
     * nor will it appear as disabled, it would act like it was ignored, this is useful for test code
     *
     * @return If the plugin should be launched
     */
    boolean launchFrom() default true;

    /**
     * Gets all plugins that should be loaded before this plugin
     *
     * @return A array of all plugins that this plugin depends on
     */
    Dependent[] dependsOn() default {};

}
