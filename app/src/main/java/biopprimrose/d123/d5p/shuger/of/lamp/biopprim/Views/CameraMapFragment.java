package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


/**
// * A simple {@link Fragment} subclass.
// * Use the {@link CameraMapFragment#/newInstance} factory method to
// * create an instance of this fragment.
 */
public class CameraMapFragment extends Fragment {

    static MapView mMapView;
    static GoogleMap googleMap;
    static TileOverlay mOverlay;
    static String mQueryKey;
    static String mQueryValue;
    static String mlatitude;
    static String mlongitude;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        final View rootView = inflater.inflate(R.layout.fragment_camera_map, container, false);

//        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        Bundle camerabundle = getArguments();
        mlatitude = camerabundle.getString("latitude");
        mlongitude = camerabundle.getString("longitude");

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mlatitude),Double.parseDouble(mlongitude)),15);
//                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.720148,139.744347),18);
//                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.985233,139.374192),18);
                googleMap.moveCamera(cUpdate);

                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);


                //ヒートマップを表示させる
//                GetData HeatMap = new GetData();
//                HeatMap.execute();
            }
        });
        return rootView;
    }

//
//    static class GetData extends AsyncTask<Integer,Integer, String> {
//        @Override
//        protected String doInBackground(Integer... value) {
//
//            Log.d("SampleLoader", "loadInBackground");
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException ignored) {
//
//            }
//
//            int responseCode = 0;
//            ArrayList<LatLng> responseData = null;
//            HttpURLConnection connection = null;
//            URL url;
//            String urlStr = URL_GET_LATLNG.toString();
//            Log.d(urlStr, "Urlstr");
//            String jsontext="";
//
//            HashMap<String, String> queryParams = new HashMap<>();
//            queryParams.put(mQueryKey, mQueryValue);
//            Log.d(queryParams.toString(), "PostQueryParams");
//            try {
//                if (mQueryKey != null | mQueryValue != null) {
//                    Uri.Builder builder = new Uri.Builder();
//                    Set keys = queryParams.keySet();
//                    for (Object key : keys) {
//                        builder.appendQueryParameter(key.toString(), queryParams.get(key));
//                    }
//                    url = new URL(urlStr + builder.toString());
//                } else {
//                    url = new URL(urlStr);
//                }
//
//
//
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(100000);
//            connection.setReadTimeout(100000);
//            connection.setRequestProperty("User-Agent", "Android");
//            connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(false);
//            connection.setDoInput(true);
//            connection.connect();
//
//
//            InputStream in = connection.getInputStream();
//            responseCode = connection.getResponseCode();
//            Log.d("doInBackground", "doInBackground: "+responseCode);
//
//
//            if (responseCode == 200) {
//                    StringBuffer stringBUffer = new StringBuffer();
//                    StringBuilder result = new StringBuilder();
//                    InputStreamReader inReader = new InputStreamReader(in);
//                    BufferedReader bufferedReader = new BufferedReader(inReader);
//                    // Log.d("doinbackground", "bufferREader"+bufferedReader.readLine());
//                    String line ;
//                    //Log.d("result",result.toString());
//                    while ((line = bufferedReader.readLine()) != null) {
//                        stringBUffer.append(line);
//                        //result.append(line);
//                    }
//                    //Log.d("aaaaaaa", "doInBackground: "+result);
//                    jsontext = stringBUffer.toString();
//                    try{
//                        Log.d("doInBackground", "doInBackground:");
//                        JSONArray jsonArray = new JSONObject(jsontext).getJSONArray("json");
//                        Log.d("aaaaaaa", "doInBackground: "+jsonArray);
//                    }catch (JSONException e){
//
//                    }
//                        bufferedReader.close();
//                        inReader.close();
//                        in.close();
//            }else{
//                //Toast.makeText(getActivity(),"ヒートマップを表示できませんでした",Toast.LENGTH_SHORT).show();
//            }
//
//            } catch (IOException e) {
//             e.printStackTrace();
//            } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        //Log.d("execute", "URL:" + urlStr);
//        //Log.d("execute", "HttpStatusCode:" + responseCode);
//        //Log.d("execute", "ResponseData:" + responseData);
//        //Log.d("doinbackground", "結果は"+responseData);
//        Log.d("doInBackground", "jsontext is "+jsontext);
//        return jsontext;
//
//
//    }
//
//
//    @Override
//    protected void onPostExecute(String jsontext){
//        ArrayList<LatLng> list = new ArrayList<LatLng>();
//        JSONArray jsonarray =new JSONArray();
//        Log.d("onPostExecute", jsontext);
//            if (jsontext != "" ){
//                try {
//                    jsonarray = new JSONArray(jsontext);
//                    Log.d("onPostExecute", "onPostExecute: " + jsonarray);
//                } catch (JSONException e) {
//
//                }
//
//
//                try {
//                    for (int i = 0; i < jsonarray.length(); i++) {
//                        JSONObject object = jsonarray.getJSONObject(i);
//                        double lat = object.getDouble("latitude");
//                        double lng = object.getDouble("longitude");
//                        list.add(new LatLng(lat, lng));
//                        //Log.d("latlng", "onPostExecute: " + lat + "," + lng);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//
//                //得られたデータからHeatMapProviderを作成
//                Log.d("list", "onPostExecute: " + list);
//                if (list.size()!=0){
//                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder().data(list).build();
//                //ヒートマップを表示させる。
//                mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
////                mOverlay.remove();
//                    }
//                }
//        }
//    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}