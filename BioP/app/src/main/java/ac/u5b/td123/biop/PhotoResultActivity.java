package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class PhotoResultActivity extends Activity{

	private SimpleCursorAdapter adapter;
	public final static String EXTRA_MYID = "ac.u5b.td123.biop.leafcatcher.MYID";
	private ListView myListView;
	int fffset;
	long posr=0;
	//idとlistviewの関連づけに使用
	ArrayList<String> ids = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_result);

		Intent intent = getIntent();
		posr = intent.getLongExtra(PhotoResultFormActivity.RINGO, 0L);
		//Loaderの初期化
		ImgOpenHelper imgOpenHelper = new ImgOpenHelper(PhotoResultActivity.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				ImgContract.Images.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null
		);

		while (c.moveToNext()) {
			String id  = c.getString(c.getColumnIndex(ImgContract.Images._ID));
			ids.add(id);
		}
		String [] from = {
				ImgContract.Images.COL_PNAME,
				ImgContract.Images.COLUMN_FILE_NAME,
				ImgContract.Images.COL_UPDATED,
				ImgContract.Images.COL_VERSION
		};

		int [] to = {
				R.id.text_pname,
				R.id.icon,
				R.id.etc,
				R.id.new_or_old
		};

		//adapterのインスタンス化
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_result,
				c,
				from,
				to,
				0
		);
		/**
		c.close();
		db.close();
		*/

		myListView = (ListView) findViewById(R.id.myListView);
		myListView.setEmptyView(findViewById(R.id.emptyView));
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(
					AdapterView<?> adapterView,
					View view,
					int position, //タッチした場所のポジション
					long id //タッチした場所のデータベースのid
			) {
				fffset = myListView.getChildAt(0).getTop();
				ImgOpenHelper ioh = new ImgOpenHelper(PhotoResultActivity.this);
				SQLiteDatabase iohdb = ioh.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(ImgContract.Images.COL_VERSION, "");

				iohdb.update(
						ImgContract.Images.TABLE_NAME,
						values,
						ImgContract.Images._ID + "=" + id,
						null
				);
				Intent intent = new Intent(PhotoResultActivity.this, PhotoResultFormActivity.class);
				intent.putExtra(EXTRA_MYID, id);
				startActivity(intent);

				iohdb.close();
				ioh.close();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		ImgOpenHelper imgOpenHelper = new ImgOpenHelper(PhotoResultActivity.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				ImgContract.Images.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null
		);

		while (c.moveToNext()) {
			String id  = c.getString(c.getColumnIndex(ImgContract.Images._ID));
			ids.add(id);
		}
		String [] from = {
				ImgContract.Images.COL_PNAME,
				ImgContract.Images.COLUMN_FILE_NAME,
				ImgContract.Images.COL_UPDATED,
				ImgContract.Images.COL_VERSION
		};

		int [] to = {
				R.id.text_pname,
				R.id.icon,
				R.id.etc,
				R.id.new_or_old
		};

		//adapterのインスタンス化
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_result,
				c,
				from,
				to,
				0
		);

		c.close();
		db.close();



		int posrs = (int) posr;
		myListView.setSelectionFromTop(posrs, fffset);

	}

	/**
	 * 未アップロードリストへ移動
	 */

	public void noupload(View view) {
		Intent intent = new Intent(PhotoResultActivity.this,NoUploadActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	/**
	 * バックボタンを押された時に実行される処理
	 */
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
