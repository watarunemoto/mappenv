package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.BreakIterator;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class EventOtherActivity extends AppCompatActivity {

    private TextView _tvLatitude;
    private TextView _tvLongitude;
    private File file;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_other);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();

        String fileName = "TestFile.txt";
        file = new File(context.getFilesDir(), fileName);

        _tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        _tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        Button buttonSave = findViewById(R.id.button_save);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPSLocationListener locationListener = new GPSLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        buttonSave.setOnClickListener( v -> {
            String text = _tvLatitude.getText().toString();
            String text = _tvLongitude.getText().toString();
            saveFile(text);

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveFile(String text) {
        try (FileWriter writer = new FileWriter(file)){
            writer.write(text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GPSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double _latitude = location.getLatitude();
            double _longitude = location.getLongitude();
            _tvLatitude.setText(Double.toString(_latitude));
            _tvLongitude.setText(Double.toString(_longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }


    }
}

