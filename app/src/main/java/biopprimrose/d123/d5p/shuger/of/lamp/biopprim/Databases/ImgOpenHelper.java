package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ImgOpenHelper extends SQLiteOpenHelper {

	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "BioP-DB";
	public final static int DB_VERSION = 2;
	//データベースの作成
	public static final String CREATE_TABLE =
			"create table " +
					ImgContract.Images.TABLE_NAME + " (" +
					"_id integer primary key autoincrement, " +
					ImgContract.Images.COL_LAT + " text, " +
					ImgContract.Images.COL_LNG + " text, " +
					ImgContract.Images.COLUMN_FILE_NAME + " text, " +
					ImgContract.Images.COL_SCORE + " text, " +
					ImgContract.Images.COL_PNAME + " text, " +
					ImgContract.Images.COL_ISDELETED + " text, " +
					ImgContract.Images.COL_VERSION  + " text, " +
					ImgContract.Images.COL_ANNOTATION + " text, " +
					ImgContract.Images.COL_EVENT_ID + " text, " +
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";


	//テーブルを削除する
	public static final String DROP_TABLE =
			"drop table if exists " + ImgContract.Images.TABLE_NAME;

	/**
	public static final String UPDATE_TABLE =
			"update " + ImgContract.Images.TABLE_NAME +
					" set " + ImgContract.Images.COL_SCORE + " = " + ;
*/
	public ImgOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


		if (oldVersion == 1) {
			Cursor c = db.query(ImgContract.Images.TABLE_NAME, null, null, null, null, null, null, null);
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

	}
}
