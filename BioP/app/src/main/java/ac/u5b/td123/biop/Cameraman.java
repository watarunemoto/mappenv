package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class Cameraman extends Activity implements LocationListener {

	public Context context;

	/**
	 * カメラのハードウェアを操作する {@link android.hardware.Camera}
	 */

	private Camera mCamera;

	/**
	 * カメラのプレビューを表示する {@link android.view.SurfaceView}
	 */
	private SurfaceView mView;

	//位置情報関連
	String provider;
	Location location;
	LocationManager mLocationManager;

	String loc;

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	//ネットワークの状態を取得する
	ConnectivityManager cm;

	//gpsの状態
	String gpsStatus;

	Activity activity;

	//画面連打すると落ちるのを防ぐ
	private boolean take_cut = false;

	//onCreate...Activityが実行された時に実行される
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		activity = this;
		mView = new SurfaceView(Cameraman.this);
		//Frame
		FrameLayout f1 = new FrameLayout(this);
		setContentView(f1);

		//Layoutの位置の設定　数を増やせば好きな位置に設定できる？
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(WC, WC);
		FrameLayout.LayoutParams lpc = new FrameLayout.LayoutParams(WC, WC);

		//lp.gravity = Gravity.CENTER;

		f1.addView(mView, new ViewGroup.LayoutParams(WC, WC));
		//Locationmanagerの参照
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		final boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gpsEnabled) {
			// GPSを設定するように促す
			enableLocationSettings();
		}

		//ネットワークの状態の取得関連
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		//GPSの状態の取得
		gpsStatus = android.provider.Settings.Secure
				.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	}

	//onPostCreate...onCreateの実行が終わった時に実行される
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		SurfaceHolder holder = mView.getHolder();
		holder.addCallback(surfaceHolderCallback);
		Log.v("post","postCreate");
	}

	//カメラのコールバック
	private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {

		//surfaceCreated...surfaceが形成されたときに実行
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			//カメラ実行
			mCamera = Camera.open();
			/**
			 * ズームの設定
			 */
			//ZoomControls zoomControls = (ZoomControls) findViewById(R.id.);


			/**
			 * 解像度の設定の追加
			 */

			Camera.Parameters params = mCamera.getParameters();
			List<Camera.Size> sizeList = params.getSupportedPictureSizes();
			for (int i = 0; i < sizeList.size(); i++) {
				Log.v("CameraPictureSize", "Size = " + sizeList.get(i).width + "x" + sizeList.get(i).height);
			}
			//解像度
			params.setPictureSize(640, 480);
			mCamera.setParameters(params);

			try {
				// プレビューをセットする
				mCamera.setPreviewDisplay(holder);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		//surfaceChanged...変更された時に実行
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

			//カメラの画面サイズを指定する
			Camera.Parameters parameters = mCamera.getParameters();
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
			Camera.Size previewSize = previewSizes.get(0);
			parameters.setPreviewSize( previewSize.width,previewSize.height);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// 破棄されたとき
			mCamera.release();
			mCamera = null;
		}
	};


	// シャッターが押されたときに呼ばれるコールバック
	private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
		public void onShutter() {
		}
	};

	// JPEGイメージ生成後に呼ばれるコールバック
	private Camera.PictureCallback mPictureListener = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			take_cut = false;
			// SDカードにJPEGデータを保存する
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Cameraman.this);
			//デバックオンリー
			DecimalFormat df = new DecimalFormat("0.000000");

			if (data != null) {
				//デバックオンリー
				double rlat = (Math.random() * 1) + 35;
				double rlog = (Math.random() * 1) + 139;
				String r = df.format(rlat);
				String r2 = df.format(rlog);
				loc = r + "," + r2;

				//loc = Gpskun();
				if (loc == null) {
					Toast.makeText(Cameraman.this, "Can't get location\nPlease turn on GPS\nImage not saved.", Toast.LENGTH_LONG).show();
				} else {
					//このアプリ専用のフォルダを使用
					FileOutputStream myFOS = null;
					File file = new File("/data/data/ac.u5b.td123.biop" + "/cmr/");

					//フォルダが存在しなかった場合にフォルダを作成
					if (!file.exists()) {
						file.mkdir();
					}
					//オリジナルの名前の生成
					String imgName = "/data/data/ac.u5b.td123.biop" + "/cmr/" + System.currentTimeMillis() + ".jpg";

					/**
					 * ファイルの書き込み
					 */
					try {
						myFOS = new FileOutputStream(imgName);
						myFOS.write(data);
						myFOS.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					//画像にタグを埋め込む(テスト)

					String locations[] = loc.split(",");

					String lata[] = locations[0].split("\\.");
					String logia[] = locations[1].split("\\.");

					//exifの形式変換を行っている改善の余地がある
					String lat = lata[0] + "/1," + lata[1].substring(0, 2) + "/1," + lata[1].substring(2, 6) + "/1000";
					String logi = logia[0] + "/1," + logia[1].substring(0, 2) + "/1," + logia[1].substring(2, 6) + "/1000";


					try {
						ExifInterface ex = new ExifInterface(imgName);
						ex.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
						ex.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, logi);
						ex.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
						ex.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
						ex.setAttribute(ExifInterface.TAG_MAKE, sp.getString("MyID", "tom"));
						ex.saveAttributes();
					} catch (IOException e) {
						e.printStackTrace();
					}
					yesornodialog(imgName);
				}
				camera.startPreview();
			}
		}
	};

	//画面がタッチされた時に写真が撮られる
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			if (!take_cut) {
				if (mCamera != null) {
					take_cut = true;
					mCamera.autoFocus(autoFocusCallback);
				}
			}
		}
		return true;
	}

	private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean b, Camera camera) {
			mCamera.takePicture(null, null, mPictureListener);
		}
	};
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	//アップロードするかどうかのダイアログを表示する
	public String yesornodialog(final String iMGNAME) {
		NetworkInfo nwi = cm.getActiveNetworkInfo();

		if (nwi.isAvailable()) {
			new AlertDialog.Builder(Cameraman.this)
					.setTitle("画像を判定します")
					.setMessage("判定のため通信を行いますか？")
					.setNegativeButton("いいえ", null)
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							HttpPhotoTransfer hpt = new HttpPhotoTransfer(activity,Cameraman.this);
							hpt.execute("http://133.14.168.53/DojakJamrujyonWeg4.php", iMGNAME);
						}
					}).show();
		} else {
			Toast.makeText(this, "No Network Connection!", Toast.LENGTH_LONG).show();
		}
		return iMGNAME;
	}

	//GPsを使うメソッド
	public String Gpskun() {

		if (gpsStatus.indexOf("gps",0) < 0) {
			Toast.makeText(this,"GPS can not use",Toast.LENGTH_LONG).show();
			return null;
		} else {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAccuracy(Criteria.POWER_LOW);

			provider = mLocationManager.getBestProvider(criteria, true);
			mLocationManager.requestLocationUpdates(provider, 10, 1, this);

			location = mLocationManager.getLastKnownLocation(provider);
			location = mLocationManager.getLastKnownLocation(provider);

			if (location == null) {
				mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (location == null) {
				mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			if (location != null) {
				//桁数の指定
				DecimalFormat df = new DecimalFormat("0.000000");
				//locationから緯度経度の取得
				String lati = df.format(location.getLatitude());
				String longi = df.format(location.getLongitude());

				return lati + "," + longi;
			} else {
				return null;
			}
		}


	}

	private void enableLocationSettings() {
		new AlertDialog.Builder(Cameraman.this)
				.setTitle("位置情報がオンになってません")
				.setMessage("位置情報をオンにしますか？")
				.setNegativeButton("いいえ", null)
				.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(settingsIntent);
					}
				}).show();
	}


	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {

	}

	@Override
	public void onProviderDisabled(String s) {

	}
}

