package org.block.serialization.parse.specific;

import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EnumParser<E extends Enum<?>> implements Parser<E> {

    private final Class<E> clazz;

    public EnumParser(Class<E> enumClass){
        this.clazz = enumClass;
    }
    @Override
    public Optional<E> deserialize(@NotNull ConfigNode node, @NotNull String key) {
        Optional<String> opString = node.getString(key);
        if(!opString.isPresent()){
            return Optional.empty();
        }
        for (Enum<?> value : this.clazz.getEnumConstants()){
            if(value.name().equals(opString.get())){
                return Optional.of((E)value);
            }
        }
        return Optional.empty();
    }

    @Override
    public void serialize(@NotNull ConfigNode node, @NotNull String key, @NotNull E value) {
        node.setValue(key, value.name());
    }
}
