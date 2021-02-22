package org.block.panel.settings.editor;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.control.settings.Option;
import com.gluonhq.charm.glisten.control.settings.OptionEditor;
import javafx.beans.property.Property;
import javafx.util.StringConverter;
import org.util.mappers.MappedBindedProperty;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class FileEditor implements OptionEditor<File> {

    private final AutoCompleteTextField<File> field = new AutoCompleteTextField<>();
    private final FileFilter filter;
    private final MappedBindedProperty<String, File> mappedValue = new MappedBindedProperty<>(this.field.textProperty(), File::getAbsolutePath, File::new);

    public FileEditor(Option<File> option){
        this(option, pathname -> true);
    }

    public FileEditor(Option<File> option, FileFilter fileFilter) {
        this.filter = fileFilter;
        valueProperty().bindBidirectional(option.valueProperty());

        this.field.setCompleterMode(AutoCompleteTextField.CompleterMode.SEARCH_AUTOMATICALLY);

        this.field.setConverter(new StringConverter<>() {
            @Override
            public String toString(File file) {
                return file.getAbsolutePath();
            }

            @Override
            public File fromString(String s) {
                return new File(s);
            }
        });

        this.field.setCompleter(s -> {
            File file = new File(s);
            var filter = "";
            if (!file.exists()) {
                filter = file.getName();
                file = file.getParentFile();
            }
            System.out.println("filefilter on: " + file.getAbsolutePath());
            var files = file.listFiles(fileFilter);
            if (files == null) {
                System.out.println("\tNo files found");
                return Collections.emptyList();
            }
            final var finalFilter = filter;
            var array = Arrays.asList(files).parallelStream().filter(f -> f.getName().startsWith(finalFilter)).collect(Collectors.toList());
            System.out.println("\tFiles Found: " + array.size());
            return array;
        });
    }

    public FileFilter getFilter(){
        return this.filter;
    }

    @Override
    public AutoCompleteTextField<File> getEditor() {
        return this.field;
    }

    @Override
    public Property<File> valueProperty() {
        return this.mappedValue;
    }

    @Override
    public File getValue() {
        return this.field.getValue();
    }

    @Override
    public void setValue(File file) {
        this.field.setText(file.getPath());
    }
}
