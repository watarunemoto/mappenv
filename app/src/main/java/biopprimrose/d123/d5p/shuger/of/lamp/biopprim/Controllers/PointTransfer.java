package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amemiyaY on 2016/01/15.
 */
public class PointTransfer extends AsyncTask<String,Integer,String> {

	String filename,point,eva_user,evaed_user;
	String str;
	String res_st;

	public  PointTransfer(String filename, String point, String eva_user, String evaed_user){
		this.filename = filename;
		this.point = point;
		this.eva_user = eva_user;
		this.evaed_user = evaed_user;
	}
	@Override
	protected String doInBackground(String... strings) {
		String url_s = strings[0];

		MultipartEntityBuilder entity = MultipartEntityBuilder.create();

		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)

				.addFormDataPart(
						"filename", filename
				)
				.addFormDataPart(
						"point", point
				)
				.addFormDataPart(
						"my_id", eva_user
				)
				.addFormDataPart(
						"other_id", evaed_user
				)
				.build();

		Request request = new Request.Builder()
				.url(url_s)
				.post(body)
				.build();


		OkHttpClient client = new OkHttpClient().newBuilder().
				readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
				.writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
				.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
				.build();

		try {
			Response res = client.newCall(request).execute();
			res_st = res.body().string();


		} catch (IOException e) {
			e.printStackTrace();
		}

		return res_st;


	}
}
