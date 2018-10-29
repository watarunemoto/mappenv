package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersListAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RecycleAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amemiyaY on 2016/11/04.
 */
public class LoadDataTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private String res_st;
    private LatLngBounds latLngBounds;
    private LatLng latLng;
    private String username;
    private String userid;
    private String otherid;


    private List<OtherUsersList> photo_list_and_native_path;
    private ProgressDialog dialog;
    
    
    private OtherUsersListAdapter otherUsersListAdapter = null;
    private RecycleAdapter recycleAdapter = null;

    public LoadDataTask(Context context) {
        this.context = context;
    }

    public LoadDataTask(Context context, LatLngBounds latLngBounds) {
        this(context);
        this.latLngBounds = latLngBounds;
    }

    public LoadDataTask(Context context, LatLngBounds latLngBounds, OtherUsersListAdapter adapter) {
        this(context, latLngBounds);
        this.otherUsersListAdapter = adapter;
    }

    public LoadDataTask(Context context, LatLngBounds latLngBounds, RecycleAdapter adapter) {
        this(context, latLngBounds);
        this.recycleAdapter = adapter;
    }
    
    public LoadDataTask(Context context, LatLng latLng) {
        this(context);
        this.latLng = latLng;
    }
    
    public LoadDataTask(Context context, String username) {
        this(context);
        this.username = username;
    }

    private String[] downloadlist_fromuser(String url, String username) {

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "other_id", username
                ).addFormDataPart(
                        "my_id", "000000"
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

        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string().replace('"',' ').replace(" ","");
            res_array = res_st.split(";");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return res_array;


    }
    
    
    private String[] downloadlist_frompos(String url, LatLngBounds latLngBounds, String userid) {

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        LatLng northeast = latLngBounds.northeast;
        LatLng southwest = latLngBounds.southwest;

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "userid", userid
                )
                .addFormDataPart(
                        "toplat", String.valueOf(northeast.latitude)
                )
                .addFormDataPart(
                        "bottomlat", String.valueOf(southwest.latitude)
                )
                .addFormDataPart(
                        "leftlng", String.valueOf(southwest.longitude)
                )
                .addFormDataPart(
                        "rightlng", String.valueOf(northeast.longitude)
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

        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string();
            res_array = res_st.split(";");

            //Log.v("respose", res_st);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res_array;
    }


    private String downloadimg(String url, String filename) {

        String imgName;
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "filename", filename
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


        try {
            Response res = client.newCall(request).execute();
            InputStream photo = res.body().byteStream();

            FileOutputStream myFOS;

            File file = new File("/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/cmr/");

            //フォルダが存在しなかった場合にフォルダを作成
            if (!file.exists()) {
                file.mkdir();
            }
            //オリジナルの名前の生成
            imgName = "/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/cmr/" + filename + "_tmp.jpg";


            try {
                myFOS = new FileOutputStream(imgName);

                byte[] data = new byte[1024];
                long total = 0;
                int count = 0;
                while ((count = photo.read(data)) != -1) {
                    total += count;
                    myFOS.write(data, 0, count);
                }

                myFOS.close();
                res.close();
                Log.v("filename", imgName);
                return imgName;


            } catch (Exception e) {
                e.printStackTrace();
            }
            photo.reset();
            photo.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public LatLngBounds latlng_to_latlngbounds(LatLng latLng, Double distance) {

        //create latlngbounds from distace (適当)
        int earth_radius = 6378150;

        //per meter
        Double earth_circle = 2 * Math.PI * earth_radius;
        Double per_meter = 360/earth_circle;
        Double distance_from_center = per_meter * Math.cos(45*Math.PI/180) * distance;

        Double north = latLng.latitude + distance_from_center;
        Double south = latLng.latitude - distance_from_center;

        Double west = latLng.longitude - distance_from_center;
        Double east = latLng.longitude + distance_from_center;

        LatLng northeast = new LatLng(north,east);
        LatLng southwest = new LatLng(south,west);

        LatLngBounds latLngBounds = new LatLngBounds(southwest,northeast);

        return latLngBounds;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.v("loaddatatask", "loading loadbackgournd");

        String url_list = UrlCollections.URL_AREA_POINT;
        String url_photo = UrlCollections.URL_THUMBNAL;
        String url_from_username = UrlCollections.URL_FROM_USERNAME;

        if (strings.length > 0) {
            return null;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String userid = sp.getString("username", "");

        String[] photo_list = null;

        if (latLng != null) {
            latLngBounds = latlng_to_latlngbounds(latLng,2000d);
            Log.v("loaddatatask","rannraran" + ","+latLngBounds.northeast +","+latLngBounds.southwest);

            photo_list = downloadlist_frompos(url_list, latLngBounds, userid);
            Log.v("latlng_bounts",photo_list+"");
            sp.edit().putBoolean("loadtask_is_load",false).apply();

        } else if (latLngBounds != null) {
            photo_list = downloadlist_frompos(url_list, latLngBounds, userid);
            Log.v("latlngBouts","isnotnull");
        } else if (username != null) {
            photo_list = downloadlist_fromuser(url_from_username,username);
            Log.v("username","isnotnull");
        }


        photo_list_and_native_path = new ArrayList<OtherUsersList>();
        long count = 0;

        if (photo_list == null) {
            Log.v("loaddatatask","photo_list is null");
            return null;
        }
        for (String point : photo_list) {
            OtherUsersList opul = new OtherUsersList();

            String[] point_to_array = point.split(">");
            if (point_to_array.length == 4) {

                String filename = point_to_array[2];
                String photo_name = point_to_array[0];
                Log.v("filename_loaddatatask",filename);
                String catch_path = downloadimg(url_photo, filename);
                if (catch_path != null) {
                    Log.v("filename_loaddatatask", catch_path);
                }
                opul.setId(count);
                opul.setName(photo_name);
                opul.setFilepath(catch_path);
                opul.setUserid(point_to_array[3]);

                Log.v("photoffdf",count+","+ photo_name +","+ catch_path+","+point_to_array[3]);

                photo_list_and_native_path.add(opul);
                count++;
            }
        }

        if (otherUsersListAdapter == null) {
            otherUsersListAdapter = new OtherUsersListAdapter(context);
            otherUsersListAdapter.setOtherUsersLists(photo_list_and_native_path);
        } else {
            //adapter.removeOtherUsersListitem();
            otherUsersListAdapter.addOtherUsersLists(photo_list_and_native_path);
        }

        Log.v("loaddatatask", "loading end");
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        /**
        if (dialog != null) {
            dialog.dismiss();
        }
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.v("loaddatatask","loadend"+photo_list_and_native_path.size());
        if (photo_list_and_native_path != null && photo_list_and_native_path.size() > 0) {
            EventBus.getDefault().post(photo_list_and_native_path);
            sp.edit().putBoolean("loadtask_is_load",true).apply();
        }
    }
}

