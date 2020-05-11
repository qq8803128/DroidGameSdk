package droid.game.common.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public final class JsonParser {
    public static final <T extends Json> T parser(String json, Class<T> clazz) throws Exception{
        JSONObject jsonObject = new JSONObject(json);

        Map<String, Object> container = parseJsonObject(jsonObject);
        T value = clazz.newInstance();
        if (value instanceof Json) {
            value.container = container;
        }

        //save singleton object
        JsonManager.manager().put(value);

        return value;
    }

    private static final Map<String, Object> parseJsonObject(JSONObject jsonObject) throws JSONException {
        Map<String, Object> jsonMap = new HashMap<>();
        Iterator<String> it = jsonObject.keys();
        while (it.hasNext()) {
            String key = it.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                jsonMap.put(key, parseJsonObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                jsonMap.put(key, parseJsonArray((JSONArray) value));
            } else {
                if (value instanceof Number){
                    ((Number)value).longValue();
                }
                jsonMap.put(key, value);
            }
        }
        return jsonMap;
    }

    private static final List<Object> parseJsonArray(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                list.add(parseJsonObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                list.add(parseJsonArray((JSONArray) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }
}
