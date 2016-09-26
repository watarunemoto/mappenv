package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amimeyaY on 2016/01/06.
 */
public class OtherPhotoListGetter extends AsyncTask<String, Integer, String> {

	ProgressDialog dialog;
	HttpResponse res = null;
	Activity activity;
	String str;

	public OtherPhotoListGetter(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... params) {

		String url_1 = params[0];

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url_1);
		publishProgress(10);
		ContentType textContentType = ContentType.create("text/plain","UTF-8");

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		String username = sp.getString("MyID", "null");


		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		post_params.add(new BasicNameValuePair("`y3=vCYX=W)4!dN8xB",username ));
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
			Log.v("test", "runtime");
		}
		return str;
	}

	@Override
	protected void onPostExecute(String s) {
		/**
		String data[] = str.split("@");
		OtherPhotoGetter opg = new OtherPhotoGetter(activity);
		Log.v("dara",data[0]);
		String[] column = data[0].split(",");
		opg.execute("http://133.14.168.53/ifjvjngkjDKcm.php",column[0],column[1],column[2],column[3],column[4],column[5],column[6]);
		 */
	}
}
