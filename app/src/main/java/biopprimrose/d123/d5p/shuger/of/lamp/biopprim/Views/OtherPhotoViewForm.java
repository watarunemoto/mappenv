package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherUsersPhotoData;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PointTransfer;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class OtherPhotoViewForm extends AppCompatActivity {
	/**
	 * 他の人の写真を閲覧、評価する画面
	 */

	//様々な画像の情報
	String[] packdata;

	TextView pname;
	TextView classs;
	TextView scores;
	ImageView imageView;

	Button good_button;
	Button bad_button;

	SharedPreferences sp;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_photo_view_form);

		/**
		 *MapsActiviyからのExif関連のデータの受け取り
		 */
		Intent intent = getIntent();
		packdata = intent.getStringArrayExtra(MapsActivity.MAP_EVA);

		pname = (TextView) findViewById(R.id.text_pname);
		classs = (TextView) findViewById(R.id.classname);
		scores = (TextView) findViewById(R.id.score);
		imageView = (ImageView) findViewById(R.id.image);

		good_button = (Button) findViewById(R.id.btn_good);
		bad_button = (Button) findViewById(R.id.btn_bad);

		pname.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		classs.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		scores.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		good_button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		bad_button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));
		pname.setText(packdata[0]);
		//classs.setText("人工知能による判定" + packdata[1]);
		Log.v("df", Arrays.toString(packdata));
		scores.setText( packdata[2]);

		sp = PreferenceManager.getDefaultSharedPreferences(OtherPhotoViewForm.this);
		username = sp.getString("MyID", "null");



		OtherOpenHelper oph = new OtherOpenHelper(OtherPhotoViewForm.this);
		SQLiteDatabase db = oph.getReadableDatabase();


		Cursor c = db.query(
				OtherUsersPhotoData.OtherImages.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null
		);
		while (c.moveToNext()) {
			String picname = c.getString(c.getColumnIndex(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME));

			if (picname.equals(packdata[3])) {
				bad_button.setEnabled(false);
				good_button.setEnabled(false);
			}
		}
		c.close();
		db.close();

	}



	public void eva_good(View view) {
		bad_button.setEnabled(false);
		good_button.setEnabled(false);
		Toast.makeText(OtherPhotoViewForm.this, "GOODしました！", Toast.LENGTH_SHORT).show();
		dbInsert("1");
	}

	public void eva_bad(View view) {
		good_button.setEnabled(false);
		bad_button.setEnabled(false);
		Toast.makeText(OtherPhotoViewForm.this, "BADしました！", Toast.LENGTH_SHORT).show();
		dbInsert("-1");
	}

	public void dbInsert(String selected) {
		OtherOpenHelper oph = new OtherOpenHelper(OtherPhotoViewForm.this);
		SQLiteDatabase db = oph.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(OtherUsersPhotoData.OtherImages.COL_EVAED, selected);
		cv.put(OtherUsersPhotoData.OtherImages.COL_PNAME,packdata[0]);
		cv.put(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME,packdata[3]);
		db.insert(
				OtherUsersPhotoData.OtherImages.TABLE_NAME,
				null,
				cv
		);
		PointTransfer pt = new PointTransfer(packdata[3],selected,username,packdata[4]);
		pt.execute("http://133.14.168.203/eifasdfceinn.php");
	}
}
