package org.block.panel.settings;

import java.io.IOException;

public interface Settings {

    void save() throws IOException;

    void load() throws IOException;
}
