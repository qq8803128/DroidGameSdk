package droid.game.open.source.orm.db.assit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite辅助类
 * 
 * @author mty
 * @date 2013-6-2下午4:42:47
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public static interface OnUpgradeListener {
		void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	public static interface OnDowngradeListener {
		void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	private OnUpgradeListener onUpgradeListener;
	private OnDowngradeListener onDowngradeListener;

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version,
						OnUpgradeListener onUpgradeListener,OnDowngradeListener onDowngradeListener) {
		super(context, name, factory, version);
		this.onUpgradeListener = onUpgradeListener;
		this.onDowngradeListener = onDowngradeListener;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//super.onDowngrade(db, oldVersion, newVersion);
		if (onDowngradeListener != null){
			onDowngradeListener.onDowngrade(db,oldVersion,newVersion);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (onUpgradeListener != null) {
			onUpgradeListener.onUpgrade(db, oldVersion, newVersion);
		}
	}

}