package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.util.Log;

import com.google.android.gms.location.LocationResult;

//import biopprimrose.d123.d5p.shuger.of.lamp.R;


public class GPSFragment extends GPSFragmentAbstract {


    public GPSFragment() {
        // Required empty public constructor
    }


    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult == null) {
            Log.e("GPSNull","# No location data.");
            return;
        }

        // 緯度・経度を取得
        double latitude = locationResult.getLastLocation().getLatitude();
        double longitude = locationResult.getLastLocation().getLongitude();
    }

}
