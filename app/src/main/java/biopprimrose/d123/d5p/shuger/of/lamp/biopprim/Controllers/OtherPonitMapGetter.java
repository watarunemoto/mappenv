package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by amemiyaY on 2016/01/13.
 */
public class OtherPonitMapGetter extends AsyncTask<String, Integer, String> {

    GoogleMap map; //マップ
    Context context; //データベースのため
    String tLat, bLat, leLongi, riLongi; //座標
    HttpResponse res;
    String res_str = "";

    public OtherPonitMapGetter(GoogleMap map, Context context, String tLat, String bLat, String leLongi, String riLongi) {
        this.map = map;
        this.context = context;
        this.tLat = tLat;
        this.bLat = bLat;
        this.leLongi = leLongi;
        this.riLongi = riLongi;
    }

    @Override
    protected String doInBackground(String... strings) {

        String url_1 = strings[0];

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_1);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String username = sp.getString("MyID", "null");

        List<NameValuePair> post_params = new ArrayList<NameValuePair>();
        post_params.add(new BasicNameValuePair("usernames", username));
        post_params.add(new BasicNameValuePair("toppplatiTUde", tLat));
        post_params.add(new BasicNameValuePair("bottomlatiTUDE", bLat));
        post_params.add(new BasicNameValuePair("leftLOngitude", leLongi));
        post_params.add(new BasicNameValuePair("rightLongituDE", riLongi));
        try {
            post.setEntity(new UrlEncodedFormEntity(post_params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            res = httpClient.execute(post);
            try {
                res_str = EntityUtils.toString(res.getEntity());
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
        return res_str;
    }

    @Override
    protected void onPostExecute(String s) {
        BitmapDescriptor ocolor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        if (!res_str.equals("")) {
            String[] data = res_str.split(":");
            for (String oneData : data) {
                String[] datd = oneData.split(",");
                if (datd[4].split("@").length != 7)
                    break;

                if (datd.length == 7) {

                    LatLng latLng = new LatLng(Double.parseDouble(datd[0]), Double.parseDouble(datd[1]));

                    map.addMarker(new MarkerOptions().position(latLng).title(datd[2]).icon(ocolor)
                            .snippet(
                                    datd[2] + "," //写真の名前 0
                                            + datd[3] + "," //class    1
                                            + datd[4].split("@")[1] + datd[4].split("@")[0] + "," //score    2
                                            + datd[5] + "," //photoname 3
                                            + datd[6]));  //useid  4
                }


            }
        }
    }
}
