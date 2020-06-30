package org.block.panel;

import org.block.panel.block.BlockType;
import org.block.panel.section.BlockSection;
import org.block.panel.section.GUISection;
import org.block.panel.section.GroupedSection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ChooserDisplayPanel extends JPanel {

    private final List<GUISection> sectionList = new ArrayList<>(Arrays.asList(
            new GroupedSection(null, "Math", new BlockSection(null,BlockType.BLOCK_TYPE_SUM, "Sum")),
            new GroupedSection(null, "Value", new BlockSection(null, BlockType.BLOCK_TYPE_INTEGER, "Integer"))
    ));
    private final JTextField search = new JTextField();
    private final JPanel panel = new JPanel();

    public ChooserDisplayPanel(){
        updateLayout();
    }

    public List<GUISection> getSectionList(){
        return Collections.unmodifiableList(this.sectionList);
    }

    public void register(GUISection section){
        this.sectionList.add(section);
        updateLayout();
    }

    private void updateLayout(){
        this.panel.removeAll();
        this.removeAll();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        this.add(this.search, c);
        c.gridy = 1;
        c.weighty = 1.0;
        this.add(this.panel, c);

        this.panel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 0, 2, 0);
        for(GUISection section : this.sectionList){
            this.panel.add((Container)section, c);
            c.gridy++;
        }
    }

}
