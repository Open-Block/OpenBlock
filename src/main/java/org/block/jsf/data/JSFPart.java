package org.block.jsf.data;

import org.block.jsf.data.generic.JSFGeneric;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.jsf.data.jsfclass.JSFClassType;
import org.block.jsf.data.jsffunction.JSFConstructor;
import org.block.jsf.data.jsffunction.JSFMethod;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.serialization.FixedTitle;
import org.block.serialization.parse.Parser;
import org.block.serialization.parse.specific.EnumParser;

public interface JSFPart<P extends JSFPart<?>> {

    JSFParameter.Builder BUILDER_PARAMETER = new JSFParameter.Builder();
    JSFConstructor.Builder BUILDER_CONSTRUCTOR = new JSFConstructor.Builder();
    JSFMethod.Builder BUILDER_METHOD = new JSFMethod.Builder();
    JSFGeneric.Builder BUILDER_GENERICS = new JSFGeneric.Builder();
    JSFClass.Builder BUILDER_CLASS = new JSFClass.Builder();

    FixedTitle<Visibility> TITLE_VISIBILITY = new FixedTitle<>("visibility", new EnumParser<>(Visibility.class));
    FixedTitle<Boolean> TITLE_IS_STATIC = new FixedTitle<>("isStatic", Parser.BOOLEAN);
    FixedTitle<Boolean> TITLE_IS_FINAL = new FixedTitle<>("isFinal", Parser.BOOLEAN);
    FixedTitle<Boolean> TITLE_IS_ABSTRACT = new FixedTitle<>("isAbstract", Parser.BOOLEAN);
    FixedTitle<Boolean> TITLE_IS_EXTENDING = new FixedTitle<>("isExtending", Parser.BOOLEAN);
    FixedTitle<JSFClassType> TITLE_CLASS_TYPE = new FixedTitle<>("classType", new EnumParser<>(JSFClassType.class));
    FixedTitle<String> TITLE_NAME = new FixedTitle<>("name", Parser.STRING);
    FixedTitle<String> TITLE_RETURN = new FixedTitle<>("return", Parser.STRING);
    FixedTitle<String> TITLE_EXTENDING = new FixedTitle<>("extending", Parser.STRING);
    FixedTitle.Listable<JSFGeneric> TITLE_GENERICS = new FixedTitle.Listable<>("generics", BUILDER_GENERICS);
    FixedTitle.Listable<String> TITLE_IMPLEMENTING = new FixedTitle.Listable<>("implementing", Parser.STRING);
    FixedTitle.Listable<JSFParameter> TITLE_PARAMETERS = new FixedTitle.Listable<>("parameters", BUILDER_PARAMETER);
    FixedTitle.Listable<JSFConstructor> TITLE_CONSTRUCTORS = new FixedTitle.Listable<>("constructor", BUILDER_CONSTRUCTOR);
    FixedTitle.Listable<JSFMethod> TITLE_METHODS = new FixedTitle.Listable<>("method", BUILDER_METHOD);
    FixedTitle.Listable<JSFClass> TITLE_CLASSES = new FixedTitle.Listable<>("classes", BUILDER_CLASS);

    Builder<P> toBuilder();

    interface Builder<B extends JSFPart<?>> extends Parser<B>, Cloneable {

        B build();

        Builder<B> clone();

    }


}
