package org.block.project.block.java.method.call;

import org.array.utils.ArrayUtils;
import org.block.Blocks;
import org.block.project.block.Block;
import org.block.project.block.BlockGraphics;
import org.block.project.block.BlockType;
import org.block.project.block.assists.AbstractAttachable;
import org.block.project.block.assists.BlockList;
import org.block.project.block.java.value.StringBlock;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaMethodCallBlock<C> extends AbstractAttachable implements MethodCallBlock {

    public class JavaMethodNameBlockList extends MethodCallBlock.AbstractMethodNameBlockList {

        public JavaMethodNameBlockList(int height) {
            super(height);
        }

        public JavaMethodNameBlockList(int height, ConnectedValueBlock<String> value) {
            super(height, value);
        }

        public JavaMethodNameBlockList(int height, String value){
            super(height, new StringBlock(JavaMethodCallBlock.this.x, JavaMethodCallBlock.this.y, value));
        }

        @Override
        public boolean canAcceptAttachment(Block block) {
            if(!(block instanceof ConnectedValueBlock)){
                return false;
            }
            ConnectedValueBlock<?> block1 = (ConnectedValueBlock<?>) block;
            return block1.getValue() instanceof String;
        }

        @Override
        public AttachableBlock getParent() {
            return JavaMethodCallBlock.this;
        }

        @Override
        public int getXPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException("Method name block is a single block, slot " + slot + " is not allowed");
            }
            return 0;
        }

        @Override
        public int getYPosition(int slot) {
            if(slot != 0){
                throw new IndexOutOfBoundsException("Method name block is a single block, slot " + slot + " is not allowed");
            }
            return 0;
        }

        @Override
        public int getSlot(int x, int y) {
            return 0;
        }
    }

    public static class JavaMethodCallBlockType<T> implements BlockType<JavaMethodCallBlock<T>>{

        private final Class<T> targetClass;

        public JavaMethodCallBlockType(Class<T> clazz){
            this.targetClass = clazz;
        }

        @Override
        public JavaMethodCallBlock<T> build(int x, int y) {
            return new JavaMethodCallBlock<>(x, y, this);
        }

        @Override
        public JavaMethodCallBlock<T> build(ConfigNode node) {
            return new JavaMethodCallBlock<>(0, 0, this);
        }

        @Override
        public File saveLocation() {
            return new File("blocks/method/call/java/");
        }

        @Override
        public String getName() {
            return "JavaMethod";
        }
    }

    private final JavaMethodCallBlock.JavaMethodCallBlockType<C> type;
    private final int marginX = 3;
    private final int marginY = 3;

    public JavaMethodCallBlock(int x, int y, JavaMethodCallBlock.JavaMethodCallBlockType<C> type) {
        super(x, y, 30, 30);
        this.type = type;
        this.attached.put(MethodCallBlock.METHOD_BLOCK_LIST, new JavaMethodNameBlockList(5));
    }

    public Class<C> getTargetClass(){
        return this.type.targetClass;
    }

    public Set<Method> getMethods(){
        Optional<ConnectedValueBlock<String>> opName = this.getMethodNameBlockList().getAttachment();
        if(!opName.isPresent()){
            return Collections.emptySet();
        }
        String name = opName.get().getValue().replaceAll(" ", "");
        return Stream.of(this.getTargetClass().getMethods()).parallel().filter(m -> m.getName().equalsIgnoreCase(name)).collect(Collectors.toSet());
    }

    public Optional<Method> getExpectedMethod(){
        Set<String> sections = this.getSections().parallelStream().filter(s -> s.equalsIgnoreCase(MethodCallBlock.METHOD_BLOCK_LIST)).collect(Collectors.toSet());
        return this.getMethods().parallelStream().filter(m -> {
            Set<String> params = ArrayUtils.convert(Collectors.toSet(), Parameter::getName, Arrays.asList(m.getParameters()));
            return params.equals(sections);
        }).findFirst();
    }

    private void updateSize(){
        Optional<ConnectedValueBlock<String>> opMethodName = this.getMethodNameBlockList().getAttachment();
        int methodNameWidth = 40;
        int methodNameHeight = this.getMethodNameBlockList().getSlotHeight(0);
        if(opMethodName.isPresent()){
            methodNameWidth = opMethodName.get().getWidth();
            methodNameHeight = opMethodName.get().getHeight();
        }
        Optional<Method> opMethod = this.getExpectedMethod();
        if(!opMethod.isPresent()){
            this.width = ((this.marginX * 2) + methodNameWidth);
            this.height = ((this.marginY * 2) + methodNameHeight);
            return;
        }
        Method method = opMethod.get();
        int width = (this.marginX * 2);
        int height = (method.getParameterCount() == 0) ? 0 : 20;
        for (Parameter parameter : method.getParameters()){
            Optional<Block> opBlock = this.getAttachments(parameter.getName()).getAttachment(0);
            if(opBlock.isPresent()){
                width = width + opBlock.get().getWidth() + this.marginX;
                height = Math.max(height, opBlock.get().getHeight());
            }else{
            }
        }
        this.width = Math.max(width, methodNameWidth);
        this.height = methodNameHeight + height;

    }

    @Override
    public Optional<String> containsSection(int x, int y) {
        y = y - this.getY();
        int height = this.getMethodNameBlockList().getSlotHeight(0);
        System.out.println("Y: " + y + " Height: " + height);
        if(y <= height){
            return Optional.of(MethodCallBlock.METHOD_BLOCK_LIST);
        }
        Optional<Method> opMethod = getExpectedMethod();
        if(!opMethod.isPresent() || opMethod.get().getParameterCount() == 0){
            return Optional.empty();
        }
        Parameter[] parameters = opMethod.get().getParameters();
        int startX = 0;
        for(Parameter parameter : parameters){
            Optional<Block> opBlock = this.getAttachments(parameter.getName()).getAttachment(0);
            int width = 40;
            if(opBlock.isPresent()){
                width = opBlock.get().getWidth();
            }
            if(y > startX && y < startX + width){
                return Optional.of(parameter.getName());
            }
        }
        return Optional.empty();
    }

    @Override
    public String writeCode(int tabs) {
        Optional<Method> opMethod = this.getExpectedMethod();
        if(!opMethod.isPresent()){
            throw new IllegalStateException("Unknown Method");
        }
        String name = opMethod.get().getName();
        Parameter[] parameters = opMethod.get().getParameters();
        for(Parameter parameter : parameters){
            if(!JavaMethodCallBlock.this.getAttachments(parameter.getName()).getAttachment(0).isPresent()){
                throw new IllegalStateException("Unknown parameter of '" + parameter.getName() + "' of type '" + parameter.getType().getSimpleName() + "'");
            }
        }
        return name + "(" + ArrayUtils.toString(", ", t -> JavaMethodCallBlock.this.getAttachments(t.getName()).getAttachment(0).get().writeCode(0), parameters) + ")";
    }

    @Override
    public Collection<String> getCodeImports() {
        return new HashSet<>();
    }

    @Override
    public BlockType<? extends Block> getType() {
        return this.type;
    }

    @Override
    public Optional<Class<Object>> getExpectedValue() {
        Optional<Method> opMethod = this.getExpectedMethod();
        if(!opMethod.isPresent()){
            return Optional.empty();
        }
        Class<?> type = opMethod.get().getReturnType();
        if(type.isAssignableFrom(Void.class)){
            return Optional.empty();
        }
        return Optional.of((Class<Object>)type);
    }
}
