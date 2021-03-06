package org.block.serialization.json;

import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class JSONArrayConfigNode implements ConfigNode {

    private final JSONArray path;

    public JSONArrayConfigNode(JSONArray path) {
        this.path = path;
    }

    @Override
    public ConfigNode getNode(String... path) {
        if (path.length == 0) {
            return this;
        }
        int entry = Integer.parseInt(path[0]);
        JSONConfigNode node;
        try {
            node = new JSONConfigNode(this.path.getJSONObject(entry));
        } catch (JSONException e) {
            this.path.put(entry, new JSONObject());
            node = new JSONConfigNode(this.path.getJSONObject(entry));
        }
        String[] newPath = new String[path.length - 1];
        for (int A = 0; A < path.length - 1; A++) {
            newPath[A] = path[A + 1];
        }
        return node.getNode(newPath);
    }

    @Override
    public Optional<String> getString(String title) {
        return Optional.ofNullable(this.path.optString(Integer.parseInt(title)));
    }

    @Override
    public OptionalInt getInteger(String title) {
        return getNumber(title, Number::intValue, OptionalInt::empty, OptionalInt::of);
    }

    @Override
    public OptionalLong getLong(String title) {
        return getNumber(title, Number::longValue, OptionalLong::empty, OptionalLong::of);
    }

    @Override
    public OptionalDouble getDouble(String title) {
        return getNumber(title, Number::doubleValue, OptionalDouble::empty, OptionalDouble::of);
    }

    @Override
    public Optional<Float> getFloat(String title) {
        return getNumber(title, Number::floatValue);
    }

    @Override
    public Optional<Boolean> getBoolean(String title) {
        Object obj = this.path.opt(Integer.parseInt(title));
        if (obj == null) {
            return Optional.empty();
        }
        if (!(obj instanceof Boolean)) {
            return Optional.empty();
        }
        return Optional.of((boolean) obj);
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
        } catch (JSONException e) {
            array = new JSONArray();
            this.path.put(Integer.parseInt(title), array);
            array = this.path.getJSONArray(Integer.parseInt(title));
        }
        int index = 0;
        for (Object value : collection) {
            array.put(index, value);
        }
    }

    @Override
    public <T> void setCollection(String title, Parser<T> parser, Collection<T> collection) {
        JSONArray array = null;
        try {
            array = this.path.getJSONArray(Integer.parseInt(title));
        } catch (JSONException e) {
            array = new JSONArray();
            this.path.put(Integer.parseInt(title), array);

        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        int index = 0;
        for (T value : collection) {
            parser.serialize(node, index + "", value);
            index++;
        }
    }

    public <T extends Number> Optional<T> getNumber(String title, Function<Number, T> function) {
        Number number = this.path.optNumber(Integer.parseInt(title));
        if (number == null) {
            return Optional.empty();
        }
        return Optional.of(function.apply(number));
    }

    public <T extends Number, O> O getNumber(String title, Function<Number, T> function, Supplier<O> empty, Function<T, O> map) {
        Number number = this.path.optNumber(Integer.parseInt(title));
        if (number == null) {
            return empty.get();
        }
        return map.apply(function.apply(number));
    }
}
