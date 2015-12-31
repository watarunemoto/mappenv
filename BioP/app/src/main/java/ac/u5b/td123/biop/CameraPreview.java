package ac.u5b.td123.biop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class CameraPreview extends Activity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

	/**
	 * ************************************************************************************************
	 */

	//onCreate...Activityが実行された時に実行される
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_acitivity);
		//
		activity = this;
		mView = (SurfaceView) findViewById(R.id.suface_id);
		//Locationmanagerの参照
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//fuselocationapi
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(1000);
		locationRequest.setFastestInterval(500);

		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

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

		//シャッターのボタン
		Button btn = (Button) findViewById(R.id.shutter);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gpsStatus = android.provider.Settings.Secure
						.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				if (gpsStatus.indexOf("gps", 0) >= 0 && !flag) {
				googleApiClient.connect();
					flag = true;
				}

				if (!take_cut) {
					if (mCamera != null) {
						take_cut = true;
						mCamera.takePicture(null, null, mPictureListener);
					}
				}
			}
		});
		/**
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.cameraPreview);
		LinearLayout linearLayout = new LinearLayout(this);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(1000,1000);
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.setMargins(10, 10, 0, 0);
		linearLayout.setLayoutParams(layoutParams);
		linearLayout.setBackgroundColor(Color.BLUE);
		frameLayout.addView(linearLayout);
		 */

	}

	//onPostCreate...onCreateの実行が終わった時に実行される
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		SurfaceHolder holder = mView.getHolder();
		holder.addCallback(surfaceHolderCallback);
		Log.v("post", "postCreate");
	}

	//カメラのコールバック
	private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {

		//surfaceCreated...surfaceが形成されたときに実行
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			/**
			 * カメラ実行
			 */
			mCamera = Camera.open();
			/**
			 * ズームの設定
			 */
			ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomctrl);
			zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Camera.Parameters parameters = mCamera.getParameters();
					if (parameters.isZoomSupported()) {
						int crZoom = parameters.getZoom();
						if (crZoom < parameters.getMaxZoom()) {
							parameters.setZoom(crZoom + 1);
							mCamera.setParameters(parameters);
						}
					}
				}
			});

			zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Camera.Parameters parameters = mCamera.getParameters();
					if (parameters.isZoomSupported()) {
						int crZoom = parameters.getZoom();
						if (crZoom > 0) {
							parameters.setZoom(crZoom - 1);
							mCamera.setParameters(parameters);
						}
					}
				}
			});
			/**
			 * 解像度の設定の追加
			 */
			Camera.Parameters params = mCamera.getParameters();

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
			parameters.setPreviewSize(previewSize.width, previewSize.height);
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
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CameraPreview.this);
			//デバックオンリー
			DecimalFormat df = new DecimalFormat("0.000000");

			if (data != null) {
				//デバックオンリー
				//でんだい周辺ver
				/**
				 double rlat = (Math.random() * 0.001) + 35.985;
				 double rlog = (Math.random() * 0.001) + 139.373;
				 String r = df.format(rlat);
				 String r2 = df.format(rlog);
				 loc = r + "," + r2;
				*/

				
				if (lati != null || longi != null) {
					loc = lati + "," + longi;
				}
				//loc = Gpskun();
				if (loc == null) {
					Toast.makeText(CameraPreview.this, "位置情報が取得できないため\n画像は保存されません", Toast.LENGTH_LONG).show();
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
						ex.saveAttributes();
					} catch (IOException e) {
						e.printStackTrace();
					}
					yesornodialog(imgName,loc,sp.getString("MyID", "tom"));

				}
				camera.startPreview();
			}
		}
	};

	//画面がタッチされた時に写真が撮られる
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mCamera != null) {
				mCamera.autoFocus(autoFocusCallback);
			}
		}
		return true;
	}

	private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean b, Camera camera) {
		}
	};


	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	//アップロードするかどうかのダイアログを表示する
	public String yesornodialog(final String iMGNAME,String locat, final String userID) {
		NetworkInfo nwi = cm.getActiveNetworkInfo();

		final String loc_data[] = locat.split(",", 0);
		final EditText editView = new EditText(CameraPreview.this);
		editView.setHint("入力しないと[unknown]になります");
		InputFilter[] inputFilters = new InputFilter[1];
		inputFilters[0] = new InputFilter.LengthFilter(50);
		editView.setFilters(inputFilters);

		//ネットワークがオンあればアップロードオフなら別のデータベースに保存
		if (nwi != null) {

			new AlertDialog.Builder(CameraPreview.this)
					.setTitle("画像を判定します")
					.setMessage("通信を行います\n名前を入力してください")
					.setView(editView)
					.setNegativeButton("いいえ", null)
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							String pname ="";
							pname = editView.getText().toString();
							if (pname.equals("")) {
								pname = "unknown";
							}
							HttpPhotoTransfer hpt = new HttpPhotoTransfer(activity, CameraPreview.this, loc_data[0], loc_data[1],pname);
							hpt.execute("http://133.14.168.53/DojakJamrujyonWeg4.php", iMGNAME,userID);
						}
					}).show();
		} else {
			//Toast.makeText(this, "ネットワークが利用できません", Toast.LENGTH_LONG).show();
			new AlertDialog.Builder(CameraPreview.this)
					.setTitle("ネットワークが利用できません")
					.setMessage("保存しておきますか\n名前を入力してください\n後からアップロードできます")
					.setView(editView)
					.setNegativeButton("いいえ", null)
					.setPositiveButton("はい", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							String pname = "";
							pname = editView.getText().toString();
							if (pname.equals("")) {
								pname = "unknown";
							}
							HttpPhotoTransfer hpt = new HttpPhotoTransfer(activity, CameraPreview.this, loc_data[0], loc_data[1], pname);
							hpt.nouploaddb(iMGNAME);
						}
					}).show();
		}
		return iMGNAME;
	}

	//GPsを使うメソッド


	private void enableLocationSettings() {
		new AlertDialog.Builder(CameraPreview.this)
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
		DecimalFormat df = new DecimalFormat("0.000000");
		//locationから緯度経度の取得
		lati = df.format(location.getLatitude());
		longi = df.format(location.getLongitude());
	}

	@Override
	protected void onStart() {
		if (gpsStatus.indexOf("gps", 0) >= 0 && !flag) {
			googleApiClient.connect();
			flag = true;
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		gpsStatus = android.provider.Settings.Secure
				.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Location creentlocation = fusedLocationProviderApi.getLastLocation(googleApiClient);
		fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, CameraPreview.this);
		if (creentlocation != null) {
			location = creentlocation;
			//桁数の指定
			DecimalFormat df = new DecimalFormat("0.000000");
			//locationから緯度経度の取得
			lati = df.format(location.getLatitude());
			longi = df.format(location.getLongitude());
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	}
}

