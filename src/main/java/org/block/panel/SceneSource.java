package org.block.panel;

import javafx.scene.Parent;
import javafx.scene.Scene;

@Deprecated
public interface SceneSource {

    Parent buildNode();

    default Scene build() {
        return new Scene(buildNode());
    }
}
