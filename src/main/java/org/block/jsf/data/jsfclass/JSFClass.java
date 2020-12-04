package org.block.jsf.data.jsfclass;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;
import org.block.jsf.data.generic.JSFGeneric;
import org.block.jsf.data.jsffunction.JSFConstructor;
import org.block.jsf.data.jsffunction.JSFFunction;
import org.block.jsf.data.jsffunction.JSFMethod;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JSFClass implements JSFPart<JSFClass> {

    public static class Builder implements JSFPart.Builder<JSFClass>{

        private JSFClassType classType;
        private String className;
        private boolean isAbstract;
        private boolean isFinal;
        private String extendingClass;
        private Visibility visibility;
        private TreeSet<String> implementingClasses = new TreeSet<>();
        private TreeSet<JSFConstructor> constructors = new TreeSet<>();
        private TreeSet<JSFMethod> methods = new TreeSet<>();
        private List<JSFGeneric> generics = new ArrayList<>();

        public Builder setVisibility(Visibility visibility){
            this.visibility = visibility;
            return this;
        }

        public Builder setType(JSFClassType type){
            this.classType = type;
            return this;
        }

        public Builder setClass(String name){
            this.className = name;
            return this;
        }

        public Builder setAbstract(boolean isAbstract){
            this.isAbstract = isAbstract;
            return this;
        }

        public Builder setExtending(String name){
            this.extendingClass = name;
            return this;
        }

        public Builder setFinal(boolean isFinal){
            this.isFinal = isFinal;
            return this;
        }

        public Builder addImplementations(Collection<String> collection){
            this.implementingClasses.addAll(collection);
            return this;
        }

        public Builder addMethods(Collection<JSFMethod> methods){
            this.methods.addAll(methods);
            return this;
        }

        @Override
        public JSFClass build() {
            if(this.className == null){
                throw new IllegalStateException("ClassName cannot be null");
            }
            if(this.classType == null){
                throw new IllegalStateException("ClassType cannot be null");
            }
            if(this.visibility == null){
                throw new IllegalStateException("Visibility cannot be null");
            }
            JSFClass jsfClass = new JSFClass(this.visibility, this.classType, this.className, this.isFinal, this.isAbstract, this.extendingClass);
            jsfClass.constructors.addAll(this.constructors);
            jsfClass.generics.addAll(this.generics);
            jsfClass.implementingClasses.addAll(this.implementingClasses);
            jsfClass.methods.addAll(this.methods);
            return jsfClass;
        }

        @Override
        public Builder clone() {
            Builder builder = new Builder();
            builder.className = this.className;
            builder.classType = this.classType;
            builder.isFinal = this.isFinal;
            builder.visibility = this.visibility;
            builder.constructors.addAll(this.constructors);
            builder.extendingClass = this.extendingClass;
            builder.generics.addAll(this.generics);
            builder.implementingClasses.addAll(this.implementingClasses);
            builder.isAbstract = this.isAbstract;
            builder.methods.addAll(this.methods);
            return builder;
        }

        @Override
        public Optional<JSFClass> deserialize(@NotNull ConfigNode node, @NotNull String key) {
            ConfigNode base = node.getNode(key);
            this.methods.addAll(JSFPart.TITLE_METHODS.deserialize(base).get());
            this.isAbstract = JSFPart.TITLE_IS_ABSTRACT.deserialize(base).orElse(this.isAbstract);
            this.extendingClass = JSFPart.TITLE_EXTENDING.deserialize(base).orElse(this.extendingClass);
            this.className = JSFPart.TITLE_NAME.deserialize(base).orElse(this.className);
            this.classType = JSFPart.TITLE_CLASS_TYPE.deserialize(base).orElse(this.classType);
            this.implementingClasses.addAll(JSFPart.TITLE_IMPLEMENTING.deserialize(base).get());
            this.generics.addAll(JSFPart.TITLE_GENERICS.deserialize(base).get());
            this.constructors.addAll(JSFPart.TITLE_CONSTRUCTORS.deserialize(base).get());
            this.isFinal = JSFPart.TITLE_IS_FINAL.deserialize(base).orElse(this.isFinal);
            this.visibility = JSFPart.TITLE_VISIBILITY.deserialize(base).orElse(this.visibility);
            return Optional.ofNullable(this.build());
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull JSFClass value) {
            ConfigNode base = node.getNode(key);
            JSFClass.TITLE_GENERICS.serialize(base, value.getGenerics());
            JSFClass.TITLE_IMPLEMENTING.serialize(base, new ArrayList<>(value.getImplementingClasses()));
            JSFClass.TITLE_IS_ABSTRACT.serialize(base, value.isAbstract());
            JSFClass.TITLE_NAME.serialize(base, value.getClassName());
            JSFClass.TITLE_CLASS_TYPE.serialize(base, value.getClassType());
            value.getExtendingClass().ifPresent(v -> JSFClass.TITLE_EXTENDING.serialize(base, v));
            JSFClass.TITLE_CONSTRUCTORS.serialize(base, new ArrayList<>(value.getConstructors()));
            JSFClass.TITLE_METHODS.serialize(base, new ArrayList<>(value.getMethods()));
            JSFClass.TITLE_IS_FINAL.serialize(base, value.isFinal());
            JSFClass.TITLE_VISIBILITY.serialize(base, value.getVisibility());
        }
    }

    private final Visibility visibility;
    private final boolean isFinal;
    private final JSFClassType classType;
    private final String className;
    private final boolean isAbstract;
    private final String extendingClass;
    private final TreeSet<String> implementingClasses = new TreeSet<>();
    private final TreeSet<JSFConstructor> constructors = new TreeSet<>();
    private final TreeSet<JSFMethod> methods = new TreeSet<>();
    private final List<JSFGeneric> generics = new ArrayList<>();

    public JSFClass(Visibility visibility, JSFClassType classType, String className, boolean isFinal, boolean isAbstract, String extendingClass) {
        this.classType = classType;
        this.className = className;
        this.isAbstract = isAbstract;
        this.extendingClass = extendingClass;
        this.visibility = visibility;
        this.isFinal = isFinal;
    }

    public boolean isFinal(){
        return this.isFinal;
    }

    public Visibility getVisibility(){
        return this.visibility;
    }

    public JSFClassType getClassType() {
        return classType;
    }

    public String getClassName() {
        return className;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public Optional<String> getExtendingClass() {
        return Optional.ofNullable(extendingClass);
    }

    public TreeSet<String> getImplementingClasses() {
        return implementingClasses;
    }

    public TreeSet<JSFConstructor> getConstructors() {
        return constructors;
    }

    public TreeSet<JSFMethod> getMethods() {
        return methods;
    }

    public List<JSFGeneric> getGenerics() {
        return generics;
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        //TODO apply values
        return builder;
    }
}
