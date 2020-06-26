package org.block.panel.block;


import org.block.panel.block.event.BlockEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Block {

    interface ParameterInsertBlock extends Block {

        int getMaxCount();
        List<ValueBlock<?>> getCurrentParameters();
        void addParameter(ValueBlock<?> block);
        void addParameter(int index, ValueBlock<?> block);
        void removeParameter(ValueBlock<?> block);
        void removeParameter(int space);
        boolean canAccept(int slot, ValueBlock<?> block);

        default void moveParameter(ValueBlock<?> block, int spaces){
            List<ValueBlock<?>> list = this.getCurrentParameters();
            int space = -1;
            for(int A = 0; A < list.size(); A++){
                if(list.get(A).equals(block)){
                    space = A;
                    break;
                }
            }
            if(space == -1){
                throw new IllegalArgumentException("Could not find block within parameter list");
            }
            int newSpace = (space + spaces) - 1;
            if(newSpace < 0){
                newSpace = 0;
            }
            if(newSpace >= list.size()){
                newSpace = list.size();
            }
            this.removeParameter(block);
            this.addParameter(newSpace, block);
        }

    }

    interface ValueBlock<V> extends Block {

        Class<V> getExpectedValue();

    }

    interface AttachableBlock<V> extends ValueBlock<V> {

        boolean canAttach(Block block);
        Optional<Block> getAttached();
        void removeAttached();
        void setAttached(Block block);

    }

    interface SequenceBlock extends Block {

        List<Block> getSequence();
        void removeFromSequence(Block block);
        void addToSequence(Block block);

    }

    interface TextBlock extends Block {

        String getText();
        void setText(String text);

    }

    interface CallerBlock extends Block {

        String writeCall();

    }

    int getHeight();
    int getWidth();
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    boolean isSelected();
    void setSelected(boolean selected);
    boolean isHighlighted();
    void setHighlighted(boolean selected);
    Collection<BlockEvent> getEvents();
    void registerEvent(BlockEvent event);
    UUID getUniqueId();

    void paint(Graphics2D graphics2D);
    String writeCode();

    default boolean contains(int x, int y){
        if(x < getX()){
            return false;
        }
        if(y < getY()){
            return false;
        }
        if(x > (getX() + getWidth())){
            return false;
        }
        if(y > (getY() + getHeight())){
            return false;
        }
        return true;
    }


}
