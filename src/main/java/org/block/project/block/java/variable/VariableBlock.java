package org.block.project.block.java.variable;

import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.group.AbstractBlockGroup;
import org.block.project.block.java.value.AbstractValue;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.util.List;
import java.util.*;

public class VariableBlock extends AbstractAttachableBlock {

    public static class VariableBlockType implements BlockType<VariableBlock>{

        @Override
        public VariableBlock build(int x, int y) {
            return new VariableBlock(x, y);
        }

        @Override
        public VariableBlock build(ConfigNode node) {
            return null;
        }

        @Override
        public File saveLocation() {
            return new File("blocks/variable/define");
        }

        @Override
        public String getName() {
            return "Variable Define";
        }
    }

    public static class NameBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<StringBlock> {

        public NameBlockGroup(int relativeY) {
            super(SECTION_NAME, "Name", relativeY);
        }
    }

    public static class BodyBlockGroup extends AbstractBlockGroup.AbstractSingleBlockGroup<ValueBlock<?>>{

        public BodyBlockGroup(int relativeY) {
            super(SECTION_BODY, "Body", relativeY);
        }
    }

    public static final String SECTION_BODY = "variable:body";
    public static final String SECTION_NAME = "variable:name";

    private int marginX = 8;
    private int marginY = 8;

    public VariableBlock(int x, int y){
        this(x, y, null, null);
    }

    public VariableBlock(int x, int y, StringBlock name, ValueBlock<?> block) {
        super(x, y);
        this.blockGroups.add(new NameBlockGroup(0));
        this.blockGroups.add(new BodyBlockGroup(0));
    }

    public BodyBlockGroup getBodyBlockGroup(){
        return (BodyBlockGroup) this.getGroup(SECTION_BODY).get();
    }

    public NameBlockGroup getNameBlockGroup(){
        return (NameBlockGroup) this.getGroup(SECTION_NAME).get();
    }

    public Optional<String> getName() {
        return this.getNameBlockGroup().getSector().getAttachedBlock().map(AbstractValue::getValue);
    }

    @Override
    public BlockGraphics getGraphicShape() {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public String writeCode(int tab) {
        Optional<ValueBlock<?>> opValueBlock = this.getBodyBlockGroup().getSector().getAttachedBlock();
        if(opValueBlock.isEmpty()){
            throw new IllegalStateException("Could not find the type specified. Does it have a value?");
        }
        Optional<StringBlock> opStringBlock = this.getNameBlockGroup().getSector().getAttachedBlock();
        if(opStringBlock.isEmpty()){
            throw new IllegalStateException("Could not find the name specified. Does it have a name?");
        }
        if(opStringBlock.get().getValue().length() == 0){
            throw new IllegalStateException("Could not find the name specified. Provide one in the block");
        }
        Class<?> clazz = null;
        String ret = null;
        do{
            Class<?> clazz2 = clazz == null ? opValueBlock.get().getExpectedValue().orElse(null) : clazz.getDeclaringClass();
            if(clazz2 == null){
                break;
            }
            clazz = clazz2;
            ret = clazz.getSimpleName() + ((ret == null) ? "" :  "." + ret);
        }while(true);
        if(ret == null){
            throw new IllegalStateException("Unknown class");
        }
        if(ret.startsWith(".")){
            ret = ret.substring(1);
        }

        return ret + " " + opStringBlock.get().getValue() + " = " + opValueBlock.get().writeCode(tab + 1);
    }

    @Override
    public Collection<String> getCodeImports() {
        Optional<ValueBlock<?>> opValueBlock = this.getBodyBlockGroup().getSector().getAttachedBlock();
        if(opValueBlock.isEmpty()){
            throw new IllegalStateException("Could not find the type specified. Does it have a value?");
        }
        Class<?> clazz = null;
        do{
            Class<?> clazz2 = (clazz == null) ? opValueBlock.get().getExpectedValue().orElse(null) : clazz.getDeclaringClass();
            if(clazz2 == null){
                break;
            }
            clazz = clazz2;
        }while(true);

        if(clazz == null){
            throw new IllegalStateException("No value block attached");
        }
        List<String> list = new ArrayList<>();
        if(clazz.getPackage() != null){
            list.add(clazz.getPackage().getName());
        }
        list.addAll(opValueBlock.get().getCodeImports());
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public BlockType<?> getType() {
        return BlockType.BLOCK_TYPE_VARIABLE;
    }
}
