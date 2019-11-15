package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.Util;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class PhotoResultFormActivity extends AppCompatActivity {
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
//
//		ImgOpenHelper iph = new ImgOpenHelper(PhotoResultFormActivity.this);
//		SQLiteDatabase db = iph.getReadableDatabase();
//
//		Cursor c = db.query(
//				ImgContract.Images.TABLE_NAME,
//				null,
//				ImgContract.Images._ID +" = "+imgId ,
//				null,
//				null,
//				null,
//				null
//		);
//
//		c.moveToFirst();
//		WindowManager wm = getWindowManager();
//		Display disp = wm.getDefaultDisplay();
//
//		photoFile = c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));
//        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//        params.width = disp.getWidth();
//        params.height = disp.getHeight();
//        Bitmap bitmap = Util.decodeSampledBitmapFromResource(photoFile,params.width,params.height);
//
////        Bitmap bitmap = Util.decodeSampledBitmapFromResource(photoFile,50,50);
//        imageView.setImageBitmap(bitmap);
////        bitmap.recycle();
//
//
////		imageView.setImageBitmap(BitmapFactory.decodeFile(photoFile));
//		imageView.setRotation(90);
//		String pname = c.getString(c.getColumnIndex(ImgContract.Images.COL_PNAME));
//        Log.v("id_form",imgId+"");
//        if (pname.equals("")) {
//			textView.setText("unknown");
//		} else {
//			textView.setText(pname);
//		}
//
//		imageView.setLayoutParams(params);
//		String score = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));
//        score = score.replaceAll("\"","");
//        score = score.replaceAll("\n","");
//		score = score.replaceAll("[A-Z]]","[a-z]");
//        String cc = getResources().getString(R.string.upload_error_return);
////        String aa = String.valueOf(score.equals(cc));
//		if (!(score.equals("error")) && !(score.equals(cc))) {
////			score = score.replaceAll("\"","");
//			String no1 = score.split(",")[0];
//			String no2 = score.split(",")[1];
//			String no3 = score.split(",")[2];
//			text_score.setText("獲得した点数は" +no1 + "点です");
//		} else {
//            text_score.setText(R.string.upload_error);
////            text_score.setText(R.string.);
//
//		}
//
//
//
//
//		c.close();

	}


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        imageView.setImageBitmap(null);
//        imageView.destroyDrawingCache();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = disp.getWidth();
        params.height = disp.getHeight();
        Bitmap bitmap = Util.decodeSampledBitmapFromResource(photoFile,params.width,params.height);

//        Bitmap bitmap = Util.decodeSampledBitmapFromResource(photoFile,10,10);
        imageView.setImageBitmap(bitmap);
//        imageView.setImageBitmap(Util.decodeSampledBitmapFromResource(photoFile,10,10));


//		imageView.setImageBitmap(BitmapFactory.decodeFile(photoFile));
        imageView.setRotation(90);
        String pname = c.getString(c.getColumnIndex(ImgContract.Images.COL_PNAME));
        Log.v("id_form",imgId+"");
        if (pname.equals("")) {
            textView.setText("unknown");
        } else {
            textView.setText(pname);
        }

        imageView.setLayoutParams(params);
        String score = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));
        score = score.replaceAll("\"","");
        score = score.replaceAll("\n","");
        score = score.replaceAll("[A-Z]]","[a-z]");
        String cc = getResources().getString(R.string.upload_error_return);
//        String aa = String.valueOf(score.equals(cc));
        if (!(score.equals("error")) && !(score.equals(cc))) {
//			score = score.replaceAll("\"","");
            String no1 = score.split(",")[0];
//            String no2 = score.split(",")[1];
//            String no3 = score.split(",")[2];
            text_score.setText("獲得した点数は" +no1 + "点です");
        } else {
            text_score.setText(R.string.upload_error);
//            text_score.setText(R.string.);

        }




        c.close();



    }

    @Override
    protected void onPause() {
        super.onPause();
//        imageView.setImageBitmap(null);
//        imageView.destroyDrawingCache();
        Drawable drawable = imageView.getDrawable();
        if(drawable!= null){
            ((BitmapDrawable)drawable).getBitmap().recycle();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Drawable drawable = imageView.getDrawable();
        if(drawable!= null){
            ((BitmapDrawable)drawable).getBitmap().recycle();
        }
        imageView =null;
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
				.setTitle(R.string.label_deleteimages)
				.setMessage(R.string.label_deleteconfirmation)
				.setNegativeButton(R.string.no_dialog, null)
				.setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
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
        Drawable drawable = imageView.getDrawable();
        if(drawable!= null){
            ((BitmapDrawable)drawable).getBitmap().recycle();
        }
//        imageView = null;
	}
}
