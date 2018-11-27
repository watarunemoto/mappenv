package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.support.v4.app.Fragment;

public abstract class GPSFragmentAbstract extends Fragment implements MyLocationManager.OnLocationResultListener {

    private MyLocationManager locationManager;

    @Override
    public void onResume() {
        super.onResume();

        locationManager = new MyLocationManager(getContext(), this);
        locationManager.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }


}

