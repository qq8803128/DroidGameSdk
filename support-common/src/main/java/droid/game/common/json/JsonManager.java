package droid.game.common.json;

import java.util.HashMap;
import java.util.Map;

public final class JsonManager {
    public static final JsonManager manager() {
        return Holder.holder;
    }

    private Map<Class, Object> map = new HashMap<>();

    JsonManager() {
        super();
    }

    public final void put(Object object) {
        if (object != null) {
            map.put(object.getClass(), object);
        }
    }

    public final void put(Class clazz,Object object){
        if (clazz != null){
            map.put(clazz, object);
        }
    }

    public final <T extends Json> T get(Class<T> clazz) {
        try {
            T value = (T) map.get(clazz);
            return value;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    static class Holder {
        private static final JsonManager holder = new JsonManager();
    }
}
