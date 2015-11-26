package ac.u5b.td123.biop;

import android.app.Activity;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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


	public HttpPhotoTransfer(Context c, Activity activity) {
		this.activity = activity;
		this.context = c;
	}

	//データベース関連
	SQLiteDatabase db;

	//データの配列
	String[] data;


	@Override
	protected String doInBackground(String... params) {

		//呼び出された時の引数
		String url = params[0];
		String filename = params[1];

		//インスタンスの作成
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost hpost = new HttpPost(url);
		MultipartEntityBuilder entity = MultipartEntityBuilder.create();


		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		//ファイルパスをファイル型として取得？
		File file = new File(filename);

		//entityにデータをセット
		entity.addBinaryBody("catb", file, ContentType.create("image/jpg"), filename);

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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		dbch(context, filename);
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
			data = str.split(",", 0);
			String resu;
			if (data[0].equals("0")) {
				resu = "葉っぱ以外の何か スコア " + data[0];
			} else if (Double.parseDouble(data[0]) < 1.0) {
				resu = "葉っぱかも... スコア " + "0" + data[0];
			} else {
				resu = "葉っぱです スコア " + data[0];
			}
			Log.v("resu",resu);
			Toast.makeText(context, "画像をアップロードしました。\n結果:" + resu, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "画像をアップロードできませんでした。", Toast.LENGTH_LONG).show();
		}
		dialog.dismiss();
	}

	public void dbch(Context c, String filename) {
		//データベースに帰ってきたデータをぶち込む
		if (str != null) {
			String score;
			ImgOpenHelper imgOpenHelper = new ImgOpenHelper(c);
			db = imgOpenHelper.getWritableDatabase();
			List<Address> list_address = null;

			data = str.split(",", 0);
			if (data[0] != null) {
				if (data[0].equals("0")) {
					score = data[0];
				} else if (Double.parseDouble(data[0]) < 1.0) {
					score = "0" + data[0];
				} else {
					score = data[0];
				}
				Double lat = Double.parseDouble(data[1]);
				Double lng = Double.parseDouble(data[2]);

				String updated =
						new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
								.format(new Date());
				String fname = filename;


				/*
				//座標から住所を算出
				String string = new String();
				Geocoder geocoder = new Geocoder(context, Locale.JAPAN);
				try {
					list_address = geocoder.getFromLocation(lon, lat, 5);
					if (!list_address.isEmpty()) {
						Address address = list_address.get(0);
						StringBuffer stringBuffer = new StringBuffer();

						String buf;
						for (int i = 0; (buf = address.getAddressLine(i)) != null; i++) {
							Log.v("geocode", "loop no." + i);
							stringBuffer.append("addres.getAddressLine(" + i + ")" + buf + "\n");
						}

						string = stringBuffer.toString();
					} else {
						Log.v("geocode", "Fail Geocoding");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Log.v("geocode", string);
				*/

				ContentValues values = new ContentValues();
				values.put(ImgContract.Images.COL_LAT, lat);
				values.put(ImgContract.Images.COL_LNG, lng);
				values.put(ImgContract.Images.COL_SCORE, score);
				values.put(ImgContract.Images.COL_UPDATED, updated);
				values.put(ImgContract.Images.COLUMN_FILE_NAME, fname);

				db.insert(
						ImgContract.Images.TABLE_NAME,
						null,
						values
				);
			}
		}
	}
}
