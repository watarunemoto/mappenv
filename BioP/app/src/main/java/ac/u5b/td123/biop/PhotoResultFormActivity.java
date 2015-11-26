package ac.u5b.td123.biop;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class PhotoResultFormActivity extends ActionBarActivity {
		//リストビューのを押した時の表示画面を請け負うActivity

		private long imgId;
		ImageView imageView;
		TextView text_class;
		TextView text_score;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_photo_result_form);

			Intent intent = getIntent();
			imgId = intent.getLongExtra(PhotoResultActivity.EXTRA_MYID, 0L);

			imageView = (ImageView) findViewById(R.id.image);
			text_class = (TextView) findViewById(R.id.classname);
			text_score = (TextView) findViewById(R.id.score);

			Uri uri = ContentUris.withAppendedId(
					ImgContentProvider.CONTENT_URI,
					imgId
			);


			String [] projection = {
					ImgContract.Images.COL_UPDATED,
					ImgContract.Images.COLUMN_FILE_NAME,
					ImgContract.Images.COL_SCORE
			};

			Cursor c = getContentResolver().query(
					uri,
					projection,
					ImgContract.Images._ID + " = ?",
					new String[] { Long.toString(imgId)},
					null
			);

			c.moveToFirst();

			WindowManager wm = getWindowManager();
			Display disp = wm.getDefaultDisplay();
			imageView.setImageBitmap(BitmapFactory.decodeFile(
							c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME))
					)
			);

			ViewGroup.LayoutParams params = imageView.getLayoutParams();
			params.width = disp.getWidth();
			imageView.setLayoutParams(params);
			String score = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));

			if (score.equals("0")) {
				text_class.setText("葉っぱ以外の何か");
			} else if ( Double.parseDouble(score) < 1.00 ) {
				text_class.setText("葉っぱかも...");
			} else {
				text_class.setText("葉っぱです");
			}
			text_score.setText(
					"Score:" +
							c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE))
			);
			c.close();
		}


		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.menu_photo_result_form, menu);
			return true;
		}

		private void deleteImg() {
			new AlertDialog.Builder(this)
					.setTitle("Delete Photo")
					.setMessage("Are you sure?")
					.setNegativeButton("No",null)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Uri uri = ContentUris.withAppendedId(
									ImgContentProvider.CONTENT_URI,
									imgId
							);

							getContentResolver().delete(
									uri,
									ImgContract.Images._ID + " = ?",
									new String[] { Long.toString(imgId)}
							);
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
			}
			return super.onOptionsItemSelected(item);
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
