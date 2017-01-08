package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RankCustomAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class RankingGeter extends AsyncTask<String, Integer, String> {

	ProgressDialog dialog;
	String  res = null;
	private RankCustomAdapter customAdapter;
	Activity activity;

	SharedPreferences sp;
	String str;

	public RankingGeter(Activity activity, SharedPreferences sp) {
		this.activity = activity;
		this.sp = sp;
	}

	@Override
	protected void onPreExecute() {
		/**
		dialog = new ProgressDialog(activity);
		String message = activity.getResources().getString(R.string.dialog_rangking_getter);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.show();
		 */
	}

	@Override
	protected String doInBackground(String... params) {

		String url = params[0];

		MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart(
						"`y3=vCYX=W)4!dN8xB", ":p<Mqf=xpsb{.5sBkUrA{Ew#vC]TM}7N~zmS"
				)
				.build();

		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();


		OkHttpClient client = new OkHttpClient().newBuilder().
				readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
				.writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
				.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
				.build();

		String res_st = null;
		try {
			Response res = client.newCall(request).execute();
			res_st = res.body().string();

			//Log.v("respose", res_st);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return res_st;
	}

	@Override
	protected void onPostExecute(String s) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (s != null) {
			String[] data = s.split(",");
			Log.v("str", s);
			List<String> alist = new ArrayList<String>();
			for (int i = 0; i < data.length - 1; i++) {
				alist.add(data[i]);
			}
			//一時的に保管
			sp.edit().putString("rank_list", s).apply();

			Date date = new Date();
			String nowtime;
			final DateFormat df = new SimpleDateFormat("MM:dd:HH");
			nowtime = "About " +  df.format(date);
			sp.edit().putString("ctime",nowtime).apply();
			Log.v("date",nowtime+"");
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		//dialog.setProgress(values[0]);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (dialog != null) {
			Log.v("progress", "dialogs");
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
}
