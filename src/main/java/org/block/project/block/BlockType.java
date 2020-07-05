package org.block.project.block;

import org.block.project.block.java.operation.SumOperation;
import org.block.project.block.java.start.method.MethodBlock;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.java.value.number.IntegerBlock;
import org.block.project.block.java.variable.VariableBlock;
import org.block.serializtion.ConfigNode;
import org.block.serializtion.FixedTitle;
import org.block.serializtion.parse.Parser;

import java.io.File;
import java.util.UUID;

/**
 * The BlockType provides global data between all Blocks it produces.
 * The BlockType is used with the display chooser to produce new blocks with default values
 * @param <B> The Blocks class type it is connected with
 */
public interface BlockType<B extends Block> {

    FixedTitle<Integer> TITLE_X = new FixedTitle<>("X", Parser.INTEGER);
    FixedTitle<Integer> TITLE_Y = new FixedTitle<>("Y", Parser.INTEGER);
    FixedTitle<UUID> TITLE_UUID = new FixedTitle<>("UUID", Parser.UNIQUE_ID);
    FixedTitle.Listable<UUID> TITLE_DEPENDS = new FixedTitle.Listable<>("Depends", Parser.UNIQUE_ID);

    IntegerBlock.IntegerBlockType BLOCK_TYPE_INTEGER = new IntegerBlock.IntegerBlockType();
    StringBlock.StringBlockType BLOCK_TYPE_STRING = new StringBlock.StringBlockType();
    SumOperation.SumOperationType BLOCK_TYPE_SUM = new SumOperation.SumOperationType();
    VariableBlock.VariableBlockType BLOCK_TYPE_VARIABLE = new VariableBlock.VariableBlockType();
    MethodBlock.MethodBlockType BLOCK_TYPE_METHOD = new MethodBlock.MethodBlockType();

    /**
     * Creates a new instanceof a block with the provided position
     * @param x The X position
     * @param y The Y position
     * @return The newly created Block
     */
    B build(int x, int y);
    B build(ConfigNode node);
    File saveLocation();
    String getName();

    default void write(ConfigNode node, B block){
        TITLE_X.serialize(node, block.getX());
        TITLE_Y.serialize(node, block.getY());
        TITLE_UUID.serialize(node, block.getUniqueId());
    }

    /**
     * Creates a new instanceof a block with the provided position.
     * This function is used in the chooser, therefore you can have different settings for the chooser version
     * if the implementation allows for it, by default it uses the {@link BlockType#build(int x, int y)}
     * @param x The X position
     * @param y The Y position
     * @return The newly created Block
     */
    default B buildDefault(int x, int y){
        return build(x, y);
    }
}
