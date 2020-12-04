package org.block.jsf;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.serialization.ConfigImplementation;
import org.block.serialization.json.JSONConfigImplementation;
import org.block.serialization.json.JSONConfigNode;

import java.io.*;
import java.util.*;

public class JavaStructureWriter {

    private final OutputStream stream;
    private final TreeSet<JSFClass> classes = new TreeSet<>(Comparator.comparing(JSFClass::getClassName));

    public JavaStructureWriter(OutputStream file){
        this.stream = file;
    }

    public OutputStream getStream(){
        return this.stream;
    }

    public void addClasses(JSFClass... collection){
        this.addClasses(Arrays.asList(collection));
    }

    public void addClasses(Collection<JSFClass> collection){
        this.classes.addAll(collection);
    }

    public void write() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(this.stream);
        JSONConfigImplementation implementation = ConfigImplementation.JSON;
        JSONConfigNode jsonNode = implementation.createEmptyNode();
        JSFPart.TITLE_CLASSES.serialize(jsonNode, new ArrayList<>(this.classes));
        String json = implementation.write(jsonNode);
        writer.write(json);
        writer.flush();
        writer.close();
    }




}
