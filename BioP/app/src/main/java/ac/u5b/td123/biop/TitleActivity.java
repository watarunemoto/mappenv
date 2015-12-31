package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by amimeyaY on 2015/04/21.
 */

/**
 * ActivityとはAndroidアプリの画面
 */
public class TitleActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);

		TextView te = (TextView) findViewById(R.id.userid);
		setId(this,te);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Double sum_socre = 0.0;
		ImgOpenHelper imgOpenHelper = new ImgOpenHelper(this);
		SQLiteDatabase sqLiteDatabase = imgOpenHelper.getReadableDatabase();
		Cursor c = sqLiteDatabase.query(
				ImgContract.Images.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null
		);
		while (c.moveToNext()) {
			sum_socre = sum_socre + Double.parseDouble(c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE)));
			//Log.v("sum",""+ sum_socre);
		}
		TextView score = (TextView) findViewById(R.id.score);
		score.setText("Score:" + String.format("%f",sum_socre));
		c.close();
		sqLiteDatabase.close();
	}

	public void setId(Activity activity, TextView textView) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		textView.setText("ID:"+ sp.getString("MyID", "null"));
	}

	/**
	 * ボタンが押された時に実行される処理
	 */
	public void btn23(View view) {
		//他のアクティビティに遷移する
		Intent intent = new Intent(TitleActivity.this, CameraPreview.class);
		startActivity(intent);
	}

	public void btn24(View view) {
		Intent intent = new Intent(TitleActivity.this, PhotoResultActivity.class);
		startActivity(intent);
	}

	public void btn25(View view) {
		Intent intent = new Intent(TitleActivity.this, MapsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}


	public void btn27(View view) {
		Intent intent = new Intent(TitleActivity.this, RankActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			Intent intent = new Intent(TitleActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}