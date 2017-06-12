package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.Draw_line;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.MapMarker;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.MarkerCollection;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    FragmentActivity activity;
    Context context;

    final ImgOpenHelper imgOpenHelper = new ImgOpenHelper(this);
    final OtherOpenHelper ooh = new OtherOpenHelper(this);

    private long locationId;

    private int flag = 0;

    Draw_line draw_line;

    SharedPreferences sp;
    String username;

    private Button map_load_button;

    private boolean is_button_clicked = false;


    SupportMapFragment mapfragment;

    public static final String MAP_EVA = "MapsActivity";

    public static String now_pos = "35.985877, 139.373096, 17.0";
    private List<MarkerCollection> markerCollections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //フォトリストからの値の受け取り
        Intent intent = getIntent();
        locationId = intent.getLongExtra(PhotoResultFormActivity.MAP_ID, 0L);

        map_load_button = (Button)  findViewById(R.id.map_load_button);
        //自分のidの取得
        sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        username = sp.getString("MyID", "null");


        mapfragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));


        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mapfragment.setRetainInstance(true);
        } else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.
            mMap = mapfragment.getMap();
        }

        if (mMap == null) {
            mapfragment = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map));
            mMap =  mapfragment.getMap();
        }


        activity = this;
        context = getApplicationContext();
        if (mMap != null) {

            //初期位置の変更
            iniPos();

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    //snippetにいれた値 画像のパス,ユーザー名の配
                    String[] data = marker.getSnippet().split(",");
                    View view = getLayoutInflater().inflate(R.layout.info_window, null);

                    //タイトル設定
                    TextView title = (TextView) view.findViewById(R.id.info_title);
                    title.setText(marker.getTitle());
                    //画像設定
                    ImageView img = (ImageView) view.findViewById(R.id.info_image);
                    img.setImageBitmap(BitmapFactory.decodeFile(data[0]));
                    return view;
                }
            });

            //現在位置ボタン
            mMap.setMyLocationEnabled(true);
            UiSettings settings = mMap.getUiSettings();
            settings.setMyLocationButtonEnabled(true);

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                }
            });



            //グリッド読み込み
            map_load_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    map_load_button.setEnabled(false);
                    Projection proj = mMap.getProjection();
                    VisibleRegion vR = proj.getVisibleRegion();
                    String bLat = String.valueOf(vR.latLngBounds.southwest.latitude);
                    String lLong = String.valueOf(vR.latLngBounds.southwest.longitude);
                    String tLat = String.valueOf(vR.latLngBounds.northeast.latitude);
                    String rLong = String.valueOf(vR.latLngBounds.northeast.longitude);

                    String[] packdata = new String[] {bLat,lLong,tLat,rLong};

                    Bundle bundle = new Bundle();
                    bundle.putStringArray("packdata",packdata);

                    OtherUsersPhotoListFragment ouplf = new OtherUsersPhotoListFragment();

                    ouplf.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.map_form_fragment, ouplf)
                            .addToBackStack("fragment_map_list")
                            .commit();

                }
            });

            //マップ閲覧の際の位置情報が変わったらその画面内に表示されている座標範囲を表示

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if (flag == 1) {


                        if (is_button_clicked) {
                            map_load_button.setEnabled(true);
                        }

                        is_button_clicked = true;

                        Projection proj = mMap.getProjection();
                        VisibleRegion vR = proj.getVisibleRegion();
                        String tLat = String.valueOf(vR.latLngBounds.northeast.latitude);
                        String bLat = String.valueOf(vR.latLngBounds.southwest.latitude);
                        String lLong = String.valueOf(vR.latLngBounds.southwest.longitude);
                        String rLong = String.valueOf(vR.latLngBounds.northeast.longitude);

                        LatLng now_latlng = vR.latLngBounds.getCenter();
                        float zoomer = mMap.getCameraPosition().zoom;

                        now_pos = String.valueOf(now_latlng.latitude) + ","
                                + String.valueOf(now_latlng.longitude) + ","
                                + String.valueOf(zoomer);

                        Log.v("now_pos",now_pos);

                        MapMarker mapMarker = new MapMarker(mMap, MapsActivity.this, imgOpenHelper,
                                locationId, tLat, bLat, lLong, rLong,markerCollections);
                        mapMarker.execute();

                    }
                }
            });






            //マップが形成された時に呼び出される処理
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    startPos();
                    flag = 1;
                }
            });




            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // 渡すデータを準備する
                    String[] packdata;
                    packdata =  marker.getSnippet().split((","));

                    List<MarkerCollection> markerCollection_tmp = isPiledMarker(marker, markerCollections);

                    if (markerCollection_tmp.size() > 1) {

                        int array_size = markerCollection_tmp.size();

                        String[] snippets = new String[array_size];

                        int count = 0;
                        for (MarkerCollection m : markerCollection_tmp) {
                            snippets[count] = m.getMarker().getSnippet();
                            count++;
                        }

                        /**
                         * 重なったマーカーをリスト形式で閲覧できる
                         * フラグメント
                         */

                        PileMarkerListFragment fragment = new PileMarkerListFragment();
                        Bundle args = new Bundle();
                        args.putStringArray("snippets", snippets);
                        fragment.setArguments(args);

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();

                        transaction.add(R.id.map_form_fragment, fragment);
                        transaction.addToBackStack("list_fragment");
                        transaction.commit();
                        map_load_button.setEnabled(false);


                        return true;
                    } else {

                        if (packdata[1].equals(username)) {

                            PhotoResultFormFragment fragment = new PhotoResultFormFragment();
                            Bundle args = new Bundle();
                            args.putStringArray("packdata", packdata);
                            fragment.setArguments(args);

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();

                            transaction.add(R.id.map_form_fragment, fragment, "fragment_form");
                            transaction.addToBackStack("photoformfragment");
                            transaction.commit();

                        }
                        return true;
                    }
                }
            });


        } else {
            Toast.makeText(this, R.string.cant_open_map, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap == null) {
            mapfragment = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map));
            mMap = mapfragment.getMap();
        }
        draw_line = new Draw_line(mMap);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sp.edit().putString("last_map_center", now_pos).apply();
    }

    public void iniPos() {
        //zoom = 2-21 float
        Double lats = null;
        Double lons = null;
        Float zoomt = null;

        String tmp_pos = sp.getString("last_map_center", "noone");
        if (!tmp_pos.equals("noone")) {
            String[] pos = tmp_pos.split(",");
            lats = Double.valueOf(pos[0]);
            lons = Double.valueOf(pos[1]);
            zoomt = Float.valueOf(pos[2]);
        } else {
            String[] pos = now_pos.split(",");
            lats = Double.valueOf(pos[0]);
            lons = Double.valueOf(pos[1]);
            zoomt = Float.valueOf(pos[2]);
        }


        CameraPosition cameraPosition =
                new CameraPosition(new LatLng(lats, lons), zoomt, 0.0f, 0.0f);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private List<MarkerCollection> isPiledMarker(Marker marker, List<MarkerCollection> markers) {

        List<MarkerCollection> markerCollection_out = new ArrayList<>();
        Log.v("markers",markers+"");

        if (markers == null) {
            Log.v("markers","jull");

            return markerCollection_out;
        }
        for (MarkerCollection markerCollection : markers) {
            LatLng marker_pos =  marker.getPosition();
            LatLng other_markerpos = markerCollection.getMarker().getPosition();

            float[] results = new float[3];
            Location.distanceBetween(marker_pos.latitude,marker_pos.longitude,other_markerpos.latitude,other_markerpos.longitude, results);
            if (results[0] < 10) {
                markerCollection_out.add(markerCollection);
            }
        }

        return markerCollection_out;
    }



    public void moveAndzoom(List<LatLng> latLngList, final Marker marker) {
        if (mMap == null || latLngList.size() == 0)
            return;

        final LatLngBounds.Builder builder = LatLngBounds.builder();
        for (LatLng latLng : latLngList) {
            builder.include(latLng);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
    }


    public void startPos() {
        List<LatLng> latLngList = new ArrayList<>();
        ImgOpenHelper ioh = new ImgOpenHelper(MapsActivity.this);
        SQLiteDatabase db = ioh.getReadableDatabase();
        Marker marker = null;

        if (locationId != 0L) {
            Cursor c2 = db.query(
                    ImgContract.Images.TABLE_NAME,
                    null,
                    ImgContract.Images._ID + "=" + locationId,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            c2.moveToFirst();
            Double lat2 = Double.parseDouble(c2.getString(c2.getColumnIndex(ImgContract.Images.COL_LAT)));
            Double lng2 = Double.parseDouble(c2.getString(c2.getColumnIndex(ImgContract.Images.COL_LNG)));

            LatLng point2 = new LatLng(lat2, lng2);
            latLngList.add(point2);
            moveAndzoom(latLngList, marker);

            c2.close();
        } else {

            Cursor c = db.query(
                    ImgContract.Images.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "updated desc", "50");
            c.moveToFirst();
            while (c.moveToNext()) {
                String lat = c.getString(c.getColumnIndex(ImgContract.Images.COL_LAT));
                String lng = c.getString(c.getColumnIndex(ImgContract.Images.COL_LNG));

                Double lat_ = Double.parseDouble(lat);
                Double lng_ = Double.parseDouble(lng);
                LatLng point = new LatLng(lat_, lng_);
                latLngList.add(point);
            }
            moveAndzoom(latLngList, marker);
        }
    }


    @Subscribe
    public void markercreated(List<MarkerCollection> markerCollections) {

        if (markerCollections.size() > 0 && markerCollections.get(0) instanceof MarkerCollection ) {
            this.markerCollections = markerCollections;
            Log.v("marker_collections",markerCollections+"");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
