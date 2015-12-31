package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by amimeyaY on 2015/11/18.
 */
public class MapMarker extends AsyncTask<String,Integer,String> {

	GoogleMap mMap;
	Context context;
	Activity activity;
	ProgressDialog dialog;

	SQLiteDatabase db;
	ImgOpenHelper imgOpenHelper;
	/**
	 * 一時的にデータを入れとくlist
	 */

	ArrayList<Double> latitude_list = new ArrayList<>();
	ArrayList<Double> longitude_list = new ArrayList<>();
	ArrayList<String> score_list = new ArrayList<>();
	ArrayList<String> snip_list = new ArrayList<>();
	ArrayList<String> name_list = new ArrayList<>();



	public MapMarker(GoogleMap mMap,Context context,FragmentActivity activity,ImgOpenHelper imgOpenHelper) {
		this.mMap = mMap;
		this.context = context;
		this.activity = activity;
		this.imgOpenHelper = imgOpenHelper;
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
					null,
					null,
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
			db.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return  null;

	}

	@Override
	protected void onPostExecute(String s) {
		BitmapDescriptor nonleaficon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

		if (!latitude_list.isEmpty()) {

			for (int i = 0; i < latitude_list.size(); i++) {
				if (score_list.get(i).equals("0")) {
					Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_list.get(i), longitude_list.get(i))).title(name_list.get(i)).icon(nonleaficon).snippet(snip_list.get(i)));
					marker.showInfoWindow();
				} else if (Double.parseDouble(score_list.get(i)) < 1.00) {
					Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_list.get(i), longitude_list.get(i))).title(name_list.get(i)).icon(icon).snippet(snip_list.get(i)));
					marker.showInfoWindow();
				} else {
					Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_list.get(i), longitude_list.get(i))).title(name_list.get(i)).icon(icon).snippet(snip_list.get(i)));
					marker.showInfoWindow();
				}
			}
		}
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
