package org.block.project.section;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GUISection {

    Optional<GUISection> getSectionsParent();
    List<GUISection> getSectionsChildren();
    Collection<String> getTags();
    String getTitle();
}
