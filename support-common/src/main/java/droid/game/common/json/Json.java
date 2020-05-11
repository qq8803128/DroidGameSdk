package droid.game.common.json;


import java.util.List;
import java.util.Map;

public class Json {
    Map<String,Object> container;

    public final Map<String, Object> getContainer() {
        return container;
    }

    /*
    public final <T> T get(T defVal,String... keys){
        try{
            Map<String,Object> map = container;
            Object retn = null;
            for (String key : keys){
                Object object = map.get(key);
                if (object instanceof Map){
                    map = (Map<String, Object>) object;
                }
                retn = object;
            }
            if (retn != null){
                if (retn instanceof Number){
                    Object o = null;
                    if (defVal instanceof Float){
                        o = ((Number)retn).floatValue();
                    }else if (defVal instanceof Integer){
                        o = ((Number)retn).intValue();
                    }else if (defVal instanceof Double){
                        o = ((Number)retn).doubleValue();
                    }else if (defVal instanceof Long){
                        o = ((Number)retn).longValue();
                    }else if (defVal instanceof Byte){
                        o = ((Number)retn).byteValue();
                    }else if (defVal instanceof Short){
                        o = ((Number)retn).shortValue();
                    }
                    return (T)o;
                }else {
                    if (defVal != null) {
                        if (retn.getClass().getName().equals(defVal.getClass().getName())) {
                            return (T) retn;
                        }else{
                            return defVal;
                        }
                    }else {
                        return (T) retn;
                    }
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return defVal;
    }
*/

    public final <T> T get(T defVal,Object... keys){
        try{
            Object o = container;
            Object result = null;
            for (Object k : keys){
                Object object = null;
                if (k instanceof Integer){
                    int key = ((Integer) k).intValue();
                    List<Object> list = (List<Object>) o;
                    object = list.get(key);
                    o = object;
                }else if (k instanceof String){
                    String key = k.toString();
                    Map<String,Object> map = (Map<String, Object>) o;
                    object = map.get(key);
                    if (object instanceof Map){
                        o = object;
                    }
                }
                result = object;
            }
            if (result != null){
                if (result instanceof Number){
                    Object or = null;
                    if (defVal instanceof Float){
                        or = ((Number)result).floatValue();
                    }else if (defVal instanceof Integer){
                        or = ((Number)result).intValue();
                    }else if (defVal instanceof Double){
                        or = ((Number)result).doubleValue();
                    }else if (defVal instanceof Long){
                        or = ((Number)result).longValue();
                    }else if (defVal instanceof Byte){
                        or = ((Number)result).byteValue();
                    }else if (defVal instanceof Short){
                        or = ((Number)result).shortValue();
                    }
                    return (T)or;
                }else {
                    if (defVal != null) {
                        if (result.getClass().getName().equals(defVal.getClass().getName())) {
                            return (T) result;
                        }else{
                            return defVal;
                        }
                    }else {
                        return (T) result;
                    }
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return defVal;
    }
}
