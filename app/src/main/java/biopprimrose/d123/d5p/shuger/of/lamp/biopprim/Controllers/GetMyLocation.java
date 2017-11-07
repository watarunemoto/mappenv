package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/11/14.
 */
public class GetMyLocation implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    //位置情報関連
    LocationManager mLocationManager;

    String loc;

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    //ネットワークの状態を取得する
    ConnectivityManager cm;

    //gpsの状態
    String gpsStatus;


    //fuselocation
    private GoogleApiClient googleApiClient;
    private boolean mResolvingError = false;

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationRequest locationRequest;
    private Location location;

    private String lati;
    private String longi;

    //connectionのフラッグ
    private boolean flag = false;

    private Context context;

    public GetMyLocation(Context context) {
        this.context = context;
    }

    public void getMylocation() {

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //fuselocationapi
        locationRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(3000)
                .setInterval(1000)
                .setFastestInterval(500);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        //ネットワークの状態の取得関連
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //GPSの状態の取得
        gpsStatus = Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        googleApiClient.connect();

    }


    @Override
    public void onLocationChanged(Location location) {
        DecimalFormat df = new DecimalFormat("0.000000");
        //locationから緯度経度の取得
        lati = df.format(location.getLatitude());
        longi = df.format(location.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        gpsStatus = Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Location creentlocation = fusedLocationProviderApi.getLastLocation(googleApiClient);
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        if (creentlocation != null) {
            location = creentlocation;
            //桁数の指定
            DecimalFormat df = new DecimalFormat("0.000000");
            //locationから緯度経度の取得
            MyLocation myLocation = new MyLocation();
            myLocation.setMylocation(new LatLng(location.getLatitude(),location.getLongitude()));
            EventBus.getDefault().post(myLocation);
            googleApiClient.disconnect();
            Log.v("getmylocation","got location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
