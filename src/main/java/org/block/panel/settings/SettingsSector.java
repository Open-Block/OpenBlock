package org.block.panel.settings;

import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.Map;

public interface SettingsSector {

    String getName();
    Map<Text, Node> getSettings();
}
