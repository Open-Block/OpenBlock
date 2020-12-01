package org.block.jsf.data.jsfvalue;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;

public abstract class JSFValue<V extends JSFValue<?>> implements JSFPart<V> {

    private final String name;
    private final String type;
    private final boolean isFinal;

    public JSFValue(boolean isFinal, String type, String name){
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
