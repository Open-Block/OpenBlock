package org.block.panel.settings;

import com.gluonhq.charm.glisten.control.settings.DefaultOption;
import com.gluonhq.charm.glisten.control.settings.OptionEditor;
import javafx.scene.Node;
import org.block.panel.settings.editor.ParsedEditor;

import java.util.function.Function;

public class BlockOption<O, T, E extends OptionEditor<O> & ParsedEditor<T>> extends DefaultOption<O> {

    private final T defaultValue;
    private final Function<T, O> function;

    public BlockOption(Node graphic, String caption, String description, String catalogue, T defaultValue, Function<T, O> function, E editor) {
        super(graphic, caption, description, catalogue, function.apply(defaultValue), true, v -> editor);
        this.defaultValue = defaultValue;
        this.function = function;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public E getEditor() {
        return (E) this.editorFactory.apply(this);
    }

    public T getValue() {
        try {
            var opValue = this.getEditor().getParsedValue();
            return opValue.orElse(this.defaultValue);
        } catch (IllegalStateException e) {
            System.out.println("Using Default value: ");
            e.printStackTrace();
            return this.defaultValue;
        }
    }

    public void setValue(T value) {
        this.getEditor().setParsedValue(value);
    }
}
