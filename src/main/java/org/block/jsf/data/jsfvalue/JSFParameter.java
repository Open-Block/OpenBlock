package org.block.jsf.data.jsfvalue;

import org.block.jsf.data.JSFPart;
import org.block.serialization.ConfigNode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class JSFParameter extends JSFValue<JSFParameter> {

    public JSFParameter(boolean isFinal, String type, String name) {
        super(isFinal, type, name);
    }

    @Override
    public Builder toBuilder() {
        return null;
    }

    public static class Builder implements JSFPart.Builder<JSFParameter> {

        private String name;
        private String type;
        private boolean isFinal;

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public boolean isFinal() {
            return this.isFinal;
        }

        public Builder setFinal(boolean aFinal) {
            this.isFinal = aFinal;
            return this;
        }

        @Override
        public JSFParameter build() {
            if (this.name == null) {
                throw new IllegalStateException("Parameter name cannot be null");
            }
            if (this.type == null) {
                throw new IllegalStateException("Parameter type cannot be null");
            }
            return new JSFParameter(this.isFinal, this.type, this.name);
        }

        @Override
        public Builder clone() {
            Builder builder = new Builder();
            builder.setFinal(this.isFinal);
            builder.setType(this.type);
            builder.setName(this.name);
            return builder;
        }

        @Override
        public Optional<JSFParameter> deserialize(@NotNull ConfigNode node, @NotNull String key) {
            ConfigNode base = node.getNode(key);
            this.isFinal = JSFPart.TITLE_IS_FINAL.deserialize(base).orElse(true);
            this.name = JSFPart.TITLE_NAME.deserialize(base).orElse(this.name);
            this.type = JSFPart.TITLE_RETURN.deserialize(base).orElse(this.type);
            return Optional.of(this.build());
        }

        @Override
        public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull JSFParameter value) {
            ConfigNode base = node.getNode(key);
            JSFPart.TITLE_IS_FINAL.serialize(base, value.isFinal());
            JSFPart.TITLE_NAME.serialize(base, value.getName());
            JSFPart.TITLE_RETURN.serialize(base, value.getType());
        }
    }
}
