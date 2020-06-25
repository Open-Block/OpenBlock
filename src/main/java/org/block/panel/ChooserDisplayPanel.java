package org.block.panel;

import org.block.panel.section.ConnectedSection;
import org.block.panel.section.GUISection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ChooserDisplayPanel extends JPanel {

    private final List<GUISection> sectionList = new ArrayList<>();

    public List<GUISection> getSectionList(){
        return Collections.unmodifiableList(this.sectionList);
    }

    public void register(GUISection section){
        this.sectionList.add(section);
        updateLayout();
    }

    private void updateLayout(){
        this.removeAll();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.insets = new Insets(10, 0, 10, 0);
        for(int A = 0; A < this.sectionList.size(); A++){
            c.gridy = A;
            GUISection section = this.sectionList.get(A);
            if(section instanceof ConnectedSection){
                if (((ConnectedSection) section).isOpen()){
                    c.weighty = 1.0;
                }
            }
            this.add((Container)section, c);
            if(section instanceof ConnectedSection){
                if (((ConnectedSection) section).isOpen()){
                    c.weighty = 0.0;
                }
            }
        }
    }

}
