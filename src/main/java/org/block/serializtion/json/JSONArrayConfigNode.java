package org.block.serializtion.json;

import org.block.serializtion.ConfigNode;
import org.block.serializtion.parse.Parser;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;
import java.util.Optional;

public class JSONArrayConfigNode implements ConfigNode {

    private final JSONArray path;

    public JSONArrayConfigNode(JSONArray path){
        this.path = path;
    }

    @Override
    public ConfigNode getNode(String... path) {
        if(path.length == 0){
            return this;
        }
        int entry = Integer.parseInt(path[0]);
        JSONConfigNode node = new JSONConfigNode(this.path.getJSONObject(entry));
        String[] newPath = new String[path.length - 1];
        for(int A = 0; A < path.length - 1; A++){
            newPath[A] = path[A + 1];
        }
        return node.getNode(newPath);
    }

    @Override
    public Optional<String> getString(String title) {
        return Optional.ofNullable(this.path.optString(Integer.parseInt(title)));
    }

    @Override
    public Optional<Integer> getInteger(String title) {
        return Optional.ofNullable(this.path.optInt(Integer.parseInt(title)));
    }

    @Override
    public Optional<Long> getLong(String title) {
        return Optional.ofNullable(this.path.optLong(Integer.parseInt(title)));
    }

    @Override
    public Optional<Double> getDouble(String title) {
        return Optional.ofNullable(this.path.optDouble(Integer.parseInt(title)));
    }

    @Override
    public Optional<Float> getFloat(String title) {
        return Optional.ofNullable(this.path.optFloat(Integer.parseInt(title)));

    }

    @Override
    public <T> Optional<T> getValue(String title, Parser<T> parser) {
        return parser.deserialize(this, title);
    }

    @Override
    public <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into) {
        return null;
    }

    @Override
    public void setValue(String title, Object value) {
        this.path.put(Integer.parseInt(title), value);
    }

    @Override
    public <T> void setValue(String title, Parser<T> parser, T value) {
        parser.serialize(this, title, value);
    }

    @Override
    public void setCollection(String title, Collection<?> collection) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(Integer.parseInt(title));
        }catch (JSONException e){
            array = new JSONArray();
            this.path.put(Integer.parseInt(title), array);
            array = this.path.getJSONArray(Integer.parseInt(title));
        }
        int index = 0;
        for(Object value : collection){
            array.put(index, value);
        }
    }

    @Override
    public <T> void setCollection(String title, Parser<T> parser, Collection<T> collection) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(Integer.parseInt(title));
        }catch (JSONException e){
            array = new JSONArray();
            this.path.put(Integer.parseInt(title), array);

        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        int index = 0;
        for(T value : collection){
            parser.serialize(node, index + "", value);
        }
    }
}
