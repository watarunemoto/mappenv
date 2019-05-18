package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.MapsActivity;

/**
 * Created by amimeyaY on 2015/11/18.
 */
public class MapMarker extends AsyncTask<String, Integer, String> {

    GoogleMap mMap;
    Context context;
    Activity activity;
    ProgressDialog dialog;


    SQLiteDatabase db;
    ImgOpenHelper imgOpenHelper;

    String tLat, bLat, leLongi, riLongi; //座標
    String username;
    Long locationid;
    int flag = 0;

    /**
     * 一時的にデータを入れとくlist
     */
    ArrayList<Double> latitude_list = new ArrayList<>();
    ArrayList<Double> longitude_list = new ArrayList<>();
    ArrayList<String> score_list = new ArrayList<>();
    ArrayList<String> snip_list = new ArrayList<>();
    ArrayList<String> name_list = new ArrayList<>();
    List<MarkerCollection> markerCollections;

    public MapMarker(GoogleMap mMap, Context context, ImgOpenHelper imgOpenHelper, Long locationid,
                     String tLat, String bLat, String leLongi, String riLongi,
                     List<MarkerCollection> markerCollections
    ) {
        this.mMap = mMap;
        this.context = context;
        this.imgOpenHelper = imgOpenHelper;
        this.locationid = locationid;
        this.tLat = tLat;
        this.bLat = bLat;
        this.leLongi = leLongi;
        this.riLongi = riLongi;
        this.markerCollections = markerCollections;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... strings) {

        db = imgOpenHelper.getReadableDatabase();
        //markerset
        //DBopen
        try {
            //処理
            Cursor c = db.query(
                    ImgContract.Images.TABLE_NAME,
                    null,
                    ImgContract.Images.COL_LAT + " <= ? AND " +
                            ImgContract.Images.COL_LAT + " >= ? AND " +
                            ImgContract.Images.COL_LNG + " >= ? AND " +
                            ImgContract.Images.COL_LNG + " <= ? AND " +
                            ImgContract.Images.COL_ISDELETED + " = ?",
                    new String[]{tLat, bLat, leLongi, riLongi, "0"},
                    null,
                    null,
                    "updated desc",
                    "50"
            );

            while (c.moveToNext()) {
                String lat = c.getString(c.getColumnIndex(ImgContract.Images.COL_LAT));
                String lng = c.getString(c.getColumnIndex(ImgContract.Images.COL_LNG));

                latitude_list.add(Double.parseDouble(lat));
                longitude_list.add(Double.parseDouble(lng));

                score_list.add(c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE)));
                snip_list.add(c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME)));
                name_list.add(c.getString(c.getColumnIndex(ImgContract.Images.COL_PNAME)));
            }
            c.close();

            if (locationid != 0L) {
                Cursor c2 = db.query(
                        ImgContract.Images.TABLE_NAME,
                        null,
                        ImgContract.Images._ID + "= ? and " + ImgContract.Images.COL_ISDELETED + "= ?",
                        new String[]{String.valueOf(locationid), "0"},
                        null,
                        null,
                        null,
                        null
                );

                c2.moveToFirst();
                Double lat2 = Double.parseDouble(c2.getString(c2.getColumnIndex(ImgContract.Images.COL_LAT)));
                Double lng2 = Double.parseDouble(c2.getString(c2.getColumnIndex(ImgContract.Images.COL_LNG)));
                String score2 = c2.getString((c2.getColumnIndex(ImgContract.Images.COL_SCORE)));
                String pname2 = c2.getString(c2.getColumnIndex(ImgContract.Images.COL_PNAME));
                String snip2 = c2.getString(c2.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));

                latitude_list.add(lat2);
                longitude_list.add(lng2);

                score_list.add(score2);
                snip_list.add(snip2);
                name_list.add(pname2);


                c2.close();
            }

            db.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * データベースから読み込んだデータをマーカーとして
     * MapsActiviyに表示する
     * フォトリストから飛んできた場合にはIDを受け取りそのIDに対応するマーカーを赤く表示する
     * @param
     */

    @Override
    protected void onPostExecute(String s) {
        BitmapDescriptor nonleaficon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        BitmapDescriptor myleaf = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        BitmapDescriptor primrose = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);

        LatLng southwest = new LatLng(Double.parseDouble(bLat), Double.parseDouble(leLongi));
        LatLng northeast = new LatLng(Double.parseDouble(tLat), Double.parseDouble(riLongi));

        int count = 0;
        if (markerCollections != null && markerCollections.size() > 0) {
            for (MarkerCollection markerCollection : markerCollections) {

                LatLng marker_position =
                        new LatLng(markerCollection.getMarker().getPosition().latitude,
                                markerCollection.getMarker().getPosition().longitude);

                if (!(marker_position.latitude >= southwest.latitude) &&
                        !(marker_position.latitude <= northeast.latitude) &&
                        !(marker_position.longitude >= southwest.longitude) &&
                        !(marker_position.longitude <= northeast.longitude)
                        ) {
                    markerCollections.remove(count);
                }

                count++;

            }
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        username = sp.getString("MyID", "null");
        if (!latitude_list.isEmpty()) {
            for (int i = 0; i < latitude_list.size() - 1; i++) {
                String no1 = score_list.get(i).split(",")[0].split(":")[0].replace("'", "").replace(" ", "");
                Marker marker = null;

                if (no1.equals("others")) {
                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_list.get(i),
                            longitude_list.get(i))).title(name_list.get(i)).icon(nonleaficon).snippet(snip_list.get(i) + "," + username));

                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_list.get(i),
                            longitude_list.get(i))).title(name_list.get(i)).icon(icon).snippet(snip_list.get(i) + "," + username));
                }

/*
                //マーカータップイベント
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //todo マーカーをタップした時に起きるイベント(画面遷移orTOAST表示)
                        return false;
                    }
                });
*/

                boolean flag_is_dou = true;
                if (markerCollections != null && markerCollections.size() > 0) {
                    for (MarkerCollection markerCollection : markerCollections) {
                        if (snip_list.get(i) == markerCollection.getMarker().getSnippet().split(",")[0]) {
                            flag_is_dou = false;
                        }
                    }
                }
                if (flag_is_dou) {
                    MarkerCollection markerC = new MarkerCollection();
                    markerC.setMarker(marker);
                    markerCollections.add(markerC);
                }
                if (markerCollections.size() > 50) {
                    break;
                }
            }
        }

        EventBus.getDefault().post(markerCollections);


        if (locationid != 0L) {
            int sizeOflist = latitude_list.size() - 1;
            Marker marker_selected = mMap.addMarker(new MarkerOptions().position(
                    new LatLng(
                            latitude_list.get(sizeOflist), longitude_list.get(sizeOflist)))
                    .title(name_list.get(sizeOflist))
                    .icon(myleaf)
                    .snippet(snip_list.get(sizeOflist))
            );
            marker_selected.showInfoWindow();
        }
        /*cd
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(context.getApplicationContext(),JmaDataDisp.class);
                LatLng latlng = marker.getPosition();
                double lat = latlng.latitude;
                double lng =latlng.longitude;
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                context.startActivity(intent);
                return false;
            }
        });*/
    }
}


