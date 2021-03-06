package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by amimeyaY on 2015/11/05.
 */
public class PhotoMultiTransfer extends AsyncTask<String, Integer, List<String>> {

    private String str;
    Context context;
    private List<String> res_ar;
    ProgressDialog dialog;
    private String pname;
    private String filename;
    private Double bounus;
    String isTimelimit;
    String updated;
    int count;
    private String annotations;
    private String isDeleted;
    ProgressBar progressBar;

    /**
     * add by GentaKunitomo on 2020/09/21
     */
    private String eventid;


    String lati;
    String longi;
    List<Integer> idlist;
    //データベース関連
    SQLiteDatabase db;


    public PhotoMultiTransfer(Context context, List<Integer> idlist, ProgressBar progressBar) {
//        this.context = context;
        super();
        this.idlist = idlist;
        this.context = context;
        this.progressBar = progressBar;

    }

    @Override
    protected List doInBackground(String... params) {

        //呼び出された時の引数
        String userid = params[0];

        String url = UrlCollections.URL_UPLOAD_PHOTO;

        TempOpenHelper toh = new TempOpenHelper(context);
        SQLiteDatabase db = toh.getReadableDatabase();

        count = 0;
        Log.v("idlist", String.valueOf(idlist));

        Cursor c = null;
        for (int id : idlist) {
//
//            count += 1;
//            Log.v("upload", String.valueOf(count) + "枚目");
//
//            dialog.setProgress(count);

            c = db.query(
                    TempContract.TempImages.TABLE_NAME,
                    null,
                    ImgContract.Images._ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null
            );

            while (c.moveToNext()) {
                filename = c.getString(c.getColumnIndex(ImgContract.Images.COLUMN_FILE_NAME));
                lati = c.getString(c.getColumnIndex(ImgContract.Images.COL_LAT));
                longi = c.getString(c.getColumnIndex(ImgContract.Images.COL_LNG));
                updated = c.getString(c.getColumnIndex(ImgContract.Images.COL_UPDATED));
                pname = c.getString(c.getColumnIndex(ImgContract.Images.COL_PNAME));

                eventid = c.getString(c.getColumnIndex(ImgContract.Images.COL_EVENT_ID));

                annotations = c.getString(c.getColumnIndex(ImgContract.Images.COL_ANNOTATION));
                isDeleted = c.getString(c.getColumnIndex(ImgContract.Images.COL_ISDELETED));

            }
              Log.d("PhotoMultiTransferlog",eventid);
//            Log.v("watcher",annotations);


            if (isDeleted.equals("0")) {
                count += 1;
                Log.v("upload", String.valueOf(count) + "枚目");

//                dialog.setProgress(count);


                //インスタンスの作成
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();

                entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                //ファイルパスをファイル型として取得？
                File file = new File(filename);
                String[] dist = filename.split("/");
                String fname = dist[dist.length - 1];
                Log.v("filename", fname);

                String ano = "なし";
                if (annotations != null) {
                    ano = annotations;
                }

                Log.v("pmt-ano", ano + annotations);
                final MediaType IMAGE = MediaType.parse("image/img");
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                                "filename", fname
                        )
                        .addFormDataPart(
                                "photoname", pname
                        )
                        .addFormDataPart(
                                "userid", userid
                        )
                        .addFormDataPart("images", fname,
                                RequestBody.create(IMAGE, file)
                        )
                        .addFormDataPart(
                                "eventid",eventid
                        )
                        .addFormDataPart(
//                            "annotation", annotations
                                "annotation", ano
                        )
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                        .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                        .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                        .build();


                try {


                    Response res = client.newCall(request).execute();
                    str = res.body().string();

                    dbch(context, filename, bounus, str);
                    dbupdate(context, String.valueOf(id));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                res_ar.add(str);
            }
        }

        if (c != null) {
            c.close();
            db.close();
        }


        return res_ar;
    }

    @Override
    protected void onPreExecute() {
        res_ar = new ArrayList<>();

//        dialog = new ProgressDialog(context);
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setTitle("アップロード中");
////        dialog.setMax(idlist.size());
////        dialog.setMax(count);
//        dialog.show();

//        progressBar = new ProgressBar(context);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar

    }

    @Override
    protected void onCancelled() {
//        dialog.dismiss();
    }


    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    @Override
    protected void onPostExecute(List<String> res_ar) {
        // サーバ側phpでechoした内容を表
        Log.v("res", String.valueOf(res_ar));

        String message_1 = context.getResources().getString(R.string.allupload_label_numberofuploadedphoto);
        String message_2 = context.getResources().getString(R.string.toast_cant_upload);
        String message_3 = context.getResources().getString(R.string.toast_network_not_work);


        if (res_ar != null && res_ar.size() != 0) {
            if (res_ar.size() == count) {

                Toast.makeText(
                        context,
                        count + message_1
                        , Toast.LENGTH_LONG
                ).show();
            } else {
                int resu = res_ar.size() - count;
                Toast.makeText(
                        context,
                        count + message_1 + "\n" + resu + message_2,
                        Toast.LENGTH_LONG
                ).show();
            }
        } else {
            Toast.makeText(
                    context,
//                    "ネットワークの調子が悪いかもしれません。",
                    R.string.upload_queue_null,
                    Toast.LENGTH_LONG
            ).show();
        }
//        dialog.dismiss();
        progressBar.setVisibility(View.GONE);     // To Hide ProgressBar


        /**
         PhotoUploadEvent pue = new PhotoUploadEvent();
         pue.setEnded("end");

         EventBus.getDefault().post(pue);
         */

    }

    public void dbch(Context c, String filename, Double bounus, String strs) {
        //データベースに帰ってきたデータをぶち込む
        if (strs != null) {
            ImgOpenHelper imgOpenHelper = new ImgOpenHelper(c);
            db = imgOpenHelper.getWritableDatabase();

            //List<Address> list_address = null;
            Double lat = Double.parseDouble(lati);
            Double lng = Double.parseDouble(longi);

            String updated =
                    new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                            .format(new Date());
            String fname = filename;
            String ano = annotations;

            ContentValues values = new ContentValues();
            values.put(ImgContract.Images.COL_LAT, lat);
            values.put(ImgContract.Images.COL_LNG, lng);
            values.put(ImgContract.Images.COL_SCORE, strs);
            values.put(ImgContract.Images.COL_UPDATED, updated);
            values.put(ImgContract.Images.COLUMN_FILE_NAME, fname);
            values.put(ImgContract.Images.COL_PNAME, pname);
            values.put(ImgContract.Images.COL_VERSION, "new");
            values.put(ImgContract.Images.COL_ISDELETED, "0");
            values.put(ImgContract.Images.COL_ANNOTATION, ano);
            values.put(ImgContract.Images.COL_EVENT_ID,eventid);
            Log.v("dbch_anotation",ano);


            db.insert(
                    ImgContract.Images.TABLE_NAME,
                    null,
                    values
            );
            db.close();
            imgOpenHelper.close();
        }
    }

    public void dbupdate(Context context, String id) {

        TempOpenHelper toh = new TempOpenHelper(context);

        ContentValues values = new ContentValues();
        values.put(TempContract.TempImages.COL_ISUPLOADED, "1");
        db = toh.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(
                    TempContract.TempImages.TABLE_NAME,
                    values,
                    ImgContract.Images._ID + "=" + id,
                    null
            );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        toh.close();
    }
}
