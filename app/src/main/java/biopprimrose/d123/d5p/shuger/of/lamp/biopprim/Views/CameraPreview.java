package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

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

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

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

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PhotoPostTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

/**
 * Changed by amemiya 24/12/2016
 * カメラで撮影を行い、位置情報のExifに書き込み
 * 本体に保存し、データベースに書き込む
 */


public class CameraPreview extends FragmentActivity implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public Context context;

    /**
     * カメラのハードウェアを操作する {@link Camera}
     */

    private Camera mCamera;

    /**
     * カメラのプレビューを表示する {@link SurfaceView}
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
    //
    private boolean auto_focus = false;

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

    RelativeLayout myRelativeLayout;

    /**
     * ************************************************************************************************
     */

    //onCreate...Activityが実行された時に実行される
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_acitivity);

        activity = this;
        mView = (SurfaceView) findViewById(R.id.suface_id);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(500);

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
        gpsStatus = Settings.Secure
                .getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        //シャッターのボタン
        //GPSがONであれば
        Button btn = (Button) findViewById(R.id.shutter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsStatus = Settings.Secure
                        .getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                Log.v("gpsstatus", gpsStatus);
                if (!(gpsStatus.indexOf("gps", 0) < 0) && !flag) {
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

        myRelativeLayout = (RelativeLayout) findViewById(R.id.my_relative);

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

            int cameraid = 0;
            mCamera = Camera.open(cameraid);

            setCameraPreviewOrientation(cameraid);
            /**
             * ズームの設定
             */

            Camera.Parameters prm = mCamera.getParameters();
            int nowZoom = prm.getZoom();
            int maxZoom = prm.getMaxZoom();

            SeekBar seekBar = (SeekBar) findViewById(R.id.zoom_seek);
            seekBar.setMax(maxZoom);

            seekBar.setProgress(nowZoom);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    Camera.Parameters parameters = mCamera.getParameters();
                    if (parameters.isZoomSupported()) {
                        parameters.setZoom(i);
                        mCamera.setParameters(parameters);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

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

            FrameLayout fl = (FrameLayout) findViewById(R.id.cameraPreview);
            overlayContent oc = new overlayContent(getApplicationContext(), fl.getWidth(), fl.getHeight());
            //RelativeLayoutに加える
            myRelativeLayout.addView(oc);
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

                if (lati != null || longi != null) {
                    loc = lati + "," + longi;
                }
                //loc = Gpskun();
                if (loc == null) {
                    Toast.makeText(CameraPreview.this, R.string.cant_get_location, Toast.LENGTH_LONG).show();
                } else {
                    //このアプリ専用のフォルダを使用
                    FileOutputStream myFOS = null;
                    File file = new File("/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/cmr/");

                    //フォルダが存在しなかった場合にフォルダを作成
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //オリジナルの名前の生成
                    String imgName = "/data/data/biopprimrose.d123.d5p.shuger.of.lamp" + "/cmr/" + System.currentTimeMillis() + ".jpg";


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
                    Log.v("positions", lat + "<" + logi);

                    try {
                        ExifInterface ex = new ExifInterface(imgName);
                        ex.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
                        ex.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, logi);
                        ex.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                        ex.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                        ex.saveAttributes();


                        Log.v("location", lat + "," + logi);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    yesornodialog(imgName, loc, sp.getString("MyID", "tom"));

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
                if (!auto_focus) {
                    mCamera.autoFocus(autoFocusCallback);
                    auto_focus = true;
                }
            }
        }
        return true;
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            auto_focus = false;
        }
    };


    //アップロードするかどうかのダイアログを表示する
    public String yesornodialog(final String iMGNAME, String locat, final String userID) {
        NetworkInfo nwi = cm.getActiveNetworkInfo();
        final String loc_data[] = locat.split(",", 0);
        final EditText editView = new EditText(CameraPreview.this);
        editView.setHint(R.string.if_not_input_unknown);
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(50);
        editView.setFilters(inputFilters);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CameraPreview.this);

        //ネットワークがオンあればアップロードオフなら別のデータベースに保存
        if (nwi != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CameraPreview.this);
            builder.setTitle(R.string.detect_img);
            builder.setNegativeButton(R.string.no_dialog, null);
//            builder.setView(editView);

            /**
             * オプションでてキストボックスの表示を許可していれば
             * テキストボックスを表示
             */
            /**
             if (sp.getBoolean("pref_dialog_object_name",true)) {



             builder.setMessage(R.string.do_you_upload);
             builder.setView(editView);
             builder.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
            String pname = "";
            pname = editView.getText().toString();
            if (pname.equals("")) {
            pname = "unknown";
            }
            PhotoPostTask hpt = new PhotoPostTask(activity, loc_data[0], loc_data[1], pname);
            hpt.execute(UrlCollections.URL_UPLOAD_PHOTO , iMGNAME, userID);
            }
            });
             } else {
             builder.setMessage(R.string.do_uploading);
             builder.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
            String pname = "unknown";
            PhotoPostTask hpt = new PhotoPostTask(activity, loc_data[0], loc_data[1], pname);
            hpt.execute(UrlCollections.URL_UPLOAD_PHOTO, iMGNAME, userID);
            }
            });
             }

             builder.show();

             */


            Bundle bundle = new Bundle();
            bundle.putString("img_path",iMGNAME);
            bundle.putString("last_latitude",loc_data[0]);
            bundle.putString("last_longitude",loc_data[1]);
            bundle.putString("userid",userID);
            PhotoPreviewFragment fragment = new PhotoPreviewFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container,fragment);
            transaction.addToBackStack("camera");

            transaction.commit();


        } else {
            //Toast.makeText(this, "ネットワークが利用できません", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(CameraPreview.this)
                    .setTitle(R.string.cant_get_network)
                    .setMessage(R.string.reserve_photo)
                    .setView(editView)
                    .setNegativeButton(R.string.no_dialog, null)
                    .setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pname = "";
                            pname = editView.getText().toString();
                            if (pname.equals("")) {
                                pname = "unknown";
                            }
                            PhotoPostTask hpt = new PhotoPostTask(activity, loc_data[0], loc_data[1], pname);
                            hpt.nouploaddb(iMGNAME);
                            Toast.makeText(CameraPreview.this, R.string.reserved, Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
        return iMGNAME;
    }

    //GPsを使うメソッド
    private void enableLocationSettings() {
        new AlertDialog.Builder(CameraPreview.this)
                .setTitle(R.string.not_on_gps)
                .setMessage(R.string.turn_on_gps)
                .setNegativeButton(R.string.no_dialog, null)
                .setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
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
        gpsStatus = Settings.Secure
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

    public void setCameraPreviewOrientation(int cameraid) {

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraid, cameraInfo);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (cameraInfo.facing == cameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(result);
    }

}

