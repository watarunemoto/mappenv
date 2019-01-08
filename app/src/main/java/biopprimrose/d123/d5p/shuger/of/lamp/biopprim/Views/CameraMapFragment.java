package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

//import static biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.CameraMapFragment.TransparencyUrlTileProvider.OSM_MAP_URL_FORMAT;

/**
// * A simple {@link Fragment} subclass.
// * Use the {@link CameraMapFragment#/newInstance} factory method to
// * create an instance of this fragment.
 */
public class CameraMapFragment extends Fragment {

    static MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        //tileOverlay.setTransparency(0.5f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_camera_map, container, false);

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


                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng now = new LatLng(35.985190, 139.374145);
                googleMap.addMarker(new MarkerOptions().position(now).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(now).zoom(18).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                TileProvider tileProvider = new UrlTileProvider(256, 256) {
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {

                        /* Define the URL pattern for the tile images */
                        String s = String.format("https://tile.openweathermap.org/map/precipitation_new/%d/%d/%d.png?appid=bf8fa0e5d53ae71cdde5ad8851372be4", zoom, x, y);

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
                    private boolean checkTileExists(int x, int y, int zoom) {
                        int minZoom = 0;
                        int maxZoom = 18;

                        if ((zoom < minZoom || zoom > maxZoom)) {
                            return false;
                        }
                        return true;
                    }
                };

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));

            }
        });
        return rootView;
    }


        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            mMapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mMapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mMapView.onLowMemory();
        }
}