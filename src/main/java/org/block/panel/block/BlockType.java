package org.block.panel.block;

import org.block.serializtion.ConfigNode;
import org.block.serializtion.FixedTitle;
import org.block.serializtion.parse.Parser;

import java.io.File;
import java.util.UUID;

public interface BlockType<B extends Block> {

    FixedTitle<Integer> TITLE_X = new FixedTitle<>("X", Parser.INTEGER);
    FixedTitle<Integer> TITLE_Y = new FixedTitle<>("Y", Parser.INTEGER);
    FixedTitle<UUID> TITLE_UUID = new FixedTitle<>("UUID", Parser.UNIQUE_ID);
    FixedTitle.Listable<UUID> TITLE_DEPENDS = new FixedTitle.Listable<>("Depends", Parser.UNIQUE_ID);

    B build(int x, int y);
    B build(ConfigNode node);
    File saveLocation();
    String getName();

    default void write(ConfigNode node, B block){
        TITLE_X.serialize(node, block.getX());
        TITLE_Y.serialize(node, block.getY());
        TITLE_UUID.serialize(node, block.getUniqueId());
    }

    default B buildDefault(int x, int y){
        return build(x, y);
    }
}
