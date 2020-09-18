package org.block.plugin.launch.meta;

/**
 * All information about a author
 */
public @interface Author {

    /**
     * Gets the contributions that the author has done
     * @return A array of each contribution
     */
    String[] contributions();

    /**
     * Gets the display name to provide for this author
     * @return The display name of this author
     */
    String displayName();

    /**
     * If the author wants to display their name, then this is the first name
     * @return The first name of the author
     */
    String firstName() default "";

    /**
     * If the author wants to display their name, then this is the last name
     * @return The last name of the author
     */
    String lastName() default "";
}
