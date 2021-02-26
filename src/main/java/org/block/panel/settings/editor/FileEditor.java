package org.block.panel.settings.editor;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.control.settings.OptionEditor;
import javafx.beans.property.Property;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileEditor implements OptionEditor<String>, ParsedEditor<File> {

    private final AutoCompleteTextField<String> field = new AutoCompleteTextField<>();
    private final FileFilter filter;

    public FileEditor() {
        this(pathname -> true);
    }

    public FileEditor(FileFilter fileFilter) {
        this.filter = fileFilter;
        this.field.setCompleter(s -> {
            File file = new File(s);
            var filter = "";
            if (!file.exists()) {
                filter = file.getName();
                file = file.getParentFile();
            }
            var files = file.listFiles(fileFilter);
            if (files == null) {
                return Collections.emptyList();
            }
            final var finalFilter = filter;
            return Arrays.asList(files).parallelStream().filter(f -> f.getName().startsWith(finalFilter)).map(File::getPath).collect(Collectors.toList());
        });
    }

    public FileFilter getFilter() {
        return this.filter;
    }

    @Override
    public AutoCompleteTextField<String> getEditor() {
        return this.field;
    }

    @Override
    public Property<String> valueProperty() {
        return this.field.textProperty();
    }

    @Override
    public String getValue() {
        return this.field.getText();
    }

    @Override
    public void setValue(String filePath) {
        System.out.println("setting as " + filePath);
        this.field.setText(filePath);
    }

    @Override
    public Optional<File> getParsedValue() {
        var value = this.getValue();
        if (value == null || value.strip().length() == 0) {
            return Optional.empty();
        }
        return Optional.of(new File(value));
    }

    @Override
    public void setParsedValue(File value) {
        this.setValue(value.getPath());
    }
}
