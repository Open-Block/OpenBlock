package org.block.project.block.java.method.call;

import org.array.utils.ArrayUtils;
import org.block.project.block.Block;
import org.block.project.block.BlockType;
import org.block.project.block.group.BlockSector;
import org.block.project.block.java.value.StringBlock;
import org.block.project.block.type.attachable.AbstractAttachableBlock;
import org.block.project.block.type.value.ValueBlock;
import org.block.serialization.ConfigNode;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaMethodCallBlock<C> extends AbstractAttachableBlock implements MethodCallBlock {

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
        super(x, y);
        this.type = type;

        //this.attached.put(MethodCallBlock.METHOD_BLOCK_GROUP, new JavaMethodNameBlockList(5));
    }

    public Class<C> getTargetClass(){
        return this.type.targetClass;
    }

    public Set<Method> getSuggestedMethods(){
        Optional<StringBlock> opName = this.getNameBlockGroup().getSector().getAttachedBlock();
        if(opName.isEmpty()){
            return Collections.emptySet();
        }
        String name = opName.get().getValue().replaceAll(" ", "");
        return Stream.of(this.getTargetClass().getMethods()).parallel().filter(m -> m.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toSet());
    }

    public Optional<Method> getExpectedMethod(){
        Optional<StringBlock> opName = this.getNameBlockGroup().getSector().getAttachedBlock();
        if(opName.isEmpty()){
            return Optional.empty();
        }
        String name = opName.get().getValue().replaceAll(" ", "");
        Set<Method> suggestedMethods = this.getSuggestedMethods();
        Set<Method> exactMethod = suggestedMethods.parallelStream().filter(m -> m.getName().equalsIgnoreCase(name)).collect(Collectors.toSet());
        if(exactMethod.isEmpty()) {
            return suggestedMethods.parallelStream().filter(m -> m.getName().toLowerCase().startsWith(name.toLowerCase())).sorted().findFirst();
        }
        MethodParameterBlockGroup parameterBlockGroup = this.getParametersBlockGroup();
        if(exactMethod.size() == 1){
            return exactMethod.stream().findFirst();
        }
        if(parameterBlockGroup.getSectors().size() == 0){
            return ArrayUtils.getBest(Method::getParameterCount, (c1, c2) -> c1 < c2, exactMethod);
        }
        Set<Method> exactCountMethod = exactMethod.parallelStream().filter(m -> m.getParameterCount() == parameterBlockGroup.getSectors().size()).collect(Collectors.toSet());
        if(exactCountMethod.size() == 1){
            return exactCountMethod.stream().findFirst();
        }
        if(exactCountMethod.isEmpty()){
            //TODO filter out impossible functions
            return ArrayUtils.getBest(Method::getParameterCount, (c1, c2) -> c1 < c2, exactMethod);
        }
        //TODO filter out impossible functions
        return exactMethod.stream().findFirst();

    }

    @Override
    public String writeCode(int tabs) {
        Optional<Method> opMethod = this.getExpectedMethod();
        if(opMethod.isEmpty()){
            throw new IllegalStateException("Unknown Method");
        }
        String name = opMethod.get().getName();
        Parameter[] parameters = opMethod.get().getParameters();
        MethodParameterBlockGroup parameterBlockGroup = this.getParametersBlockGroup();
        if(parameters.length != parameterBlockGroup.getSectors().size()){
            throw new IllegalStateException("Method not found");
        }
        for(int i = 0; i < parameters.length; i++){
            Parameter parameter = parameters[i];
            Optional<? extends Block> opBlock = parameterBlockGroup.getSectors().get(i).getAttachedBlock();
            if(opBlock.isEmpty()){
                throw new IllegalStateException("Method not found");
            }
            if(!(opBlock.get() instanceof ValueBlock)){
                throw new IllegalStateException("Block in parameter " + i + " does not have a value and therefore cannot be placed as a parameter");
            }
            ValueBlock<?> valueBlock = (ValueBlock<?>) opBlock.get();
            if(valueBlock.getExpectedValue().isEmpty()){
                throw new IllegalStateException("Block in parameter " + i + " does not have a value and therefore cannot be placed as a parameter");
            }
            if(!valueBlock.getExpectedValue().get().isAssignableFrom(parameter.getType())){
                throw new IllegalStateException("Parameter " + i + " expects value of " + parameter.getType().getSimpleName() + " however the provided block gives a value of " + valueBlock.getExpectedValue().get().getSimpleName());
            }
        }
        return name + "(" + ArrayUtils.toString(", ", t -> t.getAttachedBlock().get().writeCode(0), parameterBlockGroup.getSectors()) + ")";
    }

    @Override
    public Collection<String> getCodeImports() {
        Set<String> imports = new HashSet<>();
        MethodParameterBlockGroup parameterBlockGroup = this.getParametersBlockGroup();
        for (BlockSector<?> sector : parameterBlockGroup.getSectors()){
            if(sector.getAttachedBlock().isEmpty()){
                continue;
            }
            imports.addAll(sector.getAttachedBlock().get().getCodeImports());
        }
        return imports;
    }

    @Override
    public BlockType<? extends Block> getType() {
        return this.type;
    }

    @Override
    public Optional<Class<Object>> getExpectedValue() {
        Optional<Method> opMethod = this.getExpectedMethod();
        if(opMethod.isEmpty()){
            return Optional.empty();
        }
        Class<?> type = opMethod.get().getReturnType();
        if(type.isAssignableFrom(Void.class)){
            return Optional.empty();
        }
        return Optional.of((Class<Object>)type);
    }
}
