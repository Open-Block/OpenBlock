package org.block.panel.section;

import java.util.List;
import java.util.Optional;

public interface GUISection {

    Optional<GUISection> getSectionsParent();
    List<GUISection> getSectionsChildren();
    String getTitle();
}
