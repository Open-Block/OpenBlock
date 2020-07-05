package org.block.plugin.launch.meta;

/**
 * The information of if the loading plugin required another plugin to load before hand.
 * If A dependent is specified, then that depending plugin will be loaded first.
 *
 * This class can also be used in some events whereby the event will be fired to the depending plugin
 * first and then this plugin
 */
public @interface Dependent {

    /**
     * Gets the id of the dependent plugin
     * @return The id of the dependent plugin
     */
    String getId();

    /**
     * Gets the minimum supported Major number (The returned number is valid - use 0 if all versions are supported)
     * @return Gets the minimum supported major number
     */
    int getMinMajorVersion();

    /**
     * Gets the maximum supported Major number (use {@link Integer#MAX_VALUE} if it should be supported from hereon)
     * @return Gets the maximum supported Major number
     */
    int getMaxMajorVersion();

    /**
     * Gets the minimum supported Minor number (The returned number is valid - use 0 if all versions are supported)
     * @return Gets the minimum supported minor number
     */
    int getMinMinorVersion();

    /**
     * Gets the maximum supported Minor number (use {@link Integer#MAX_VALUE} if it should be supported from hereon)
     * @return Gets the maximum supported Minor number
     */
    int getMaxMinorVersion();

    /**
     * Gets the minimum supported Patch number (The returned number is valid - use 0 if all versions are supported)
     * @return Gets the minimum supported patch number
     */
    int getMinPatchVersion();

    /**
     * Gets the maximum supported Patch number (use {@link Integer#MAX_VALUE} if it should be supported from hereon)
     * @return Gets the maximum supported Patch number
     */
    int getMaxPatchVersion();

    /**
     * Checks if this plugin can be loaded without the depending plugin.
     *
     * If the plugin requires this specified plugin for extra features, however does not need the dependent to make
     * this plugin work, then specify this to be true
     * @return If this plugin can load without the dependent plugin
     */
    boolean canLoadWithout() default false;
}
