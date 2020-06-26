package org.block.serializtion.json;

import org.block.serializtion.ConfigImplementation;
import org.block.serializtion.ConfigNode;
import org.json.JSONObject;
import org.json.JSONWriter;

public class JSONConfigImplementation implements ConfigImplementation {

    @Override
    public JSONConfigNode createEmptyNode() {
        return new JSONConfigNode(new JSONObject());
    }

    @Override
    public JSONConfigNode load(String json) {
        if(json.length() == 0){
            return new JSONConfigNode(new JSONObject());
        }
        return new JSONConfigNode(new JSONObject(json));
    }

    @Override
    public String write(ConfigNode node) {
        if(!(node instanceof JSONConfigNode)){
            throw new IllegalArgumentException("Node is not JSON");
        }
        return JSONWriter.valueToString(((JSONConfigNode)node).getPath());
    }
}
