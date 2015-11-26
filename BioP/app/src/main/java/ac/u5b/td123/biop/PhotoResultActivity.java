package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class PhotoResultActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

	private SimpleCursorAdapter adapter;
	public final static String EXTRA_MYID = "ac.u5b.td123.biop.leafcatcher.MYID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_result);
		//Loaderの初期化

		getLoaderManager().initLoader(0, null, this);

		String [] from = {
				ImgContract.Images.COL_SCORE,
				ImgContract.Images.COLUMN_FILE_NAME,
				ImgContract.Images.COL_UPDATED
		};

		int [] to = {
				R.id.text_score,
				R.id.icon,
				R.id.etc
		};

		//adapterのインスタンス化
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_result,
				null,
				from,
				to,
				0
		);
		ListView myListView = (ListView) findViewById(R.id.myListView);
		myListView.setEmptyView(findViewById(R.id.emptyView));
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(
					AdapterView<?> adapterView,
					View view,
					int position,
					long id
			) {
				Intent intent = new Intent(PhotoResultActivity.this, PhotoResultFormActivity.class);
				intent.putExtra(EXTRA_MYID, id);
				startActivity(intent);
			}
		});

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

		String [] projection = {
				ImgContract.Images._ID,
				ImgContract.Images.COL_SCORE,
				ImgContract.Images.COL_UPDATED,
				ImgContract.Images.COLUMN_FILE_NAME
		};

		return new CursorLoader(
				this,
				ImgContentProvider.CONTENT_URI,
				projection,
				null,
				null,
				ImgContract.Images.COL_UPDATED + " DESC" //逆順
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
