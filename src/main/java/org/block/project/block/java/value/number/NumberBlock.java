package org.block.project.block.java.value.number;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.plugin.event.EventListener;
import org.block.project.block.event.mouse.BlockMouseClickEvent;
import org.block.project.block.event.value.BlockEditValueEvent;
import org.block.project.block.input.OpenBlockDialog;
import org.block.project.block.input.PanelDialog;
import org.block.project.block.input.type.ValueDialog;
import org.block.project.block.java.value.AbstractValue;
import org.block.project.legacypanel.inproject.MainDisplayPanel;
import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.block.util.ClassCompare;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * The generic {@link Block} to display a pre-defended number value
 * Note that in its current form, the expected value is not in its primitive form.
 * This needs to be worked on ... might need a overhaul to {@link Block.ValueBlock} as generics can not accept primitives
 * @param <V> The expected value
 */
public abstract class NumberBlock<V extends Number> extends AbstractValue<V> implements Block.ValueBlock.ConnectedValueBlock.MutableConnectedValueBlock<V> {

    public abstract static class NumberBlockType<N extends Number> implements BlockType<NumberBlock<N>>{

        private final Parser<N> parser;

        public NumberBlockType(Parser<N> parser){
            this.parser = parser;
        }

        @Override
        public NumberBlock<N> build(ConfigNode node) {
            Optional<Integer> opX = TITLE_X.deserialize(node);
            if(!opX.isPresent()){
                throw new IllegalStateException("Could not find position X");
            }
            Optional<Integer> opY = TITLE_Y.deserialize(node);
            if(!opY.isPresent()){
                throw new IllegalStateException("Could not find position Y");
            }
            Optional<N> opValue = node.getValue("Title", this.parser);
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find value");
            }
            Optional<UUID> opUUID = TITLE_UUID.deserialize(node);
            if(!opValue.isPresent()){
                throw new IllegalStateException("Could not find ID");
            }
            NumberBlock<N> block = this.build(opX.get(), opY.get());
            block.setValue(opValue.get());
            block.id = opUUID.get();
            block.layer = TITLE_LAYER.deserialize(node).orElse(Blocks.getInstance().getLoadedProject().get().getPanel().getBlocksPanel().getBlocks().size());
            return block;
        }

        @Override
        public void write(ConfigNode node, NumberBlock<N> block) {
            BlockType.super.write(node, block);
            node.setValue("Title", this.parser, block.getValue());
        }

        @Override
        public File saveLocation() {
            return new File("blocks/value/number");
        }

        @Override
        public String getName() {
            return "Number";
        }
    }

    public class OnClickListener implements EventListener<BlockMouseClickEvent> {

        @Override
        public Class<BlockMouseClickEvent> getEventClass() {
            return BlockMouseClickEvent.class;
        }

        @Override
        public void onEvent(BlockMouseClickEvent event) {
            PanelDialog panel = (PanelDialog)NumberBlock.this.createDialog();
            BlockEditValueEvent event2 = ((MainDisplayPanel)Blocks.getInstance().getWindow().getContentPane()).getBlocksPanel().getSelectedComponent().sendEvent(new BlockEditValueEvent(NumberBlock.this, panel));
            if(event2.isCancelled()){
                return;
            }
            OpenBlockDialog<? extends Container> dialog = new OpenBlockDialog<>((Window)Blocks.getInstance().getWindow(), event2.getEditPanel());
            panel.getAcceptButton().addActionListener((e) -> {
                NumberBlock.this.setValue((V)((ValueDialog<? extends Number>)panel).getOutput());
                dialog.dispose();

            });
            dialog.setVisible(true);
        }
    }

    public NumberBlock(int x, int y, V value) {
        super(x, y, value, Object::toString);
        init();
    }

    private void init(){
        this.registerEventListener(new OnClickListener());
    }

    @Override
    public Optional<Class<V>> getExpectedValue() {
        return Optional.of((Class<V>) ClassCompare.toPrimitive(this.getValue().getClass()));
    }

    @Override
    public String writeCode(int tab) {
        return this.getValue().toString();
    }

    @Override
    public Collection<String> getCodeImports() {
        return Collections.unmodifiableCollection(Collections.emptySet());
    }
}
