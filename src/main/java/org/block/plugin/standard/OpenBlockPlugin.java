package org.block.plugin.standard;

import org.block.Blocks;
import org.block.plugin.Plugin;
import org.block.plugin.PluginContainer;
import org.block.plugin.launch.LaunchListener;
import org.block.plugin.launch.event.ModuleRegisterEvent;
import org.block.plugin.launch.meta.Author;

/**
 * This is the class which if a object requires a plugin but comes as default will be used
 * This is not used within the loading pool, like other plugins and cannot be disabled
 * While its not in the loading pool, you can use it as a dependent, mainly for version checking
 * This is also not provided within the {@link Blocks#getPlugins()} or {@link Blocks#getEnabledPlugins()}
 * launchFrom is disabled as this is not launched, it is just context
 */
@Plugin(
        id = "openblock",
        displayName = "Open Block",
        majorVersion = 1,
        launchFrom = false,
        contributors = {
                @Author(
                        displayName = "mosemister",
                        contributions = {
                                "Project Lead",
                                "PluginAPI",
                                "BlockAPI",
                                "Graphics",
                                "Documentation Author"
                        })
        })
public final class OpenBlockPlugin {

    @LaunchListener
    public void registerModule(ModuleRegisterEvent event){
        event.getModule().add(PluginContainer.OPEN_BLOCK_MODULE);
    }
}
