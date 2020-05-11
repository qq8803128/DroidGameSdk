package droid.game.common.http;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpMap extends HashMap<String,String> {
    public static HttpMap create(){
        return new HttpMap();
    }

    public HttpMap add(String key, String value){
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)){
            put(key,value);
        }
        return this;
    }

    public HttpMap addAll(Map<String,String> map){
        if (map != null){
            for (String key : map.keySet()){
                String value = map.get(key);
                add(key,value);
            }
        }
        return this;
    }
}