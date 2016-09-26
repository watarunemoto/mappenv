package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.OtherPhotoListGetter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherUsersPhotoData;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class EvalOtherPhotos extends AppCompatActivity {

	private SimpleCursorAdapter adapter;
	GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eval_other_photos);


		gridView = (GridView) findViewById(R.id.gridView);

		OtherOpenHelper imgOpenHelper = new OtherOpenHelper(EvalOtherPhotos.this);
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				OtherUsersPhotoData.OtherImages.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null
		);

		String [] from = {
				OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME
		};

		int [] to = {
				R.id.photokano
		};

		//adapterのインスタンス化
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.list_item_other,
				c,
				from,
				to,
				0
		);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});
	}

	public void getPhotos(View v) {
		OtherPhotoListGetter oplg = new OtherPhotoListGetter(EvalOtherPhotos.this);
		oplg.execute("http://133.14.168.203/yuYURc12.php");
	}
}
