package org.block.jsf.data.jsffunction;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.Visibility;
import org.block.jsf.data.generic.JSFGeneric;
import org.block.jsf.data.jsfvalue.JSFParameter;

import java.util.ArrayList;
import java.util.List;

public abstract class JSFFunction<F extends JSFFunction<?>> implements Comparable<F>, JSFPart<F> {

    private final Visibility visibility;
    private final String returning;
    private final boolean isStatic;
    private final boolean isFinal;
    private final List<JSFGeneric> generics = new ArrayList<>();
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

    public List<JSFGeneric> getGenerics() {
        return generics;
    }

    public List<JSFParameter> getParameters() {
        return parameters;
    }

    @Override
    public int compareTo(F o) {
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
}
