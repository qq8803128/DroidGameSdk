package droid.game.plugin.sdk.delegate.db;

import android.content.Context;
import droid.game.open.source.orm.LiteOrm;

public class LiteOrmImpl {
    public static LiteOrmImpl getInstance(){
        return Holder.holder;
    }

    private static final String DB_NAME = "%s.oldgame.db";
    private LiteOrm mLiteOrm;

    LiteOrmImpl() {
        super();
    }

    public void init(Context context){
        if (mLiteOrm == null){
            String dbName = context.getPackageName() + DB_NAME;
            mLiteOrm = LiteOrm.newSingleInstance(context,dbName);
            mLiteOrm.setDebugged(true);
        }
    }

    private static class Holder{
        private static final LiteOrmImpl holder = new LiteOrmImpl();
    }
}
