package org.block.jsf.data.jsfclass;

import org.block.jsf.data.jsffunction.JSFFunction;

import java.util.TreeMap;
import java.util.TreeSet;

public class JSFClass {

    private final JSFClassType classType;
    private final String className;
    private final boolean isAbstract;
    private final String classPackage;
    private final String extendingClass;
    private final TreeSet<String> implementingClasses = new TreeSet<>();
    private final TreeSet<JSFFunction> constructors = new TreeSet<>();
    private final TreeSet<JSFFunction> methods = new TreeSet<>();
    private final TreeMap<String, String> generics = new TreeMap<>();

    public JSFClass(JSFClassType classType, String className, boolean isAbstract, String classPackage, String extendingClass) {
        this.classType = classType;
        this.className = className;
        this.isAbstract = isAbstract;
        this.classPackage = classPackage;
        this.extendingClass = extendingClass;
    }

    public JSFClassType getClassType() {
        return classType;
    }

    public String getClassName() {
        return className;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public String getExtendingClass() {
        return extendingClass;
    }

    public TreeSet<String> getImplementingClasses() {
        return implementingClasses;
    }

    public TreeSet<JSFFunction> getConstructors() {
        return constructors;
    }

    public TreeSet<JSFFunction> getMethods() {
        return methods;
    }

    public TreeMap<String, String> getGenerics() {
        return generics;
    }
}
