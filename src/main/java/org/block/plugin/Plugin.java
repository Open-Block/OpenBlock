package org.block.plugin;

import org.block.jsf.data.jsfclass.JSFClass;

import java.util.HashSet;
import java.util.Set;

public class Plugin {

    public static final String ID_NODE = "ID";
    public static final String NAME_NODE = "Name";
    public static final String VERSION_NODE = "Version";
    public static final String CLASSES_NODE = "Classes";
    private final String id;
    private final String name;
    private final String version;
    private final Set<JSFClass> classes = new HashSet<>();

    public Plugin(String id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public Set<JSFClass> getClasses() {
        return this.classes;
    }
}
