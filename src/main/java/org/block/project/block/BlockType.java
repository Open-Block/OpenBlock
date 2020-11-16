package org.block.project.block;

import org.block.project.block.java.method.call.JavaMethodCallBlock;
import org.block.project.block.java.operation.number.MinusOperation;
import org.block.project.block.java.operation.number.SumOperation;
import org.block.project.block.java.specific.ReturnBlock;
import org.block.project.block.java.method.MethodBlock;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.java.value.number.IntegerBlock;
import org.block.project.block.java.variable.UseVariableBlock;
import org.block.project.block.java.variable.VariableBlock;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.jetbrains.annotations.NotNull;

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
    FixedTitle<Integer> TITLE_LAYER = new FixedTitle<>("Layer", Parser.INTEGER);
    FixedTitle.Listable<UUID> TITLE_DEPENDS = new FixedTitle.Listable<>("Depends", Parser.UNIQUE_ID);
    FixedTitle<String> TITLE_CLASS = new FixedTitle<>("Class", Parser.STRING);

    IntegerBlock.IntegerBlockType BLOCK_TYPE_INTEGER = new IntegerBlock.IntegerBlockType();
    StringBlock.StringBlockType BLOCK_TYPE_STRING = new StringBlock.StringBlockType();
    SumOperation.SumOperationType BLOCK_TYPE_SUM = new SumOperation.SumOperationType();
    MinusOperation.MinusOperationType BLOCK_TYPE_MINUS = new MinusOperation.MinusOperationType();
    VariableBlock.VariableBlockType BLOCK_TYPE_VARIABLE = new VariableBlock.VariableBlockType();
    UseVariableBlock.VariableBlockType BLOCK_TYPE_VARIABLE_USE = new UseVariableBlock.VariableBlockType();
    MethodBlock.MethodBlockType BLOCK_TYPE_METHOD = new MethodBlock.MethodBlockType();
    ReturnBlock.ReturnBlockType BLOCK_TYPE_RETURN = new ReturnBlock.ReturnBlockType();
    JavaMethodCallBlock.JavaMethodCallBlockType<Void> BLOCK_TYPE_JAVA_METHOD_CALL = new JavaMethodCallBlock.JavaMethodCallBlockType<>(Void.class);

    /**
     * Creates a new instance of a block with the provided position
     * @param x The X position
     * @param y The Y position
     * @return The newly created Block
     */
    B build(int x, int y);

    /**
     * Creates a new instance of a block with the values found within the provided ConfigNode
     * @param node The node with the values
     * @return The built instance
     * @throws IllegalStateException If the ConfigNode does not have the required values stored within
     */
    B build(ConfigNode node);

    /**
     * Gets the default save location for all of the Blocks of this type
     * @return The save location
     */
    File saveLocation();

    /**
     * Gets the BlockType's name
     * @return The Block type's name
     */
    String getName();

    /**
     * Designed to be overwritten with super call to this.
     * Writes the provided block to the provided ConfigNode
     * @param node The save location
     * @param block The block to save
     */
    default void write(@NotNull ConfigNode node, @NotNull B block){
        TITLE_X.serialize(node, block.getX());
        TITLE_Y.serialize(node, block.getY());
        TITLE_LAYER.serialize(node, block.getLayer());
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
