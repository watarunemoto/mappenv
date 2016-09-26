package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by amemiyaY on 2016/04/23.
 */
public class Hash_id_transfar extends AsyncTask<String , String ,String>{

	HttpResponse res;
	String str;
	SharedPreferences sp;
	public Hash_id_transfar(SharedPreferences sp){
		this.sp = sp;
	}


	@Override
	protected String doInBackground(String... strings) {
		String url_1 = strings[0];
		String user_name = strings[1];

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url_1);
		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		post_params.add(new BasicNameValuePair("dCei=CJEI::IEFUdjf",user_name));

		try {
			post.setEntity(new UrlEncodedFormEntity(post_params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			res = httpClient.execute(post);
			try {
				str = EntityUtils.toString(res.getEntity());
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
			Log.v("test", "runtime");
		}
		return str;
	}

	@Override
	protected void onPostExecute(String s) {
		sp.edit().putString("user_hash",str).apply();
		Log.v("hash",str);
	}
}
