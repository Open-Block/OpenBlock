package org.block.plugin.launch.event;

import org.block.plugin.launch.LaunchEvent;
import org.block.project.module.Module;

import java.util.HashSet;
import java.util.Set;

public class ModuleRegisterEvent implements LaunchEvent {

    private Set<Module> module = new HashSet<>();

    public Set<Module> getModule(){
        return this.module;
    }
}
