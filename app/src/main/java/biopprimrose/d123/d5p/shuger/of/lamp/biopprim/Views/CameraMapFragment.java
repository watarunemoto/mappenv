package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

import static biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections.URL_GET_LATLNG;
import com.google.android.gms.maps.model.MarkerOptions;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;



/**
// * A simple {@link Fragment} subclass.
// * Use the {@link CameraMapFragment#newInstance} factory method to
// * create an instance of this fragment.
 */
public class CameraMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.985233,139.374192),18);
                googleMap.moveCamera(cUpdate);

                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.719029,139.7455905),15);
                googleMap.moveCamera(cUpdate);


                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                //LatLng now = new LatLng(35.985190, 139.374145);
                //googleMap.addMarker(new MarkerOptions().position(now).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(now).zoom(18).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                /*
                //OpenWeatherMapから降雨量データを取得、表示させる。
                TileProvider tileProvider = new UrlTileProvider(256, 256) {
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {

                        /* Define the URL pattern for the tile images */
                 /*       String s = String.format("https://tile.openweathermap.org/map/precipitation_new/%d/%d/%d.png?appid=bf8fa0e5d53ae71cdde5ad8851372be4", zoom, x, y);

                        if (!checkTileExists(x, y, zoom)) {
                            return null;
                        }

                        try {
                            return new URL(s);
                        } catch (MalformedURLException e) {
                            throw new AssertionError(e);
                        }
                    }

                    /*
                     * Check that the tile server supports the requested x, y and zoom.
                     * Complete this stub according to the tile range you support.
                     * If you support a limited range of tiles at different zoom levels, then you
                     * need to define the supported x, y range at each zoom level.
                     */
                   /* private boolean checkTileExists(int x, int y, int zoom) {
                        int minZoom = 0;
                        int maxZoom = 18;

                        if ((zoom < minZoom || zoom > maxZoom)) {
                            return false;
                        }
                        return true;
                    }
                };

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));*/

                //ヒートマップを表示させる
                GetData HeatMap = new GetData();
                HeatMap.execute();
            }
        });

        return rootView;
    }


static class GetData extends AsyncTask<Integer,Integer, String> {


    @Override
    protected String doInBackground(Integer... value) {

        Log.d("SampleLoader", "loadInBackground");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }

        int responseCode = 0;
        ArrayList<LatLng> responseData = null;
        HttpURLConnection connection = null;
        URL url;
        String urlStr = URL_GET_LATLNG.toString();
        Log.d(urlStr, "Urlstr");
        String jsontext="";

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(mQueryKey, mQueryValue);
        Log.d(queryParams.toString(), "PostQueryParams");
        try {
            if (mQueryKey != null | mQueryValue != null) {
                Uri.Builder builder = new Uri.Builder();
                Set keys = queryParams.keySet();
                for (Object key : keys) {
                    builder.appendQueryParameter(key.toString(), queryParams.get(key));
                }
                url = new URL(urlStr + builder.toString());
            } else {
                url = new URL(urlStr);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.setRequestProperty("User-Agent", "Android");
            connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();


            InputStream in = connection.getInputStream();
            responseCode = connection.getResponseCode();
            Log.d("doInBackground", "doInBackground: "+responseCode);


            if (responseCode == 200) {
                    StringBuffer stringBUffer = new StringBuffer();
                    StringBuilder result = new StringBuilder();
                    InputStreamReader inReader = new InputStreamReader(in);
                    BufferedReader bufferedReader = new BufferedReader(inReader);
                    // Log.d("doinbackground", "bufferREader"+bufferedReader.readLine());
                    String line ;
                    //Log.d("result",result.toString());
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBUffer.append(line);
                        //result.append(line);
                    }
                    //Log.d("aaaaaaa", "doInBackground: "+result);
                    jsontext = stringBUffer.toString();
                    try{
                        Log.d("doInBackground", "doInBackground:");
                        JSONArray jsonArray = new JSONObject(jsontext).getJSONArray("json");
                        Log.d("aaaaaaa", "doInBackground: "+jsonArray);
                    }catch (JSONException e){
                    }
                    bufferedReader.close();
                    inReader.close();
                    in.close();
            }else{

                //Toast.makeText(getActivity(),"ヒートマップを表示できませんでした",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        //Log.d("execute", "URL:" + urlStr);
        //Log.d("execute", "HttpStatusCode:" + responseCode);
        //Log.d("execute", "ResponseData:" + responseData);
        //Log.d("doinbackground", "結果は"+responseData);
        Log.d("doInBackground", "jsontext is "+jsontext);
        return jsontext;


    }


    @Override
    protected void onPostExecute(String jsontext){
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        JSONArray jsonarray =new JSONArray();
        Log.d("onPostExecute", jsontext);
            if (jsontext != "" ){
                try {
                    jsonarray = new JSONArray(jsontext);
                    Log.d("onPostExecute", "onPostExecute: " + jsonarray);
                } catch (JSONException e) {

                }


                try {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject object = jsonarray.getJSONObject(i);
                        double lat = object.getDouble("latitude");
                        double lng = object.getDouble("longitude");
                        list.add(new LatLng(lat, lng));
                        //Log.d("latlng", "onPostExecute: " + lat + "," + lng);
                    }
                } catch (JSONException e) {

                }


                //得られたデータからHeatMapProviderを作成
                Log.d("list", "onPostExecute: " + list);
                if (list.size()!=0){
                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder().data(list).build();
                //ヒートマップを表示させる。
                googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                }
            }
    }
}