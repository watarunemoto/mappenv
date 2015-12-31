package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.ArrayList;


public class NoUploadActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	public final static String EXTRA_MYID = "ac.u5b.td123.biop.NoUploadActivity.MYID";
	ListView myListView;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_no_upload);

		//Loaderの初期化
		//getLoaderManager().initLoader(0, null, this);
		TempOpenHelper imgOpenHelper = new TempOpenHelper(NoUploadActivity.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				TempContract.TempImages.TABLE_NAME,
				null,
				TempContract.TempImages.COL_ISUPLOADED + " = 0",
				null,
				null,
				null,
				null
		);

		String[] from = {
				TempContract.TempImages.COLUMN_FILE_NAME,
				TempContract.TempImages.COL_UPDATED
		};

		final int[] to = {
				R.id.icon,
				android.R.id.text1,
		};

		//adapterのインスタンス化
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.no_upload_item,
				c,
				from,
				to,
				0
		);

		myListView = (ListView) findViewById(R.id.list);
		myListView.setEmptyView(findViewById(R.id.emptyView));
		myListView.setAdapter(adapter);

		myListView.setItemsCanFocus(false);
		myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(
					AdapterView<?> adapterView,
					View view,
					int position,
					long id
			) {
				Log.v("position", id + "");
				ListView listView = (ListView)adapterView;
				listView.setItemChecked(position, true);
				final String pos = String.valueOf(id);



				new AlertDialog.Builder(NoUploadActivity.this)
						.setTitle("画像を判定を行います")
						.setMessage("判定のため通信を行いますか？")
						.setNegativeButton("いいえ", null)
						.setPositiveButton("はい", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								NoUploadedPhotoTransfer nupt = new NoUploadedPhotoTransfer(NoUploadActivity.this);
								//NoUploadedPhotoTransfer nupt = new NoUploadedPhotoTransfer(NoUploadActivity.this);
								nupt.execute("http://133.14.168.53/DojakJamrujyonWeg4.php", pos);

							}
						}).show();


				((ListView) adapterView).deferNotifyDataSetChanged();
			}
		});


		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	/**
	 * アップロードするやつ
	 */


	/**
	 * 未アップロードリストへ移動
	 */

	public void uploaded(View view) {
		finish();
	}

	public void sentaku(View view) {
	}

	/**
	 * バックボタンを押された時に実行される処理
	 */
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		//Loaderを作った時に実行されるクエリを作成

		String[] projection = {
				TempContract.TempImages._ID,
				TempContract.TempImages.COL_SCORE,
				TempContract.TempImages.COL_UPDATED,
				TempContract.TempImages.COLUMN_FILE_NAME
		};

		return new CursorLoader(
				this,
				TempImgContentProvider.CONTENT_URI,
				projection,
				null,
				null,
				TempContract.TempImages.COL_UPDATED //昇順
		);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		//COntentproviderから値が帰ってきたときに実行
		adapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {
		//Loaderがリセットされた時に実行
		adapter.swapCursor(null);
	}
}
