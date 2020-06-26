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
        String singleLined = JSONWriter.valueToString(((JSONConfigNode)node).getPath());
        String newPage = "";
        int tab = 0;
        boolean isInArray = false;
        boolean isInString = false;
        boolean tabBefore = false;
        for(int A = 0; A < singleLined.length(); A++){
            char at = singleLined.charAt(A);
            if(isInString){
                if(at == '"'){
                    isInString = false;
                    newPage += '"';
                    continue;
                }
                newPage += '"';
                continue;
            }
            if(at == '{' || at == '['){
                tab++;
                newPage += at + "\n";
                tabBefore = true;
                continue;
            }
            if(at == '}' || at == ']'){
                tab--;
                if(tabBefore) {
                    newPage += tab(tab);
                }
                if((A + 1) != singleLined.length() && singleLined.charAt(A + 1) == ','){
                    newPage += at + ",\n";
                }else{
                    newPage += at + "\n";
                }
                continue;
            }
            if(tabBefore){
                tabBefore = false;
                newPage += tab(tab);
            }
            newPage += at;
        }
        return newPage;
    }

    private String tab(int A){
        String value = "";
        for(int B = 0; B < A; B++){
            value += "\t";
        }
        return value;
    }
}
