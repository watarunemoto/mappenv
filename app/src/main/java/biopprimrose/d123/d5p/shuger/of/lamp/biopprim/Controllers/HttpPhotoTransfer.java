package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class HttpPhotoTransfer extends AsyncTask<String, Integer, String> {

    public Context context;
    private String str;
    private Activity activity;
    ProgressDialog dialog;
    private String pname;
    private String filename;

    String lati;
    String longi;

    //データベース関連
    SQLiteDatabase db;


    public HttpPhotoTransfer(Context c, Activity activity, String lati, String longi, String pname) {
        this.activity = activity;
        this.context = c;
        this.lati = lati;
        this.longi = longi;
        this.pname = pname;
    }

    @Override
    protected String doInBackground(String... params) {

        //呼び出された時の引数
        String url = params[0];
        filename = params[1];
        String userid = params[2];

        //インスタンスの作成
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost hpost = new HttpPost(url);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();


        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //ファイルパスをファイル型として取得？
        File file = new File(filename);

        //entityにデータをセット
        ContentType textContentType = ContentType.create("text/plain", "UTF-8");

        entity.addBinaryBody("catb", file, ContentType.create("image/jpg"), filename);
        entity.addTextBody("usrIDaafdfwe", userid, textContentType);
        entity.addTextBody("ppersonalname", pname, textContentType);
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
                str = str.replace("{","").replace("}","");
                Log.v("resu", str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbch(context, filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return str;
    }

    @Override
    protected void onPreExecute() {
        String message = activity.getResources().getString(R.string.now_uploading);

        dialog = new ProgressDialog(activity);
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    protected void onCancelled() {
        dialog.dismiss();
    }

    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    @Override
    protected void onPostExecute(String result) {
        // サーバ側phpでechoした内容を表示

        String message = activity.getResources().getString(R.string.uploaded);

        String no1 =  str.split(",")[0].split(":")[0].replace("'","").replace(" ","").replace("{","").replace("}","");


        if (str != null) {
            Toast.makeText(context, message + no1, Toast.LENGTH_LONG).show();
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.cant_upload)
                    .setMessage(R.string.do_you_reserved)
                    .setNegativeButton(R.string.yes_dialog, null)
                    .setPositiveButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            nouploaddb(filename);
                        }
                    }).show();
            Toast.makeText(context, R.string.reserved, Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    //アップロードできないやつを別のデータベースに書き込むやつ
    public void nouploaddb(String filename) {

        String score;
        TempOpenHelper tempOpenHelper = new TempOpenHelper(context);
        db = tempOpenHelper.getWritableDatabase();

        Double lat = Double.parseDouble(lati);
        Double lng = Double.parseDouble(longi);

        String updated =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                        .format(new Date());
        String fname = filename;

        score = "不明";
        ContentValues values = new ContentValues();
        values.put(TempContract.TempImages.COL_LAT, lat);
        values.put(TempContract.TempImages.COL_LNG, lng);
        values.put(TempContract.TempImages.COL_SCORE, score);
        values.put(TempContract.TempImages.COL_UPDATED, updated);
        values.put(TempContract.TempImages.COLUMN_FILE_NAME, fname);
        values.put(TempContract.TempImages.COL_PNAME, pname);
        values.put(TempContract.TempImages.COL_ISUPLOADED, "0");
        values.put(TempContract.TempImages.COL_ISDELETED, "0");

        db.insert(
                TempContract.TempImages.TABLE_NAME,
                null,
                values
        );
    }

    public void dbch(Context c, String filename) {
        //データベースに帰ってきたデータをぶち込む
        if (str != null) {
            ImgOpenHelper imgOpenHelper = new ImgOpenHelper(c);
            db = imgOpenHelper.getWritableDatabase();

            //List<Address> list_address = null;
            Double lat = Double.parseDouble(lati);
            Double lng = Double.parseDouble(longi);

            String updated =
                    new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                            .format(new Date());
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

            db.insert(
                    ImgContract.Images.TABLE_NAME,
                    null,
                    values
            );
        }
    }
}
