package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class RankingGeter extends AsyncTask<String, Integer, String> {

	ProgressDialog dialog;
	HttpResponse res = null;
	ListView lv;
	private RankCustomAdapter customAdapter;
	Activity activity;
	String str;

	public RankingGeter(Activity activity,ListView lv) {
		this.activity = activity;
		this.lv = lv;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(activity);
		dialog.setMessage("つうしんちゅう");
		dialog.setCancelable(true);
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {

		String url_1 = params[0];

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url_1);
		publishProgress(10);
		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		post_params.add(new BasicNameValuePair("`y3=vCYX=W)4!dN8xB", ":p<Mqf=xpsb{.5sBkUrA{Ew#vC]TM}7N~zmS"));
		try {
			post.setEntity(new UrlEncodedFormEntity(post_params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			res = httpClient.execute(post);
			publishProgress(40);
			try {
				str = EntityUtils.toString(res.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(70);
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
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (s != null) {
			String[] data = s.split(",");
			Log.v("str", s);
			ArrayList<String> alist = new ArrayList<String>();
			for (int i = 0; i < data.length - 1; i++) {
				alist.add(data[i]);
			}
			customAdapter = new RankCustomAdapter(activity, alist);
			lv.setAdapter(customAdapter);
			publishProgress(100);
			Log.v("test","rankok");
		} else {
			Toast.makeText(activity, "ランキングが取得できません", Toast.LENGTH_SHORT).show();
			Log.v("test","rankbad");
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
			Log.v("progress","dialogs");
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
}
