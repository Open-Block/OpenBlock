package org.block.plugin.launch;

import org.block.plugin.launch.meta.Dependent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in {@link org.block.plugin.Plugin} classes. Events found within the class require to be specified with this
 * annotation to mark them as event listener methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LaunchListener {

    Dependent[] dependsOn() default {};

}
