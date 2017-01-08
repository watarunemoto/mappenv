package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FriendOpenHelper extends SQLiteOpenHelper {

	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "Biop_Friend";
	public final static int DB_VERSION = 6;
	//データベースの作成
	public static final String CREATE_TABLE =
			"create table " +
					FriendContract.Friends.TABLE_NAME + " (" +
					FriendContract.Friends.COL_ID + " integer primary key autoincrement, " +
					FriendContract.Friends.COL_OTHER_ID + " text, " +
					FriendContract.Friends.COL_OTHER_NAME + " text, " +
					FriendContract.Friends.COL_IS_ACCEPTED+ " text, " +
					FriendContract.Friends.COL_JOB_ID + " text, " +
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";

	//テーブルを削除する
	public static final String DROP_TABLE =
			"drop table if exists " + FriendContract.Friends.TABLE_NAME;


	public FriendOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create_friend_table","ok");
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v("create_friend_table","ok");
		db.execSQL(DROP_TABLE);
		db.execSQL(CREATE_TABLE);
		/**
		if (oldVersion == 1) {
			Cursor c = db.query(ImgContract.Images.TABLE_NAME, null, null, null, null, null, null);
			while (c.moveToNext()) {
				String score = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));
				String id = c.getString(c.getColumnIndex(ImgContract.Images._ID));
				String score_replace =  score.replaceAll(",", "@");
				if (!score_replace.equals("")) {
					db.execSQL("update " + ImgContract.Images.TABLE_NAME +
							" set " + ImgContract.Images.COL_SCORE + " = '" + score_replace + "'" + " where " + ImgContract.Images._ID + " = " + id);
				}
			}
		}


		 */

	}
}
