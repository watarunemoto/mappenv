package ac.u5b.td123.biop;

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
	}

}
