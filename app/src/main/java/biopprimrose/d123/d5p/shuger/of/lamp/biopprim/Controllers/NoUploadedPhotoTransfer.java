package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by amimeyaY on 2015/12/03.
 */
public class NoUploadedPhotoTransfer extends AsyncTask<String, Integer, String> {


    public Context context;
    private String str;

    private String filename;
    private String lati;
    private String longi;
    private String updated;
    private String pname;
    private String annotation;
    private String imgname;
    String id;

    private Integer responseCode;

    GridView myListview;
    TempOpenHelper imgOpenHelper;

    public NoUploadedPhotoTransfer(Context c, GridView myListview, TempOpenHelper imgOpenHelper) {
        this.context = c;
        this.myListview = myListview;
        this.imgOpenHelper = imgOpenHelper;

    }

    //データベース関連
    SQLiteDatabase db;

    @Override
    protected String doInBackground(String... params) {

        //呼び出された時の引数
        String url = params[0];
        id = params[1];
        annotation = "";

        TempOpenHelper tempOpenHelper = new TempOpenHelper(context);
        SQLiteDatabase db = tempOpenHelper.getReadableDatabase();

        Cursor c = db.query(
                TempContract.TempImages.TABLE_NAME,
                null,
                TempContract.TempImages._ID + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        Log.v("position", id);

        while (c.moveToNext()) {
            filename = c.getString(c.getColumnIndex(TempContract.TempImages.COLUMN_FILE_NAME));
            lati = c.getString(c.getColumnIndex(TempContract.TempImages.COL_LAT));
            longi = c.getString(c.getColumnIndex(TempContract.TempImages.COL_LNG));
            updated = c.getString(c.getColumnIndex(TempContract.TempImages.COL_UPDATED));
            pname = c.getString(c.getColumnIndex(TempContract.TempImages.COL_PNAME));
            annotation = c.getString(c.getColumnIndex(TempContract.TempImages.COL_ANNOTATION));
        }

        c.close();
        db.close();

        //インスタンスの作成
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //ファイルパスをファイル型として取得？
//        imgname = context.getFilesDir().getPath() + filename;
        imgname = filename.replace("/data/data/biopprimrose.d123.d5p.shuger.of.lamp/cmr/","");
        Log.d("dir", context.getFilesDir().getPath());
        Log.d("imgname",imgname);
        File file = new File(filename);
        final MediaType IMAGE = MediaType.parse("image/jpg");
        //entityにデータをセット

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String userid = sp.getString("MyID", "null");


        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "images", "images",
                        RequestBody.create(IMAGE,file)
                )
                .addFormDataPart(
                        "photoname",pname
                )
                .addFormDataPart(
                        "userid", userid
                )
                .addFormDataPart(
                        "imgpath" , filename
                )
                .addFormDataPart(
                        "filename", imgname
                )
                .addFormDataPart(
                        "annotation", annotation
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(15*1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20*1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20*1000, TimeUnit.MILLISECONDS)
                .build();
        try
        {
            Response res = client.newCall(request).execute();
            responseCode = res.code();
            Log.d("responsecode",Integer.toString(responseCode));
            if (!res.isSuccessful()) {
                Log.d("500",res.body().toString());
            }

            if (res.body() !=null) {
                str = res.body().string();
            }

//            str = res.body().string();
        }catch (Exception e) {
            e.printStackTrace();
//            str = null;
        }
        return str;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onCancelled() {
    }

    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.v("sdfas", str);
        String[] res = str.split("@");
//        if (str != null && res.length == 7) {
        if (responseCode == 200 && str != null){
            long ide = dbch(context, filename, updated);
            if (ide < 0) {
                Toast.makeText(context, "データベースの書き込みに失敗しました", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(context, "アップロードに成功しました\n:", Toast.LENGTH_LONG).show();
                TempOpenHelper toh = new TempOpenHelper(context);
                ContentValues values = new ContentValues();
                values.put(TempContract.TempImages.COL_ISUPLOADED, "1");
                db = toh.getWritableDatabase();
                db.beginTransaction();
                try {
                    db.update(
                            TempContract.TempImages.TABLE_NAME,
                            values,
                            TempContract.TempImages._ID + "=" + id,
                            null

                    );
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
            SimpleCursorAdapter adapter = setMyListview(context);
            myListview.setAdapter(adapter);
        } else {
            Toast.makeText(context, "ネットワークを利用できないかエラーが起きています。", Toast.LENGTH_LONG).show();
        }
    }

    public long dbch(Context context, String filename, String updated) {
        //データベースに帰ってきたデータをぶち込む
        ImgOpenHelper imgOpenHelper = new ImgOpenHelper(context);
        SQLiteDatabase db = imgOpenHelper.getWritableDatabase();

        Double lat = Double.parseDouble(lati);
        Double lng = Double.parseDouble(longi);

        String fname = filename;

        ContentValues values = new ContentValues();
        values.put(ImgContract.Images.COL_LAT, lat);
        values.put(ImgContract.Images.COL_LNG, lng);
        values.put(ImgContract.Images.COL_SCORE, str);
        values.put(ImgContract.Images.COL_UPDATED, updated);
        values.put(ImgContract.Images.COLUMN_FILE_NAME, fname);
        values.put(ImgContract.Images.COL_PNAME, pname);
        values.put(ImgContract.Images.COL_VERSION, "new");
        values.put(ImgContract.Images.COL_ANNOTATION, "annotions");
        values.put(ImgContract.Images.COL_ISDELETED, "0");

        long id = db.insert(
                ImgContract.Images.TABLE_NAME,
                null,
                values
        );

        Log.v("db", id + "");
        return id;

    }

    public SimpleCursorAdapter setMyListview(Context context) {
        TempOpenHelper imgOpenHelper = new TempOpenHelper(context);
        SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
        Cursor c = db.query(
                TempContract.TempImages.TABLE_NAME,
                null,
                TempContract.TempImages.COL_ISUPLOADED + " = 0 AND " + TempContract.TempImages.COL_ISDELETED + " = 0",
                null,
                null,
                null,
                TempContract.TempImages.COL_UPDATED + " DESC"
        );

        String[] from = {
                TempContract.TempImages.COLUMN_FILE_NAME
        };

        final int[] to = {
                R.id.icon
        };

        //adapterのインスタンス化
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context,
                R.layout.no_upload_item,
                c,
                from,
                to,
                0
        );
        return adapter;
    }
}
