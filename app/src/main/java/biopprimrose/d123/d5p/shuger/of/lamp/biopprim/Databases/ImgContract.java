package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.provider.BaseColumns;

/**
 * Created by amimeyaY on 2015/11/05.
 */

public final class ImgContract {

	public ImgContract() {}

	public static abstract class Images implements BaseColumns {
		//テーブル名
		public static final String TABLE_NAME = "images";
		//カラム名　画像バイナリデータ名
		public static final String COLUMN_FILE_NAME = "fname";
		//アップロード
		public static final String COL_UPDATED = "updated";
		//スコア
		public static final String COL_SCORE = "score";
		//緯度経度
		public static final String COL_LAT = "lat";
		public static final String COL_LNG = "lng";
		//イベントID
		public static final String COL_EVENT_ID = "EventID";
		//自由に付ける名前
		public static final String COL_PNAME = "pname";
		//new表示するやつ
		public static final String COL_VERSION = "nroo";
		//デリートされたか
		public static final String COL_ISDELETED = "isdeleted";
		public static final String COL_ANNOTATION = "annotations";
	}

}
