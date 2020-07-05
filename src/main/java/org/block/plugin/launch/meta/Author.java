package org.block.plugin.launch.meta;

public @interface Author {

    String[] contributions();
    String displayName();
    String firstName() default "";
    String lastName() default "";
}
