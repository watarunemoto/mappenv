package ac.u5b.td123.biop;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ImgOpenHelper extends SQLiteOpenHelper {

	//データベースの名前とバージョンの定義
	public final static String DB_NAME = "BioP-DB2";
	public final static int DB_VERSION = 6;
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
					"created datetime default current_timestamp, " +
					"updated datetime default current_timestamp)";

	public static final String ALTER_TABLE =
			"alter table " +
					ImgContract.Images.TABLE_NAME +
					" add " + ImgContract.Images.COL_VERSION + " text ";

	public static final String UPDATE_TABLE =
			"update " + ImgContract.Images.TABLE_NAME +
					" set" + ImgContract.Images.COL_VERSION + "";



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
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			//db.execSQL(DROP_TABLE);
			//古いデータの取得
			//db.execSQL(CREATE_TABLE);
			//db.execSQL(ALTER_TABLE);
			Log.v("alter", "alter table");
	}
}
