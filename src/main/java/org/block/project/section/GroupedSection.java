package org.block.project.section;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class GroupedSection extends JPanel implements GUISection {

    public class OnMouseClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            GroupedSection.this.panel.setVisible(!GroupedSection.this.panel.isVisible());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    protected final List<GUISection> sections = new ArrayList<>();
    protected final JPanel panel = new JPanel();
    protected final JLabel text;
    protected final GUISection parent;
    protected final Collection<String> collection;

    public GroupedSection(GUISection parent, String text, Collection<String> tags, GUISection... collection){
        this(parent, text, tags, Arrays.asList(collection));
    }

    public GroupedSection(GUISection parent, String text, Collection<String> tag, Collection<GUISection> collection){
        this.text = new JLabel(text, SwingUtilities.CENTER);
        this.parent = parent;
        this.sections.addAll(collection);
        this.collection = Collections.unmodifiableCollection(tag);
        init();
    }

    private void init(){
        this.addMouseListener(new OnMouseClick());
        this.text.setBackground(Color.DARK_GRAY);
        this.text.setForeground(Color.WHITE);
        this.text.setOpaque(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.text, c);
        c.weighty = 1.0;
        c.gridy = 1;
        this.add(this.panel, c);
    }

    private void updatePanel(){
        this.panel.removeAll();
        this.panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        for(GUISection section : this.sections){
            this.panel.add((Container)section, c);
            c.gridy++;
        }
    }

    public void register(GUISection section){
        this.sections.add(section);
        updatePanel();
    }

    public void unregister(GUISection section){
        this.sections.remove(section);
        updatePanel();
    }

    @Override
    public Optional<GUISection> getSectionsParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public List<GUISection> getSectionsChildren() {
        return Collections.unmodifiableList(this.sections);
    }

    @Override
    public Collection<String> getTags() {
        return this.collection;
    }

    @Override
    public String getTitle() {
        return this.text.getText();
    }
}
