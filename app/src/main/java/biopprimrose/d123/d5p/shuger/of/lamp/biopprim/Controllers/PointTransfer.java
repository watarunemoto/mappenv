package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

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
 * Created by amemiyaY on 2016/01/15.
 */
public class PointTransfer extends AsyncTask<String,Integer,String> {

	String filename,point,eva_user,evaed_user;
	HttpResponse res;
	String str;

	public  PointTransfer(String filename, String point, String eva_user, String evaed_user){
		this.filename = filename;
		this.point = point;
		this.eva_user = eva_user;
		this.evaed_user = evaed_user;
	}
	@Override
	protected String doInBackground(String... strings) {
		String url_s = strings[0];

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url_s);
		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		post_params.add(new BasicNameValuePair("fileName", filename));
		post_params.add(new BasicNameValuePair("poINT", point));
		post_params.add(new BasicNameValuePair("eva_User", eva_user));
		post_params.add(new BasicNameValuePair("evaed_UseR", evaed_user));

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
			Log.v("test","runtime");
		}
		return str;
	}
}
