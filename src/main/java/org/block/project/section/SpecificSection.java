package org.block.project.section;

import java.util.Collection;
import java.util.List;

public class SpecificSection extends GroupedSection {

    public SpecificSection(GUISection parent, String text, Collection<String> tags, GUISection... collection) {
        super(parent, text, tags, collection);
    }

    public SpecificSection(GUISection parent, String text, Collection<String> tag, Collection<GUISection> collection) {
        super(parent, text, tag, collection);
    }

    public void setTitle(String title){
        this.text.setText(title);
    }

    public void setTags(Collection<String> collection){
        this.collection.clear();
        this.collection.addAll(collection);
    }

    public synchronized void unregisterAll(){
        List<GUISection> list = this.getSectionsChildren();
        for(int A = 0; A < list.size(); A++){
            this.unregister(list.get(A));
        }
    }
}
