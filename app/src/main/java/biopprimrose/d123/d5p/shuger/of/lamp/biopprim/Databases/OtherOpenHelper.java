package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amimeyaY on 2016/01/03.
 */
public class OtherOpenHelper extends SQLiteOpenHelper{


	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "BioP-DB-Other";
	public final static int DB_VERSION = 9;
	//データベースの作成
	public static final String CREATE_TABLE =
			"create table " +
					OtherUsersPhotoData.OtherImages.TABLE_NAME + " (" +
					"_id integer primary key autoincrement, " +
					OtherUsersPhotoData.OtherImages.COL_LAT + " text, " +
					OtherUsersPhotoData.OtherImages.COL_LNG + " text, " +
					OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME + " text, " +
					OtherUsersPhotoData.OtherImages.COL_SCORE + " text, " +
					OtherUsersPhotoData.OtherImages.COL_PNAME + " text, " +
					OtherUsersPhotoData.OtherImages.COL_USER_NAME + " text, " +
					OtherUsersPhotoData.OtherImages.COL_DB_NAME + " text, " +
					OtherUsersPhotoData.OtherImages.COL_POINT + " text, " +
					OtherUsersPhotoData.OtherImages.COL_DETECT + " text, " +
					OtherUsersPhotoData.OtherImages.COL_EVAED + " text, " +
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";

	//テーブルを削除する
	public static final String DROP_TABLE =
		"drop table if exists " + OtherUsersPhotoData.OtherImages.TABLE_NAME;



	public OtherOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i2) {
	}
}
