package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class HttpPhotoTransfer extends AsyncTask<String, Integer, String> {

	public Context context;
	private String str;
	private Activity activity;
	ProgressDialog dialog;
	private String pname;
	private String filename;

	String lati;
	String longi;


	public HttpPhotoTransfer(Context c, Activity activity, String lati, String longi,String pname) {
		this.activity = activity;
		this.context = c;
		this.lati = lati;
		this.longi = longi;
		this.pname = pname;
	}

	//データベース関連
	SQLiteDatabase db;

	//データの配列
	String[] data;


	@Override
	protected String doInBackground(String... params) {

		//呼び出された時の引数
		String url = params[0];
		filename = params[1];
		String userid = params[2];

		//インスタンスの作成
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost hpost = new HttpPost(url);
		MultipartEntityBuilder entity = MultipartEntityBuilder.create();


		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		//ファイルパスをファイル型として取得？
		File file = new File(filename);

		//entityにデータをセット
		ContentType textContentType = ContentType.create("text/plain","UTF-8");

		entity.addBinaryBody("catb", file, ContentType.create("image/jpg"), filename);
		entity.addTextBody("usrIDaafdfwe", userid, textContentType);
		entity.addTextBody("ppersonalname", pname,textContentType);
		/**entityにセットする
		 * この操作にはapache-mime5j-core,httpcore,httpmimeの３つのラブラリが必要
		 */
		hpost.setEntity(entity.build());

		str = null;





		//レスポンスを取得
		HttpResponse response = null;
		try {
			response = httpClient.execute(hpost);
			try {
				str = EntityUtils.toString(response.getEntity(), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}

			dbch(context, filename);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return str;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(activity);
		dialog.setMessage("つうしんちゅう");
		dialog.show();
	}

	@Override
	protected void onCancelled() {
		dialog.dismiss();
	}

	public void onCancel(DialogInterface dialog) {
		cancel(true);
	}

	@Override
	protected void onPostExecute(String result) {
		// サーバ側phpでechoした内容を表示
		if (str != null) {
			Log.v("resu", str);
			String resu;
			if (str.equals("0")) {
				resu = "葉っぱ以外の何か スコア " + str;
			} else if (Double.parseDouble(str) < 1.0) {
				resu = "葉っぱかも... スコア " + "0" + str;
			} else {
				resu = "葉っぱです スコア " + str;
			}
			Log.v("resu", resu);
			Toast.makeText(context, "画像をアップロードしました。\n結果:" + resu, Toast.LENGTH_LONG).show();
		} else {
			new AlertDialog.Builder(activity)
					.setTitle("画像をアップロードできませんでした")
					.setMessage("保存しておきますか\n後でアップロードできます。")
					.setNegativeButton("いいえ", null)
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							nouploaddb(filename);
						}
					}).show();
			Toast.makeText(context, "画像を保存しました", Toast.LENGTH_LONG).show();
		}
		dialog.dismiss();
	}

	//アップロードできないやつを別のデータベースに書き込むやつ
	public void nouploaddb(String filename) {

		String score;
		TempOpenHelper tempOpenHelper = new TempOpenHelper(context);
		db = tempOpenHelper.getWritableDatabase();

		Double lat = Double.parseDouble(lati);
		Double lng = Double.parseDouble(longi);

		String updated =
				new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
						.format(new Date());
		String fname = filename;

		score = "不明";
		ContentValues values = new ContentValues();
		values.put(TempContract.TempImages.COL_LAT, lat);
		values.put(TempContract.TempImages.COL_LNG, lng);
		values.put(TempContract.TempImages.COL_SCORE, score);
		values.put(TempContract.TempImages.COL_UPDATED, updated);
		values.put(TempContract.TempImages.COLUMN_FILE_NAME, fname);
		values.put(TempContract.TempImages.COL_PNAME,pname);
		values.put(TempContract.TempImages.COL_ISUPLOADED,"0");

		db.insert(
				TempContract.TempImages.TABLE_NAME,
				null,
				values
		);
	}

	public void dbch(Context c, String filename) {
		//データベースに帰ってきたデータをぶち込む
		String score;
		if (str != null) {
			ImgOpenHelper imgOpenHelper = new ImgOpenHelper(c);
			db = imgOpenHelper.getWritableDatabase();

			//List<Address> list_address = null;
			if (str.equals("0")) {
				score = str;
			} else if (Double.parseDouble(str) < 1.0) {
				score = "0" + str;
			} else {
				score = str;
			}

			Double lat = Double.parseDouble(lati);
			Double lng = Double.parseDouble(longi);

			String updated =
					new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
							.format(new Date());
			String fname = filename;

			ContentValues values = new ContentValues();
			values.put(ImgContract.Images.COL_LAT, lat);
			values.put(ImgContract.Images.COL_LNG, lng);
			values.put(ImgContract.Images.COL_SCORE, score);
			values.put(ImgContract.Images.COL_UPDATED, updated);
			values.put(ImgContract.Images.COLUMN_FILE_NAME, fname);
			values.put(ImgContract.Images.COL_PNAME,pname);
			values.put(ImgContract.Images.COL_VERSION,"new");

			db.insert(
					ImgContract.Images.TABLE_NAME,
					null,
					values
			);
		}
	}
}
