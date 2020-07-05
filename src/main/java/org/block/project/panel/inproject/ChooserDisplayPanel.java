package org.block.project.panel.inproject;

import org.block.project.block.BlockType;
import org.block.project.section.BlockSection;
import org.block.project.section.GUISection;
import org.block.project.section.GroupedSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class ChooserDisplayPanel extends JPanel {

    public class OnKey implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            new Thread(ChooserDisplayPanel.this::updatePanel).start();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private final List<GUISection> sectionList = new ArrayList<>();
    private final JTextField search = new JTextField();
    private final JPanel panel = new JPanel();

    public final Function<GUISection, Double> FILTER = (e) -> {
        if(this.search.getText().length() == 0){
            return 100.0;
        }
        String compare = this.search.getText();
        Set<String> total = new HashSet<>(e.getTags());
        total.add(e.getTitle());
        Map<String, Double> map = new HashMap<>();
        for(String word : compare.split(" ")) {
            for (String value : total) {
                for(int A = Math.min(word.length(), value.length()); A > 0; A--){
                    if(value.toUpperCase().contains(word.substring(0, A).toUpperCase())){
                        double percent = (A * 100.0) / value.length();
                        if(map.containsKey(word)){
                            if (percent > map.get(word)){
                                map.replace(word, percent);
                            }
                        }else {
                            map.put(word, percent);
                        }
                    }
                }
            }
        }
        double percent = 0;
        int totalPercent = map.size() * 100;
        for(Double per : map.values()){
            percent += per;
        }
        if(percent == 0){
            return 0.0;
        }
        return (percent * 100) / totalPercent;
    };

    public ChooserDisplayPanel(){
        this.search.addKeyListener(new OnKey());
        init();
        updateLayout();
    }

    public void init(){
        GroupedSection mathSection = new GroupedSection(null, "Math", Collections.singletonList("Mathematics"));
        GroupedSection valueSection = new GroupedSection(null, "Value", Collections.emptyList());

        GroupedSection storeSection = new GroupedSection(valueSection, "Store", Arrays.asList("Variable", "Save"));
        GroupedSection numberSection = new GroupedSection(valueSection, "Number", Arrays.asList("Whole", "Decimal"));

        valueSection.register(storeSection);
        valueSection.register(numberSection);

        mathSection.register(new BlockSection(mathSection, BlockType.BLOCK_TYPE_SUM, "Sum", "Plus", "Add"));
        valueSection.register(new BlockSection(valueSection, BlockType.BLOCK_TYPE_STRING, "Text", "String", "Character", "Char"));
        numberSection.register(new BlockSection(numberSection, BlockType.BLOCK_TYPE_INTEGER, "Integer", "Number"));
        storeSection.register(new BlockSection(storeSection, BlockType.BLOCK_TYPE_VARIABLE, "Store", "Variable", "Save"));
        storeSection.register(new BlockSection(storeSection, BlockType.BLOCK_TYPE_METHOD, "Set", "Method", "Call"));

        this.sectionList.add(mathSection);
        this.sectionList.add(valueSection);
    }

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
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        this.add(this.search, c);
        c.gridy = 1;
        c.weighty = 1.0;
        this.add(this.panel, c);
        updatePanel();
    }

    private synchronized void updatePanel(){
        GridBagConstraints c = new GridBagConstraints();
        this.panel.removeAll();
        this.panel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 0, 2, 0);
        List<Map.Entry<GUISection, Double>> likelyhood = new ArrayList<>();
        this.sectionList.forEach(s -> {
            Double percent = FILTER.apply(s);
            likelyhood.add(new AbstractMap.SimpleEntry<>(s, percent));
            Comparator<Map.Entry<GUISection, Double>> compare = Comparator.comparingDouble(Map.Entry::getValue);
            Comparator<Map.Entry<GUISection, Double>> then = Comparator.comparing(o1 -> o1.getKey().getTitle());
            compare.thenComparing(then.reversed());
            likelyhood.sort(compare);
            for(int A = 0; A < this.panel.getComponentCount(); A++){
                this.panel.remove(A);
            }
            c.gridy = 0;
            for(int A = likelyhood.size(); A > 0; A--){
                Map.Entry<GUISection, Double> section = likelyhood.get(A - 1);
                this.panel.add((Container)section.getKey(), c);
                c.gridy++;
                this.panel.repaint();
                this.panel.revalidate();
            }
        });
    }

}
