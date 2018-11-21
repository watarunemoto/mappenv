package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.NoUploadedPhotoTransfer;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;


public class NoUploadActivity extends Activity {

	private SimpleCursorAdapter adapter;
	GridView myListView;
	TempOpenHelper imgOpenHelper;


	static final int CONTEXT_MENU1_ID = 0;
	static final int CONTEXT_MENU2_ID = 1;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_no_upload);

		myListView = (GridView) findViewById(R.id.list_gird_no);
		registerForContextMenu(myListView);
		TextView empy = (TextView) findViewById(R.id.emptyView);
		empy.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		myListView.setEmptyView(empy);
		SimpleCursorAdapter adapter = setAdapter();
		myListView.setAdapter(adapter);

	}


	/**
	 * 未アップロードリストへ移動
	 */

	/**
	 * 画像アップロード
	 */
	public void upPhoto(final String pos) {
		new AlertDialog.Builder(NoUploadActivity.this)
				.setTitle(R.string.detect_img)
				.setMessage(R.string.do_you_upload)
				.setNegativeButton(R.string.no_dialog, null)
				.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ConnectivityManager cm =
								(ConnectivityManager) NoUploadActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

						NetworkInfo info = cm.getActiveNetworkInfo();
                        if (info != null) {
                            NoUploadedPhotoTransfer nupt = new NoUploadedPhotoTransfer(NoUploadActivity.this, myListView, imgOpenHelper);
                            //NoUploadedPhotoTransfer nupt = new NoUploadedPhotoTransfer(NoUploadActivity.this);
                            nupt.execute(UrlCollections.URL_UPLOAD_PHOTO, pos);
                        } else{
                            String hoge = getString(R.string.camera_err_network);
                            ;
                            Toast.makeText(NoUploadActivity.this, hoge, Toast.LENGTH_SHORT).show();
                        }

					}
				}).show();
	}

	/**
	 * 画像削除
	 */
	public void removePhoto(final String pos) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.do_you_delete)
				.setMessage(R.string.do_delete)
				.setNegativeButton(R.string.no_dialog, null)
				.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						TempOpenHelper iph = new TempOpenHelper(NoUploadActivity.this);
						SQLiteDatabase db = iph.getWritableDatabase();

						ContentValues values = new ContentValues();
						values.put(TempContract.TempImages.COL_ISDELETED, "1");
						db.update(
								TempContract.TempImages.TABLE_NAME,
								values,
								TempContract.TempImages._ID + " = " + pos,
								null
						);
						SimpleCursorAdapter adapter =  setAdapter();
						myListView.setAdapter(adapter);
						Toast.makeText(NoUploadActivity.this, R.string.deleted + pos, Toast.LENGTH_SHORT).show();
						iph.close();
						db.close();
					}
				}).show();
	}


	/**
	 * ホップあっぷメニュー
	 * 削除と送信のふたつの機能をもつ
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(R.string.file_edit);
		menu.add(0, CONTEXT_MENU1_ID, 0, R.string.do_you_delete);
		menu.add(0, CONTEXT_MENU2_ID, 0, R.string.do_you_upload);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//listviewのアイテムのiDを取得
		AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		SimpleCursorAdapter adapter = setAdapter();
		int ste = (int) adapter.getItemId(acmi.position);
		String pos = String.valueOf(ste);
		Log.v("conpos", pos);
		switch (item.getItemId()) {
			case CONTEXT_MENU1_ID:
				removePhoto(pos);
				break;
			case CONTEXT_MENU2_ID:
				upPhoto(pos);
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * バックボタンを押された時に実行される処理
	 */
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	public SimpleCursorAdapter setAdapter() {
		TempOpenHelper imgOpenHelper = new TempOpenHelper(NoUploadActivity.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				TempContract.TempImages.TABLE_NAME,
				null,
				TempContract.TempImages.COL_ISUPLOADED + " = 0 AND " + TempContract.TempImages.COL_ISDELETED + " = 0",
				null,
				null,
				null,
				TempContract.TempImages.COL_UPDATED + " DESC"
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
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				R.layout.no_upload_item,
				c,
				from,
				to,
				0
		);

		return adapter;
	}

}
