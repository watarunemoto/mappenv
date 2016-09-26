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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;


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
    String id;

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
        }

        c.close();
        db.close();

        //インスタンスの作成
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost hpost = new HttpPost(url);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //ファイルパスをファイル型として取得？
        File file = new File(filename);

        //entityにデータをセット
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String userid = sp.getString("MyID", "null");

        ContentType textContentType = ContentType.create("text/plain", "UTF-8");
        entity.addTextBody("usrIDaafdfwe", userid, textContentType);
        entity.addTextBody("ppersonalname", pname, textContentType);
        entity.addBinaryBody("catb", file, ContentType.create("image/jpg"), filename);

        /**entityにセットする
         * この操作にはapache-mime5j-core,httpcore,httpmimeの３つのラブラリが必要
         */

        hpost.setEntity(entity.build());

        str = null;

        //レスポンスを取得
        HttpResponse response = null;
        try {
            response = httpClient.execute(hpost);
            try {
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
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
        if (str != null && res.length == 7) {
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
