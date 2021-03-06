package org.block.serialization.json;

import org.block.serialization.ConfigNode;
import org.block.serialization.parse.Parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class JSONConfigNode implements ConfigNode {

    private final JSONObject path;

    public JSONConfigNode(JSONObject obj) {
        this.path = obj;
    }

    @Override
    public ConfigNode getNode(String... path) {
        JSONObject obj = this.path;
        for (String key : path) {
            try {
                obj = obj.getJSONObject(key);
            } catch (JSONException e) {
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
    public OptionalInt getInteger(String title) {
        return this.getNumber(title, Number::intValue, OptionalInt::empty, OptionalInt::of);
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
        Object obj = this.path.opt(title);
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
        try {
            return parser.deserialize(this, title);
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T, C extends Collection<T>> C getCollection(String title, Parser<T> parser, C into) {
        JSONArray array;
        try {
            array = this.path.getJSONArray(title);
        } catch (JSONException e) {
            return into;
        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        for (int A = 0; A < array.length(); A++) {
            try {
                parser.deserialize(node, A + "").ifPresent(into::add);
            } catch (IllegalStateException ignored) {
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
        JSONArray array;
        try {
            array = this.path.getJSONArray(title);
        } catch (JSONException e) {
            JSONArray jArray = new JSONArray();
            this.path.put(title, jArray);
            array = this.path.getJSONArray(title);
        }
        int index = 0;
        for (Object value : collection) {
            array.put(index, value);
            index++;
        }
    }

    @Override
    public <T> void setCollection(String title, Parser<T> parser, Collection<T> collection) {
        JSONArray array;
        try {
            array = this.path.getJSONArray(title);
        } catch (JSONException e) {
            array = new JSONArray();
            this.path.put(title, array);
        }
        JSONArrayConfigNode node = new JSONArrayConfigNode(array);
        int index = 0;
        for (T value : collection) {
            parser.serialize(node, index + "", value);
            index++;
        }
    }

    public <T extends Number, O> O getNumber(String title, Function<Number, T> function, Supplier<O> empty, Function<T, O> map) {
        Number number = this.path.optNumber(title);
        if (number == null) {
            return empty.get();
        }
        var parsed = function.apply(number);
        return map.apply(parsed);
    }

    public <T extends Number> Optional<T> getNumber(String title, Function<Number, T> function) {
        Number number = this.path.optNumber(title);
        if (number == null) {
            return Optional.empty();
        }
        return Optional.of(function.apply(number));
    }

    JSONObject getPath() {
        return this.path;
    }
}
