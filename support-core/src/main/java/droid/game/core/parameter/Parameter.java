package droid.game.core.parameter;

import android.text.TextUtils;
import droid.game.common.parameter.IParameter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Parameter<TO extends Parameter> implements IParameter<TO> {
    private String mFunction;
    private String mWhat;
    private Map<String,Object> mContainer;
    private Map<String,String> mHttpMap;

    public Parameter() {
        super();
        mFunction = "";
        mWhat = "";
        mContainer = Collections.synchronizedMap(new HashMap<String, Object>());
        mHttpMap = Collections.synchronizedMap(new HashMap<String,String>());
    }

    @Override
    public TO setFunction(String function) {
        mFunction = function;
        return (TO) this;
    }

    @Override
    public String getFunction() {
        return mFunction;
    }

    @Override
    public TO setWhat(String what) {
        mWhat = what;
        return (TO) this;
    }

    @Override
    public String getWhat() {
        return mWhat;
    }

    @Override
    public <T> T get(String key, T defVal) {
        try{
            Object o = get(key);
            if (o != null){
                return (T)o;
            }
        }catch (Throwable e){

        }
        return defVal;
    }

    @Override
    public Object get(String key) {
        return this.mContainer.get(key);
    }

    @Override
    public TO put(String key, Object value) {
        if (value != null && !TextUtils.isEmpty(key)) {
            mContainer.put(key, value);

            String valStr = value.toString();
            if (!TextUtils.isEmpty(valStr)){
                mHttpMap.put(key,valStr);
            }
        }
        return (TO) this;
    }

    @Override
    public TO putAll(Map<String, Object> map) {
        if (map != null){
            for (String key : map.keySet()){
                put(key,map.get(key));
            }
        }
        return (TO) this;
    }

    @Override
    public TO putAll(IParameter parameter) {
        if (parameter != null){
            putAll(parameter.container());
        }
        return (TO) this;
    }

    @Override
    public Map<String, Object> container() {
        return mContainer;
    }

    @Override
    public Map<String, String> map() {
        return mHttpMap;
    }

    @Override
    public boolean contains(String key) {
        return mContainer.containsKey(key);
    }

    @Override
    public void remove(String key) {
        mContainer.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return mContainer.isEmpty();
    }

    /*
    @Override
    public <T extends IParameter> T to(Class<T> clazz) {
        try{
            if (this.getClass().getName().equals(clazz.getName())){
                return (T) this;
            }
            return (T) clazz.newInstance().putAll(this);
        }catch (Throwable e){

        }
        return null;
    }
    */

    @Override
    public String toString() {
        return super.toString() + "--->[" + mContainer.toString() + "]";
    }
}
