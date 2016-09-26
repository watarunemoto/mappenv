package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherUsersPhotoData;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amimeyaY on 2016/01/06.
 */
public class OtherPhotoGetter extends AsyncTask<String, Integer, String> {

	ProgressDialog dialog;
	HttpResponse res = null;
	Context context;
	//他人の画像の情報
	String lat;
	String lng;
	String filename;
	String score;
	String pname;
	String updated;
	String usename;

	String imgName;
	String str;

	ImageView imageView;
	Button goodbutton;
	Button badbutton;
	Boolean flag;

	byte[] photo_down;

	ProgressDialog progressDialog;


	public OtherPhotoGetter(Context context,ImageView imageView,Button goodbutton, Button badbutton, Boolean flag) {
		this.context = context;
		this.imageView = imageView;
		this.goodbutton = goodbutton;
		this.badbutton = badbutton;
		this.flag = flag;
	}


	@Override
	protected void onPreExecute() {
		String message =  context.getResources().getString(R.string.photo_loading);

		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {

		String url_1 = params[0];
		filename = params[1];


		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url_1);
		publishProgress(10);
		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		post_params.add(new BasicNameValuePair("dievifkgiD74", filename));
		try {
			post.setEntity(new UrlEncodedFormEntity(post_params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			res = httpClient.execute(post);
			try {
				photo_down = EntityUtils.toByteArray(res.getEntity());
				//is  = res.getEntity().getContent();
				FileOutputStream myFOS;

				File file = new File("/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/ops/");

				//フォルダが存在しなかった場合にフォルダを作成
				if (!file.exists()) {
					file.mkdir();
				}
				//オリジナルの名前の生成
				imgName = "/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/ops/" + filename;

				try {
					myFOS = new FileOutputStream(imgName);
					myFOS.write(photo_down);
					myFOS.close();
					dbch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			Log.v("test", "taroumaru");
		} catch (IOException e) {
			e.printStackTrace();
			Log.v("test", "ikachan");
		} catch (RuntimeException e) {
			e.printStackTrace();
			Log.v("test","runtime");
		}
		return str;
	}

	@Override
	protected void onPostExecute(String  s) {
		Bitmap bitmap = BitmapFactory.decodeFile(imgName);
		imageView.setImageBitmap(bitmap);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		if (flag) {
			goodbutton.setEnabled(true);
			badbutton.setEnabled(true);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public void dbch() {
		//データベースに帰ってきたデータをぶち込む
		OtherOpenHelper imgOpenHelper = new OtherOpenHelper(context);
		SQLiteDatabase db = imgOpenHelper.getWritableDatabase();

		Double lati = Double.parseDouble(lat);
		Double longi = Double.parseDouble(lng);

		ContentValues values = new ContentValues();
		values.put(OtherUsersPhotoData.OtherImages.COL_LAT, lati);
		values.put(OtherUsersPhotoData.OtherImages.COL_LNG, longi);
		values.put(OtherUsersPhotoData.OtherImages.COL_SCORE, score);
		values.put(OtherUsersPhotoData.OtherImages.COL_UPDATED, updated);
		values.put(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME, imgName);
		values.put(OtherUsersPhotoData.OtherImages.COL_PNAME, pname);
		values.put(OtherUsersPhotoData.OtherImages.COL_USER_NAME,usename);

		db.insert(
				OtherUsersPhotoData.OtherImages.TABLE_NAME,
				null,
				values
		);
	}
}
