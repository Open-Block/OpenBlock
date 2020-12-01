package org.block.jsf.data.jsffunction;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JSFConstructor extends JSFFunction<JSFConstructor> {

    public static class Builder implements JSFPart.Builder<JSFConstructor> {

        private Visibility visibility = Visibility.PUBLIC;
        private String returning;
        private boolean isFinal;
        private final TreeMap<String, String> generics = new TreeMap<>();
        private final List<JSFParameter> parameters = new ArrayList<>();

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

        public TreeMap<String, String> getGenerics() {
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
        public JSFConstructor build() {
            if(this.visibility == null){
                throw new IllegalStateException("Visibility cannot be null");
            }
            if(this.returning == null){
                throw new IllegalStateException("Returning value cannot be null");
            }
            JSFConstructor cons = new JSFConstructor(this.visibility, this.isFinal, this.returning);
            cons.getParameters().addAll(this.parameters);
            cons.getGenerics().putAll(this.generics);
            return cons;
        }

        @Override
        public Builder clone() {
            Builder builder = new Builder();
            builder.parameters.addAll(this.parameters);
            builder.returning = this.returning;
            builder.isFinal = this.isFinal;
            builder.visibility = this.visibility;
            builder.generics.putAll(this.generics);
            return builder;
        }

        @Override
        public Optional<JSFConstructor> deserialize(@NotNull ConfigNode node, @NotNull String key) {
            return Optional.empty();
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull JSFConstructor value) {
            ConfigNode base = node.getNode(key);
            JSFPart.TITLE_RETURN.serialize(base, value.getReturning());
            JSFPart.TITLE_IS_FINAL.serialize(base, value.isFinal());
            JSFPart.TITLE_VISIBILITY.serialize(base, value.getVisibility());
            JSFPart.TITLE_PARAMETERS.serialize(base, value.getParameters());
        }
    }

    public JSFConstructor(Visibility visibility, boolean isFinal, String returning) {
        super(visibility, isFinal, false, returning);
    }

    @Override
    public String getName() {
        return this.getReturning();
    }

    @Override
    public int compareTo(JSFConstructor o) {
        List<JSFParameter> parameters = this.getParameters();
        List<JSFParameter> otherParameters = o.getParameters();
        if(parameters.isEmpty()){
            return -1;
        }
        if(otherParameters.isEmpty()){
            return 1;
        }
        int similar = 0;
        int diff = parameters.size() - otherParameters.size();
        if(diff < 0){
            diff = -diff;
        }
        for(int A = 0; A < Math.min(parameters.size(), otherParameters.size()); A++){
            if(parameters.get(A).equals(otherParameters.get(A))){
                similar++;
            }
        }
        return similar - diff;
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.generics.putAll(this.getGenerics());
        builder.isFinal = this.isFinal();
        builder.parameters.addAll(this.getParameters());
        builder.visibility = this.getVisibility();
        builder.returning = this.getReturning();
        return builder;
    }
}
