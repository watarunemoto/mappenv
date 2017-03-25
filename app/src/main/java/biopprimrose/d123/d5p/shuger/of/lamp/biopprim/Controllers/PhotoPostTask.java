package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class PhotoPostTask extends AsyncTask<String, Integer, String> {

    private String str;
    private Activity activity;
    ProgressDialog dialog;
    private String pname;
    private String filename;

    String lati;
    String longi;

    //データベース関連
    SQLiteDatabase db;


    public PhotoPostTask( Activity activity, String lati, String longi, String pname) {
        this.activity = activity;
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

        File file = new File(filename);
        final MediaType IMAGE = MediaType.parse("image/jpg");

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
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
                        "filename", filename
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
            str = res.body().string();
            str = str.replace("{","").replace("}","").replace("\\","").replace("\"","");
            dbch(activity, filename);

            //dbch(activity,filename,bounus);
        }catch (Exception e) {
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
            Toast.makeText(activity, message + no1, Toast.LENGTH_LONG).show();
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

            Toast.makeText(activity, R.string.reserved, Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    //アップロードできないやつを別のデータベースに書き込むやつ
    public void nouploaddb(String filename) {

        String score;
        TempOpenHelper tempOpenHelper = new TempOpenHelper(activity);
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