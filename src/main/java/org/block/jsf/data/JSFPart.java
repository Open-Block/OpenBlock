package org.block.jsf.data;

import org.block.jsf.data.jsffunction.JSFConstructor;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.jsf.data.jsfvalue.JSFValue;
import org.block.serialization.ConfigNode;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Deserialize;
import org.block.serialization.parse.Parser;
import org.block.serialization.parse.Serialize;
import org.block.serialization.parse.specific.EnumParser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface JSFPart<P extends JSFPart<?>> {

    JSFParameter.Builder BUILDER_PARAMETER = new JSFParameter.Builder();
    JSFConstructor.Builder BUILDER_CONSTRUCTOR = new JSFConstructor.Builder();

    FixedTitle<Visibility> TITLE_VISIBILITY = new FixedTitle<>("visibility", new EnumParser<>(Visibility.class));
    FixedTitle<Boolean> TITLE_IS_STATIC = new FixedTitle<>("isStatic", Parser.BOOLEAN);
    FixedTitle<Boolean> TITLE_IS_FINAL = new FixedTitle<>("isFinal", Parser.BOOLEAN);
    FixedTitle<String> TITLE_NAME = new FixedTitle<>("name", Parser.STRING);
    FixedTitle<String> TITLE_RETURN = new FixedTitle<>("return", Parser.STRING);
    FixedTitle.Listable<JSFParameter> TITLE_PARAMETERS = new FixedTitle.Listable<>("parameters", BUILDER_PARAMETER);


    interface Builder<B extends JSFPart<?>> extends Parser<B>, Cloneable {

        B build();
        Builder<B> clone();

    }

    Builder<P> toBuilder();


}
