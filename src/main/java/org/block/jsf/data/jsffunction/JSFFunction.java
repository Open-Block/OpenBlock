package org.block.jsf.data.jsffunction;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.jsf.data.jsfvalue.JSFValue;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public abstract class JSFFunction<F extends JSFFunction<?>> implements Comparable<F>, JSFPart<F> {

    private final Visibility visibility;
    private final String returning;
    private final boolean isStatic;
    private final boolean isFinal;
    private final TreeMap<String, String> generics = new TreeMap<>();
    private final List<JSFParameter> parameters = new ArrayList<>();

    public abstract String getName();

    public JSFFunction(Visibility visibility, boolean isFinal, boolean isStatic, String returning){
        this.visibility = visibility;
        this.returning = returning;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getReturning() {
        return returning;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public TreeMap<String, String> getGenerics() {
        return generics;
    }

    public List<JSFParameter> getParameters() {
        return parameters;
    }
}
