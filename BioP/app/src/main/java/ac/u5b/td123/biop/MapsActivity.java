package ac.u5b.td123.biop;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

	private GoogleMap mMap; // Might be null if Google Play services APK is not available.

	FragmentActivity activity;
	Context context;

	final Handler handler = new Handler();

	final SpinerDialog dialogFragment = SpinerDialog.newInstance(R.string.dialog_title, R.string.dialog_message);
	final ImgOpenHelper imgOpenHelper = new ImgOpenHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);


		dialogFragment.show(getFragmentManager(), "dialog_fragment");
		setUpMapIfNeeded();
		activity = this;
		context = getApplicationContext();
		if (mMap != null) {
			mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View view = getLayoutInflater().inflate(R.layout.info_window, null);
					//タイトル設定
					TextView title = (TextView) view.findViewById(R.id.info_title);
					title.setText(marker.getTitle());
					//画像設定
					ImageView img = (ImageView) view.findViewById(R.id.info_image);
					img.setImageBitmap(BitmapFactory.decodeFile(marker.getSnippet()));
					return view;
				}
			});

			SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map));

			//現在位置ボタン
			mMap.setMyLocationEnabled(true);
			UiSettings settings = mMap.getUiSettings();
			settings.setMyLocationButtonEnabled(true);

			mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
				@Override
				public boolean onMyLocationButtonClick() {

					Log.v("Map", "Zoom Level = " + mMap.getCameraPosition().zoom);
					return false;
				}
			});
		} else {
			Toast.makeText(this, "マップが開けません", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		dialogFragment.dismiss();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	public void moveAndzoom(List<LatLng> latLngList) {
		if (mMap == null || latLngList.size() == 0)
			return;

		final LatLngBounds.Builder builder = LatLngBounds.builder();
		for (LatLng latLng : latLngList) {
			builder.include(latLng);
		}
		//
		mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
		mMap.setOnCameraChangeListener(null);
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.

			if (mMap != null) {
				setUpMap();
			}

		}
	}

	private void setUpMap() {
		/**マーカーを動的に表示するために少しディレイを加えたら上手く行った
		 *
		 */
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {


				if (mMap == null)
					return;
				//markerset
				//DBopen

				SQLiteDatabase db = imgOpenHelper.getReadableDatabase();

				//処理
				Cursor c = db.query(
						ImgContract.Images.TABLE_NAME,
						null,
						null,
						null,
						null,
						null,
						"updated desc",
						"50"
				);
				Log.v("DB_TEST", "Count: " + c.getCount());
				List<LatLng> latLngList = new ArrayList<>();
				Double lat_ = 0.0;
				Double lng_ = 0.0;
				while (c.moveToNext()) {
					String lat = c.getString(c.getColumnIndex(ImgContract.Images.COL_LAT));
					String lng = c.getString(c.getColumnIndex(ImgContract.Images.COL_LNG));
					String detect = c.getString(c.getColumnIndex(ImgContract.Images.COL_SCORE));
					BitmapDescriptor nonleaficon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
					BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
					//snippetに画像のパスを渡してしまう
					String snip = c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));

					lat_ = Double.parseDouble(lat);
					lng_ = Double.parseDouble(lng);
					LatLng point = new LatLng(lat_, lng_);
					latLngList.add(point);
					//BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.mark);
					if (detect.equals("0")) {
						mMap.addMarker(new MarkerOptions().position(new LatLng(lat_, lng_)).title("葉っぱ以外のなにか").icon(nonleaficon).snippet(snip));
					} else if (Double.parseDouble(detect) < 1.00) {
						mMap.addMarker(new MarkerOptions().position(new LatLng(lat_, lng_)).title("葉っぱかも...").icon(icon).snippet(snip));
					} else {
						mMap.addMarker(new MarkerOptions().position(new LatLng(lat_, lng_)).title("葉っぱです").icon(icon).snippet(snip));
					}
				}
				moveAndzoom(latLngList);
				c.close();
				db.close();
			}
		}, 1000);

	}
}
