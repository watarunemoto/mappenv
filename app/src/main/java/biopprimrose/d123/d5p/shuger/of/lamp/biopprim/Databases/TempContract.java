package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.provider.BaseColumns;

/**
 * Created by amimeyaY on 2015/12/02.
 */
public class TempContract {

	public TempContract() {}

	public static abstract class TempImages implements BaseColumns {
		//テーブル名
		public static final String TABLE_NAME = "no_image";
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
		//アップロードしたか否か
		public static final String COL_ISUPLOADED = "isUploaded";
		//削除したか
		public static final String COL_ISDELETED = "isdeleted";
		public static final String COL_ANNOTATION = "annotations";
	}

}
