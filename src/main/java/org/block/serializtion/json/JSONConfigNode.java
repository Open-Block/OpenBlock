package org.block.serializtion.json;

import org.block.serializtion.ConfigNode;
import org.block.serializtion.parse.Parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Optional;

public class JSONConfigNode implements ConfigNode {

    private final JSONObject path;

    public JSONConfigNode(JSONObject obj){
        this.path = obj;
    }

    JSONObject getPath(){
        return this.path;
    }

    @Override
    public ConfigNode getNode(String... path) {
        JSONObject obj = this.path;
        for(String key : path){
            try {
                obj = obj.getJSONObject(key);
            }catch (JSONException e){
                JSONObject obj1 = new JSONObject();
                obj.put(key, obj1);
                obj = obj.getJSONObject(key);
            }
        }
        return new JSONConfigNode(obj);
    }

    @Override
    public Optional<String> getString(String title) {
        return Optional.ofNullable(this.path.optString(title));
    }

    @Override
    public Optional<Integer> getInteger(String title) {
        return Optional.ofNullable(this.path.optInt(title));
    }

    @Override
    public Optional<Long> getLong(String title) {
        return Optional.ofNullable(this.path.optLong(title));
    }

    @Override
    public Optional<Double> getDouble(String title) {
        return Optional.ofNullable(this.path.optDouble(title));
    }

    @Override
    public Optional<Float> getFloat(String title) {
        return Optional.ofNullable(this.path.optFloat(title));
    }

    @Override
    public <T> Optional<T> getValue(String title, Parser<T> parser) {
        try {
            return parser.deserialize(this, title);
        }catch (IllegalStateException e){
            return Optional.empty();
        }
    }

    @Override
    public <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(title);
        }catch (JSONException e){
            return into;
        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        for(int A = 0; A < array.length(); A++){
            try {
                parser.deserialize(node, A + "").ifPresent(e -> into.add(e));
            }catch (IllegalStateException e){
                continue;
            }
        }
        return into;
    }

    @Override
    public void setValue(String title, Object value) {
        this.path.put(title, value);
    }

    @Override
    public <T> void setValue(String title, Parser<T> parser, T value) {
        parser.serialize(this, title, value);
    }

    @Override
    public void setCollection(String title, Collection<?> collection) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(title);
        }catch (JSONException e){
            JSONArray jArray = new JSONArray();
            this.path.put(title, jArray);
            array = this.path.getJSONArray(title);
        }
        int index = 0;
        for(Object value : collection){
            array.put(index, value);
            index++;
        }
    }

    @Override
    public <T> void setCollection(String title, Parser<T> parser, Collection<T> collection) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(title);
        }catch (JSONException e){
            array = new JSONArray();
            this.path.put(title, array);
        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        int index = 0;
        for(T value : collection){
            parser.serialize(node, index + "", value);
            index++;
        }
    }
}
