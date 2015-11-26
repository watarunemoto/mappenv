package ac.u5b.td123.biop;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ImgOpenHelper extends SQLiteOpenHelper {

	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "BioP-DB2";
	public final static int DB_VERSION = 1;
	//データベースの作成
	public static final String CREATE_TABLE =
			"create table " +
					ImgContract.Images.TABLE_NAME + " (" +
					"_id integer primary key autoincrement, " +
					ImgContract.Images.COL_LAT + " text, " +
					ImgContract.Images.COL_LNG + " text, " +
					ImgContract.Images.COLUMN_FILE_NAME + " text, " +
					ImgContract.Images.COL_SCORE + " text, " +
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";

	//テーブルを削除する
	public static final String DROP_TABLE =
			"drop table if exists " + ImgContract.Images.TABLE_NAME;

	public ImgOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i2) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
