package org.block.jsf;

import org.block.jsf.data.JSFPart;
import org.block.jsf.data.jsfclass.JSFClass;
import org.block.serialization.ConfigNode;

import java.util.List;

public class JavaStructureNodeReader {

    private final ConfigNode node;

    public JavaStructureNodeReader(ConfigNode node){
        this.node = node;
    }

    public ConfigNode getNode(){
        return this.node;
    }

    public List<JSFClass> read(){
        return JSFPart.TITLE_CLASSES.deserialize(this.node).get();
    }
}
