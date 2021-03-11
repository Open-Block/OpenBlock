package org.block.plugin;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.project.Project;
import org.block.project.UnloadedProject;
import org.block.serialization.ConfigImplementation;

import java.io.IOException;
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

    public Project.Loaded load(UnloadedProject project, ConfigImplementation<?> configImplementation) {
        throw new IllegalStateException("Not implemented");
    }

    public GridPane createDisplayInfo(UnloadedProject project) {
        try {
            var plugin = project.getExpectedPlugin();
            var pane = new GridPane();
            pane.setAlignment(Pos.TOP_CENTER);
            pane.setHgap(10.0);
            pane.setGridLinesVisible(true);
            pane.getColumnConstraints().clear();
            pane.getColumnConstraints().add(new ColumnConstraints());
            var valueColumn = new ColumnConstraints();
            valueColumn.setFillWidth(true);
            pane.getColumnConstraints().add(valueColumn);
            pane.add(new Label("Plugin:"), 0, 0);
            pane.add(this.createValueLabel(plugin.getName()), 1, 0);
            return pane;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Plugin)) {
            return false;
        }
        return this.id.equals(((Plugin) obj).id);
    }

    @Override
    public String toString() {
        return "Plugin[Id: " + this.id + ", Name: " + this.name + ", Version: " + this.version + "]";
    }

    private Label createValueLabel(String value) {
        var label = new Label(value);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER_RIGHT);
        return label;
    }
}
