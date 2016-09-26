package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by amimeyaY on 2015/12/02.
 */
public class TempOpenHelper extends SQLiteOpenHelper {

	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "BioP-DB-Temp";
	public final static int DB_VERSION = 9;
	//データベースの作成
	public static final String CREATE_TABLE =
			"create table " +
					TempContract.TempImages.TABLE_NAME + " (" +
					"_id integer primary key autoincrement, " +
					TempContract.TempImages.COL_LAT + " text, " +
					TempContract.TempImages.COL_LNG + " text, " +
					TempContract.TempImages.COLUMN_FILE_NAME + " text, " +
					TempContract.TempImages.COL_SCORE + " text, " +
					TempContract.TempImages.COL_PNAME + " text, " +
					TempContract.TempImages.COL_ISUPLOADED + " text, " +
					TempContract.TempImages.COL_ISDELETED + " text, " +
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";


	//テーブルを削除する
	public static final String DROP_TABLE =
			"drop table if exists " + TempContract.TempImages.TABLE_NAME;

	public TempOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		//db.execSQL(ALTER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i2) {
		Log.v("alter", "alter table");
	}
}
