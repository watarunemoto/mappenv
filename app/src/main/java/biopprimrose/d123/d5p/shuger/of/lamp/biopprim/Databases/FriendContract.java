package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.provider.BaseColumns;

/**
 * Created by amimeyaY on 2015/12/02.
 */
public class FriendContract {

	public FriendContract() {}

	public static abstract class Friends implements BaseColumns {
		//テーブル名
		public static final String COL_ID = "_id";
		public static final String TABLE_NAME = "friend_table";
		public static final String COL_OTHER_ID = "other_id";
		public static final String COL_OTHER_NAME = "other_name";
		public static final String COL_IS_ACCEPTED = "isaccepted";
		public static final String COL_JOB_ID = "job_id";
	}
}
