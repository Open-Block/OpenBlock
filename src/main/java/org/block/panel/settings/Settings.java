package org.block.panel.settings;

import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Settings {

    Set<SettingsSector> GENERAL_SETTINGS = new HashSet<>(Arrays.<SettingsSector>asList(new LogFileSettings()));
    Set<SettingsSector> PROJECT_SETTINGS = new HashSet<>();

    static Text getSimpleLogger(){
        throw new IllegalStateException("Not implemented");
    }

}
