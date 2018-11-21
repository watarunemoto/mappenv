package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.google.android.gms.location.LocationResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.MyLocationManager;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PhotoPostTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Changed by amemiya 24/12/2016
 * カメラで撮影を行い、位置情報のExifに書き込み
 * 本体に保存し、データベースに書き込む
 */


public class CameraPreview extends AppCompatActivity implements MyLocationManager.OnLocationResultListener{

    private Toast t;
    public Context context;

    /**
     * カメラのハードウェアを操作する {@link Camera}
     */

    private Camera mCamera;
    private static final int REQUEST_CODE_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private final int REQUEST_PERMISSION = 1000;


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
//    String gpsStatus;

    Activity activity;

    //画面連打すると落ちるのを防ぐ
    private boolean take_cut = false;
    //
    private boolean auto_focus = false;

    private MyLocationManager locationManager;


    private String lati;
    private String longi;


    static String annotation = "";
    private String anoret = "";

    RelativeLayout myRelativeLayout;

    static final int RESULT_SUBACTIVITY = 1000;
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

        //ネットワークの状態の取得関連
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //GPSの状態の取得

        //シャッターのボタン
        //GPSがONであれば
        Button btn = (Button) findViewById(R.id.shutter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gpsStatus = Settings.Secure
//                        .getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//                Log.v("gpsstatus", gpsStatus);

                if (!take_cut) {
                    if (mCamera != null) {
                        take_cut = true;
                        mCamera.takePicture(null, null, mPictureListener);
                    }
                }
            }
        });



        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }
        else{
            SurfaceHolder holder = mView.getHolder();
            holder.addCallback(surfaceHolderCallback);
        }

        myRelativeLayout = (RelativeLayout) findViewById(R.id.my_relative);

        final CameraMapFragment CMF = new CameraMapFragment();
        Button mapfragmentbutton = (Button) findViewById(R.id.CameraMapbutton);
        mapfragmentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.container,CMF);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        Button anobutton = (Button) findViewById(R.id.anobutton);
        anobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(CameraPreview.this, AnnotationActivity.class);
//                startActivity(intent2);


                intent2.putExtra("Annottion",annotation);
                Log.v("ano1",annotation);


                startActivityForResult(intent2, RESULT_SUBACTIVITY);
            }
        });

    }

    public void checkPermission() {
        // 既に許可している
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){


            SurfaceHolder holder = mView.getHolder();
            holder.addCallback(surfaceHolderCallback);
        }
        // 拒否していた場合
        else{
            requestCameraPermission();
        }
    }
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(CameraPreview.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this,
                    R.string.label_permissionconfirmation, Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,},
                    REQUEST_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                SurfaceHolder holder = mView.getHolder();
//                holder.addCallback(surfaceHolderCallback);
                Toast toast = Toast.makeText(this,
                        R.string.label_rebootconfirmation, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        R.string.label_unavailablenotification, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //onPostCreate...onCreateの実行が終わった時に実行される
        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
//            SurfaceHolder holder = mView.getHolder();

//            holder.addCallback(surfaceHolderCallback);

//            if(Build.VERSION.SDK_INT >= 23){
//                checkPermission();
//            }
//            else{
//            SurfaceHolder holder = mView.getHolder();
//            holder.addCallback(surfaceHolderCallback);
//            }


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
                    Toast.makeText(CameraPreview.this, R.string.camera_err_getlocation, Toast.LENGTH_LONG).show();

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
    public String yesornodialog(final String iMGNAME, String locat, final String userID ) {
        NetworkInfo nwi = cm.getActiveNetworkInfo();
        final String loc_data[] = locat.split(",", 0);
        final EditText editView = new EditText(CameraPreview.this);
        editView.setHint(R.string.hint_photoname);
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(50);
        editView.setFilters(inputFilters);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CameraPreview.this);

        //ネットワークがオンあればアップロードオフなら別のデータベースに保存
        if (nwi != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CameraPreview.this);
            builder.setTitle(R.string.camera_title_detectimg);
            builder.setNegativeButton(R.string.no_dialog, null);


            Bundle bundle = new Bundle();
            bundle.putString("img_path",iMGNAME);
            bundle.putString("last_latitude",loc_data[0]);
            bundle.putString("last_longitude",loc_data[1]);
            bundle.putString("userid",userID);
            bundle.putString("annotation",anoret);
            PhotoPreviewFragment fragment = new PhotoPreviewFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container,fragment);
            transaction.addToBackStack("camera");

            transaction.commit();


        } else {
            //Toast.makeText(this, "ネットワークが利用できません", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(CameraPreview.this)
                    .setTitle(R.string.camera_err_network)
                    .setMessage(R.string.label_reserveconfirmation)
                    .setView(editView)
                    .setNegativeButton(R.string.no_dialog, null)
                    .setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pname = "";
                            Log.v("anohpton",""+ anoret);
                            pname = editView.getText().toString();
                            if (pname.equals("")) {
                                pname = getString(R.string.label_photoname);
                            }
                            if (anoret.equals(null)){
                                anoret = "";
                            }
                            PhotoPostTask hpt = new PhotoPostTask(activity, loc_data[0], loc_data[1], pname, anoret);
                            hpt.nouploaddb(iMGNAME);
                            Log.v("imgpath/" , iMGNAME);
                            Toast.makeText(CameraPreview.this, R.string.label_reservenotification, Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
        Log.v("anoret?:",""+ anoret);
        return iMGNAME;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationManager = new MyLocationManager(this, this);
        locationManager.startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult == null) {
            Log.e("GPSError","# No location data.");
            return;
        }

        // 緯度・経度を取得
        double latitude = locationResult.getLastLocation().getLatitude();
        double longitude = locationResult.getLastLocation().getLongitude();
        DecimalFormat df = new DecimalFormat("0.000000");
        lati = df.format(latitude);
        longi = df.format(longitude);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent2) {
        super.onActivityResult(requestCode, resultCode, intent2);


        if(resultCode == RESULT_OK && requestCode == RESULT_SUBACTIVITY &&
                null != intent2) {
//            String res = intent2.getStringExtra("MESSAGE2");

            anoret  = intent2.getStringExtra("Annotation");
            Log.v("anomain",""+ anoret);
            if (anoret.equals(null)){
                anoret = "";
            }
        }



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

