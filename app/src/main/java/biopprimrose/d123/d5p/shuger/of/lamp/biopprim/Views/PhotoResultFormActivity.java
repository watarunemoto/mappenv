package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class PhotoResultFormActivity extends ActionBarActivity {
	//リストビューのを押した時の表示画面を請け負うActivity

	private long imgId;
	private long posr;
	ImageView imageView;
	TextView text_score;
	TextView textView;
	Button oributton;
	public final static String MAP_ID = "biopprimrose.d123.d5p.shuger.of.lamp.MAP_ID";
	public final static String RINGO ="biopprimrose.d123.d5p.shuger.of.lamp.LIST";
	private String photoFile;

	final int degree = 90;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_result_form);

		Intent intent = getIntent();
		imgId = intent.getLongExtra(PhotoResultActivity.EXTRA_MYID, 0L);
		posr = intent.getIntExtra(PhotoResultActivity.EXTRA_MYPOS, 0);

		imageView = (ImageView) findViewById(R.id.image);
		text_score = (TextView) findViewById(R.id.score);
		text_score.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));

		textView = (TextView) findViewById(R.id.text_title_form);
		textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));

		Button button = (Button) findViewById(R.id.toMap);
		button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));

		ImgOpenHelper iph = new ImgOpenHelper(PhotoResultFormActivity.this);
		SQLiteDatabase db = iph.getReadableDatabase();

		Cursor c = db.query(
				ImgContract.Images.TABLE_NAME,
				null,
				ImgContract.Images._ID +" = "+imgId ,
				null,
				null,
				null,
				null
		);

		c.moveToFirst();
		WindowManager wm = getWindowManager();
		Display disp = wm.getDefaultDisplay();

		photoFile = c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));
		imageView.setImageBitmap(BitmapFactory.decodeFile(photoFile));
		imageView.setRotation(90);
		String pname = c.getString(c.getColumnIndex(ImgContract.Images.COL_PNAME));
        Log.v("id_form",imgId+"");
        if (pname.equals("")) {
			textView.setText("unknown");
		} else {
			textView.setText(pname);
		}

		ViewGroup.LayoutParams params = imageView.getLayoutParams();
		params.width = disp.getWidth();
		imageView.setLayoutParams(params);
		String score = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));

		String[] result_list = score.split("@");
		if (result_list.length != 7) {
			text_score.setText(R.string.error);
			return;
		}
		text_score.setText( result_list[1] );



		c.close();

	}

	public void toMaps(View view) {
		Intent intent = new Intent(PhotoResultFormActivity.this, MapsActivity.class);
		intent.putExtra(MAP_ID, imgId);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_photo_result_form, menu);
		return true;
	}

	private void deleteImg() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.do_you_delete)
				.setMessage(R.string.do_delete)
				.setNegativeButton(R.string.no_dialog, null)
				.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ImgOpenHelper iph = new ImgOpenHelper(PhotoResultFormActivity.this);
						SQLiteDatabase db = iph.getWritableDatabase();

						ContentValues values = new ContentValues();
						values.put(ImgContract.Images.COL_ISDELETED, "1");
						db.beginTransaction();
						try {
							db.update(
									ImgContract.Images.TABLE_NAME,
									values,
									ImgContract.Images._ID + "=" + String.valueOf(imgId),
									null
							);
							db.setTransactionSuccessful();
						} finally {
							db.endTransaction();
						}

						 String filename = PhotoResultFormActivity.this.getFilesDir().getPath() + "biopprimrose.d123.d5p.shuger.of.lamp/cmr/" + photoFile;
						 File file = new File(filename);
						 final boolean delete = file.delete();

						db.close();
						finish();
					}
				}).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_delete:
				deleteImg();
				break;
			case R.id.home:
				Intent intent = new Intent(PhotoResultFormActivity.this, PhotoResultActivity.class);
				intent.putExtra(RINGO, posr);
				finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * バックボタンを押された時に実行される処理
	 */
	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}
}
