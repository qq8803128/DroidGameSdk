package droid.game.common.log.parser;


import java.util.Map;
import java.util.Set;

import droid.game.common.log.Parser;
import droid.game.common.log.utils.ObjectUtil;

/**
 * Created by pengwei on 16/3/8.
 */
class MapParse implements Parser<Map> {
    @Override
    public Class<Map> parseClassType() {
        return Map.class;
    }

    @Override
    public String parseString( Map map) {
        StringBuilder msg = new StringBuilder(map.getClass().getName() + " [" + LINE_SEPARATOR);
        Set keys = map.keySet();
        for (Object key : keys) {
            String itemString = "%s -> %s" + LINE_SEPARATOR;
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                } else if (value instanceof Character) {
                    value = "\'" + value + "\'";
                }
            }
            msg.append(String.format(itemString, ObjectUtil.objectToString(key),
                    ObjectUtil.objectToString(value)));
        }
        return msg + "]";
    }
}
