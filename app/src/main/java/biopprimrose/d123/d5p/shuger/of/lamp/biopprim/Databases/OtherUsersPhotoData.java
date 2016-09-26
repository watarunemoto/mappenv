package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.provider.BaseColumns;

/**
 * Created by amimeyaY on 2016/01/03.
 */
public class OtherUsersPhotoData {



	public OtherUsersPhotoData() {}

	public static abstract class OtherImages implements BaseColumns {
		//テーブル名
		public static final String TABLE_NAME = "other_image";
		//カラム名　画像バイナリデータ名
		public static final String COLUMN_FILE_NAME = "fname";
		//アップロード
		public static final String COL_UPDATED = "updated";
		//スコア
		public static final String COL_SCORE = "score";
		//緯度経度
		public static final String COL_LAT = "lat";
		public static final String COL_LNG = "lng";
		//個別につける名前
		public static final String COL_PNAME = "pname";
		//アップロード者名
		public static final String COL_USER_NAME = "username";
		//見分ける目印
		public static final String COL_DB_NAME = "dbname";
		//他人からの得点
		public static final String COL_POINT = "point_from_others";
		//訂正された場合の判定？
		public static final String COL_DETECT = "re_evaluation";
		//評価したかの
		public static final String COL_EVAED = "col_evaed";
	}

}
