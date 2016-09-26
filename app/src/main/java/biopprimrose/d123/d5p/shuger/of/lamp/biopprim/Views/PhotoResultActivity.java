package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class PhotoResultActivity extends Activity{

	private SimpleCursorAdapter adapter;
	public final static String EXTRA_MYID = "biopprimrose.d123.d5p.shuger.of.lamp.leafcatcher.MYID";
	public final static String EXTRA_MYPOS = "biopprimrose.d123.d5p.shuger.of.lamp.leafcatcher.MYPOS";
	private GridView myListView;
	int fffset;
	long posr=0;
	//idとlistviewの関連づけに使用
	ArrayList<String> ids = new ArrayList<>();

	static final int CONTEXT_MENU1_ID = 0;
	private static final int COL_SCORE_ID = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_result);

		Intent intent = getIntent();
		posr = intent.getLongExtra(PhotoResultFormActivity.RINGO, 0L);

		myListView = (GridView) findViewById(R.id.gridView);
		myListView.setEmptyView(findViewById(R.id.emptyView));
		registerForContextMenu(myListView);

		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(
					AdapterView<?> adapterView,
					View view,
					int position, //タッチした場所のポジション
					long id //タッチした場所のデータベースのid
			) {
				fffset = myListView.getChildAt(0).getTop();
				posr = myListView.getFirstVisiblePosition();

				ImgOpenHelper ioh = new ImgOpenHelper(PhotoResultActivity.this);
				SQLiteDatabase iohdb = ioh.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(ImgContract.Images.COL_VERSION, "");

				Log.v("id",id+","+position);

				iohdb.update(
						ImgContract.Images.TABLE_NAME,
						values,
						ImgContract.Images._ID + "=" + id,
						null
				);
				Intent intent = new Intent(PhotoResultActivity.this, PhotoResultFormActivity.class);
				intent.putExtra(EXTRA_MYID, id);
				intent.putExtra(EXTRA_MYPOS,posr);
				startActivity(intent);

				iohdb.close();
				ioh.close();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		adapter = adapter_reslash();

		myListView.setAdapter(adapter);
		myListView.setSelection((int)posr);
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("ファイル操作");
		menu.add(0, CONTEXT_MENU1_ID, 0, "画像削除");
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
				deleteImg(pos);
				break;
		}
		return super.onContextItemSelected(item);
	}

	public SimpleCursorAdapter setAdapter() {
		ImgOpenHelper imgOpenHelper = new ImgOpenHelper(PhotoResultActivity.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				ImgContract.Images.TABLE_NAME,
				null,
				ImgContract.Images.COL_ISDELETED + " = 0",
				null,
				null,
				null,
				ImgContract.Images.COL_UPDATED + " DESC"
		);

		String[] from = {
				ImgContract.Images.COLUMN_FILE_NAME,
				ImgContract.Images.COL_VERSION
		};

		final int[] to = {
				R.id.icon,
				R.id.new_or_old
		};

		//adapterのインスタンス化
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_result,
				c,
				from,
				to,
				0
		);

		return adapter;
	}


	private void deleteImg(final String imgId) {
		new AlertDialog.Builder(this)
				.setTitle("Delete Photo")
				.setMessage("Are you sure?")
				.setNegativeButton("No", null)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ImgOpenHelper iph = new ImgOpenHelper(PhotoResultActivity.this);
						SQLiteDatabase db = iph.getWritableDatabase();

						ContentValues values = new ContentValues();
						values.put(ImgContract.Images.COL_ISDELETED, "1");
						db.beginTransaction();
						try {
							db.update(
									ImgContract.Images.TABLE_NAME,
									values,
									ImgContract.Images._ID + "=" + imgId,
									null
							);
							db.setTransactionSuccessful();
						} finally {
							db.endTransaction();
						}

						ImgOpenHelper imgOpenHelper = new ImgOpenHelper(PhotoResultActivity.this);
						SQLiteDatabase dbi = imgOpenHelper.getReadableDatabase();
						Cursor c = dbi.query(
								ImgContract.Images.TABLE_NAME,
								null,
								ImgContract.Images._ID + " = " + imgId,
								null,
								null,
								null,
								ImgContract.Images.COL_UPDATED + " DESC"
						);

						c.moveToNext();
						String photoFile = c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));

						String filename = PhotoResultActivity.this.getFilesDir().getPath() + "biopprimrose.d123.d5p.shuger.of.lamp/cmr/" + photoFile;
						File file = new File(filename);
						final boolean delete = file.delete();

                        adapter = adapter_reslash();
                        myListView.setAdapter(adapter);

						db.close();
					}
				}).show();
	}

    private SimpleCursorAdapter adapter_reslash() {
        ImgOpenHelper imgOpenHelper = new ImgOpenHelper(PhotoResultActivity.this);
        SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
        Cursor c = db.query(
                ImgContract.Images.TABLE_NAME,
                null,
                ImgContract.Images.COL_ISDELETED + " = 0",
                null,
                null,
                null,
                ImgContract.Images.COL_UPDATED + " DESC"
        );


        while (c.moveToNext()) {
            String id  = c.getString(c.getColumnIndex(ImgContract.Images._ID));
            ids.add(id);

        }


        String [] from = {
                ImgContract.Images.COLUMN_FILE_NAME,
                ImgContract.Images.COL_VERSION,
                ImgContract.Images.COL_SCORE
        };

        int [] to = {
                R.id.icon,
                R.id.new_or_old,
                R.id.detected_result
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


        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i == COL_SCORE_ID) {
                    TextView detected = (TextView) view;
                    String detect_result = cursor.getString(COL_SCORE_ID);
					String[] detects = detect_result.split(",");
					String no1 =  detect_result.split(",")[0].split(":")[0].replace("'","").replace(" ", "").replace("{","").replace("}","");
					if (no1.equals("others")) {
                        detected.setTextColor(Color.BLACK);
						Log.v("dfi",no1 + "," + "others");
					} else {
                        detected.setTextColor(Color.GREEN);
                    }

                    detected.setText(no1);
                    return true;
                }

                return false;
            }
        });

        return adapter;

    }
}
