package droid.game.common.parameter;

import java.io.Serializable;
import java.util.Map;

public interface IParameter<TO extends IParameter> extends Serializable {
   // <T extends IParameter> T to(Class<T> clazz);
   TO setFunction(String function);
    String getFunction();
    TO setWhat(String what);
    String getWhat();
    Map<String,Object> container();
    Map<String,String> map();
    <T> T get(String key,T defVal);
    Object get(String key);
    boolean contains(String key);
    boolean isEmpty();
    void remove(String key);
    TO put(String key, Object value);
    TO putAll(Map<String,Object> map);
    TO putAll(IParameter parameter);
}
