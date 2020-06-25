package org.block.panel.section;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ConnectedSection extends JPanel implements GUISection {

    private boolean isOpen;
    private final GUISection parent;
    private final List<GUISection> sections = new ArrayList<>();
    private final String title;

    public ConnectedSection(GUISection section, String title, GUISection... sections){
        this(section, title, Arrays.asList(sections));
    }

    public ConnectedSection(GUISection section, String title, Collection<GUISection> sections){
        this.parent = section;
        this.title = title;
        this.sections.addAll(sections);
    }

    public boolean isOpen(){
        return this.isOpen;
    }

    public void setOpen(boolean check){
        this.isOpen = check;
    }

    @Override
    public Optional<GUISection> getSectionsParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public List<GUISection> getSectionsChildren() {
        return this.sections;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
    }
}
