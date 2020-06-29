package org.block.panel.block.input.type;

import org.block.panel.block.input.PanelDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class CollectionDialog<T> extends PanelDialog implements ValueDialog<T> {

    private class ToStringRenderer extends JLabel implements ListCellRenderer<T> {

        public ToStringRenderer(){
            super("", SwingUtilities.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setText(CollectionDialog.this.toString.apply(value));
            this.setOpaque(true);
            if(isSelected){
                this.setBackground(new Color(163, 184, 204));
            }else{
                this.setBackground(null);
            }
            return this;
        }
    }

    private final Function<T, String> toString;

    @SafeVarargs
    public CollectionDialog(Function<T, String> function, T... values){
        this(function, Arrays.asList(values));
    }

    public CollectionDialog(Function<T, String> function, Collection<T> values) {
        super(new JComboBox<>());
        this.toString = function;
        init(values);
    }

    private void init(Collection<T> values){
        values.forEach(v -> getInteractionPanel().addItem(v));
        this.getInteractionPanel().setRenderer(new ToStringRenderer());
    }

    @Override
    public JComboBox<T> getInteractionPanel() {
        return (JComboBox<T>) super.getInteractionPanel();
    }

    @Override
    public T getOutput() {
        return (T)getInteractionPanel().getSelectedItem();
    }
}
