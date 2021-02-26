package org.block.panel.settings.editor;

import java.util.Optional;

public interface ParsedEditor<T> {

    Optional<T> getParsedValue();

    void setParsedValue(T value);
}
