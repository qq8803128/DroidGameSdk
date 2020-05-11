package droid.game.common.log.parser;

import android.os.Bundle;

import droid.game.common.log.Parser;
import droid.game.common.log.utils.ObjectUtil;

/**
 * Created by pengwei on 16/3/8.
 */
class BundleParse implements Parser<Bundle> {

    @Override
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    @Override
    public String parseString( Bundle bundle) {
        StringBuilder builder = new StringBuilder(bundle.getClass().getName());
        builder.append(" [");
        builder.append(LINE_SEPARATOR);
        for (String key : bundle.keySet()) {
            builder.append(String.format("'%s' => %s " + LINE_SEPARATOR,
                    key, ObjectUtil.objectToString(bundle.get(key))));
        }
        builder.append("]");
        return builder.toString();
    }
}
