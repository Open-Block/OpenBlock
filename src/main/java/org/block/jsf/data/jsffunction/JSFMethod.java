package org.block.jsf.data.jsffunction;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;
import org.block.jsf.data.generic.JSFGeneric;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JSFMethod extends JSFFunction<JSFMethod> {

    public static class Builder implements JSFPart.Builder<JSFMethod> {

        private Visibility visibility = Visibility.PUBLIC;
        private String returning;
        private String name;
        private boolean isFinal;
        private boolean isAbstract;
        private boolean isStatic;
        private List<JSFGeneric> generics = new ArrayList<>();
        private List<JSFParameter> parameters = new ArrayList<>();

        public String getName(){
            return this.name;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Visibility getVisibility() {
            return visibility;
        }

        public Builder setVisibility(Visibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public String getReturning() {
            return returning;
        }

        public Builder setReturning(String returning) {
            this.returning = returning;
            return this;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public Builder setFinal(boolean aFinal) {
            isFinal = aFinal;
            return this;
        }

        public List<JSFGeneric> getGenerics() {
            return generics;
        }

        public List<JSFParameter> getParameters() {
            return parameters;
        }

        public Builder addParameters(JSFParameter... collection){
            return addParameters(Arrays.asList(collection));
        }

        public Builder addParameters(Collection<JSFParameter> collection){
            this.parameters.addAll(collection);
            return this;
        }

        @Override
        public JSFMethod build() {
            if(this.visibility == null){
                throw new IllegalStateException("Visibility cannot be null");
            }
            if(this.returning == null){
                throw new IllegalStateException("Returning value cannot be null");
            }
            if(this.name == null){
                throw new IllegalStateException("Name value cannot be null");
            }
            JSFMethod cons = new JSFMethod(this.visibility, this.isFinal, this.isStatic, this.returning, this.name, this.isAbstract);
            cons.getParameters().addAll(this.parameters);
            cons.getGenerics().addAll(this.generics);
            return cons;
        }

        @Override
        public Builder clone() {
            Builder builder = new Builder();
            builder.parameters.addAll(this.parameters);
            builder.returning = this.returning;
            builder.isFinal = this.isFinal;
            builder.visibility = this.visibility;
            builder.name = this.name;
            builder.isAbstract = this.isAbstract;
            builder.isStatic = this.isStatic;
            builder.generics.addAll(this.generics);
            return builder;
        }

        @Override
        public Optional<JSFMethod> deserialize(@NotNull ConfigNode node, @NotNull String key) {
            Builder builder = new Builder();
            ConfigNode base = node.getNode(key);
            builder.name = JSFPart.TITLE_NAME.deserialize(base).orElse(this.name);
            builder.visibility = JSFPart.TITLE_VISIBILITY.deserialize(base).orElse(this.visibility);
            builder.isAbstract = JSFPart.TITLE_IS_ABSTRACT.deserialize(base).orElse(this.isAbstract);
            builder.isStatic = JSFPart.TITLE_IS_STATIC.deserialize(base).orElse(this.isStatic);
            builder.generics = JSFPart.TITLE_GENERICS.deserialize(base).get();
            builder.parameters = JSFPart.TITLE_PARAMETERS.deserialize(base).get();
            return Optional.empty();
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull JSFMethod value) {
            ConfigNode base = node.getNode(key);
            JSFPart.TITLE_RETURN.serialize(base, value.getReturning());
            JSFPart.TITLE_NAME.serialize(base, value.getName());
            JSFPart.TITLE_IS_FINAL.serialize(base, value.isFinal());
            JSFPart.TITLE_VISIBILITY.serialize(base, value.getVisibility());
            JSFPart.TITLE_PARAMETERS.serialize(base, value.getParameters());
            JSFPart.TITLE_IS_STATIC.serialize(base, value.isStatic());
            JSFPart.TITLE_IS_ABSTRACT.serialize(base, value.isAbstract());
            JSFPart.TITLE_GENERICS.serialize(base, value.getGenerics());
        }
    }

    private final String name;
    private final boolean isAbstract;

    public JSFMethod(Visibility visibility, boolean isFinal, boolean isStatic, String returning, String name, boolean isAbstract) {
        super(visibility, isFinal, isStatic, returning);
        this.name = name;
        this.isAbstract = isAbstract;
    }

    public boolean isAbstract(){
        return this.isAbstract;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(@NotNull JSFMethod o) {
        int stringCompare = o.getName().compareTo(this.getName());
        if(stringCompare != 0){
            return stringCompare;
        }
        return super.compareTo(o);
    }

    @Override
    public JSFMethod.Builder toBuilder() {
        Builder builder = new Builder();
        builder.generics.addAll(this.getGenerics());
        builder.isFinal = this.isFinal();
        builder.parameters.addAll(this.getParameters());
        builder.visibility = this.getVisibility();
        builder.returning = this.getReturning();
        builder.isStatic = this.isStatic();
        builder.isAbstract = this.isAbstract();
        builder.name = this.getName();
        return builder;
    }
}
