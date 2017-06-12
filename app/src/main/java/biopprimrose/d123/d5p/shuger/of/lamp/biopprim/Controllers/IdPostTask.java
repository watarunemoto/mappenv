package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amemiyaY on 2016/04/23.
 */
public class IdPostTask extends AsyncTask<String , String ,String>{

	String str;
	SharedPreferences sp;
	public IdPostTask(SharedPreferences sp){
		this.sp = sp;
	}


	@Override
	protected String doInBackground(String... strings) {
		String url = strings[0];
		String user_name = strings[1];


		MultipartEntityBuilder entity = MultipartEntityBuilder.create();

		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		RequestBody body = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart(
						"userid", user_name

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
			res_st = res.body().string().replace('"',' ').replace(" ","");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res_st;
	}

	@Override
	protected void onPostExecute(String s) {
		sp.edit().putString("user_hash",str).apply();
	}
}
