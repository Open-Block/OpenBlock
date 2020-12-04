package org.block.jsf.data.generic;

import org.block.jsf.data.JSFPart;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JSFGeneric implements JSFPart<JSFGeneric> {

    public static class Builder implements JSFPart.Builder<JSFGeneric>{

        private String reference;
        private boolean isExtending;
        private List<String> extending = new ArrayList<>();

        public Builder setReferenceName(String name){
            this.reference = name;
            return this;
        }

        public Builder setExtending(boolean check){
            this.isExtending = check;
            return this;
        }

        public Builder addClasses(Collection<String> collection){
            this.extending.addAll(collection);
            return this;
        }

        @Override
        public JSFGeneric build() {
            return new JSFGeneric(this.reference, this.isExtending, this.extending);
        }

        @Override
        public Builder clone() {
            Builder builder = new Builder();
            builder.extending.addAll(this.extending);
            builder.isExtending = this.isExtending;
            builder.reference = this.reference;
            return builder;
        }

        @Override
        public Optional<JSFGeneric> deserialize(@NotNull ConfigNode node, @NotNull String key) {
            ConfigNode base = node.getNode(key);
            this.reference = JSFPart.TITLE_NAME.deserialize(base).orElse(this.reference);
            this.isExtending = JSFPart.TITLE_IS_EXTENDING.deserialize(base).orElse(this.isExtending);
            this.extending = JSFPart.TITLE_IMPLEMENTING.deserialize(base).get();
            return Optional.ofNullable(this.build());
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull JSFGeneric value) {
            ConfigNode base = node.getNode(key);
            JSFPart.TITLE_NAME.serialize(base, value.getRepresentation());
            JSFPart.TITLE_IS_EXTENDING.serialize(base, value.isExtending());
            JSFPart.TITLE_IMPLEMENTING.serialize(base, value.getClasses());
        }
    }

    private final String representation;
    private final boolean extended;
    private final List<String> classes = new ArrayList<>();

    public JSFGeneric(String representation, boolean extended, Collection<String> collection){
        this.representation = representation;
        this.extended = extended;
        this.classes.addAll(collection);
    }

    public String getRepresentation(){
        return this.representation;
    }

    public boolean isExtending(){
        return this.extended;
    }

    public List<String> getClasses(){
        return this.classes;
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.extending = this.getClasses();
        builder.isExtending = this.isExtending();
        builder.reference = this.getRepresentation();
        return builder;
    }
}
