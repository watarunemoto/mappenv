package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PhotoMultiTransfer;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankingGeter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;


/**
 * Created by amimeyaY on 2015/04/21.
 */

/**
 * ActivityとはAndroidアプリの画面
 */
public class TitleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_title);

        /**
         * navigation drawerとtoolbarの取得と設定
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText(R.string.title_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * gridview上のImageButtonの遷移先
         */

        ImageButton camera = (ImageButton) findViewById(R.id.camera_button);
        ImageButton list = (ImageButton) findViewById(R.id.list_button);
        ImageButton map = (ImageButton) findViewById(R.id.map_button);
        ImageButton rank = (ImageButton) findViewById(R.id.rank_button);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TitleActivity.this, CameraPreview.class);
                startActivity(intent);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TitleActivity.this, TabListview.class);
                startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TitleActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TitleActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * navigation drawerからアイテムの取得
         */
        ImgOpenHelper imgOpenHelper = new ImgOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = imgOpenHelper.getReadableDatabase();
        int deta_num = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, ImgContract.Images.TABLE_NAME);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        String nums = getResources().getString(R.string.photo_num);

        menuItem.setTitle(nums + String.valueOf(deta_num));

        sqLiteDatabase.close();

        /**
         * navgation drawerのヘッダーにユーザー名を挿入
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TitleActivity.this);


        String ids = getResources().getString(R.string.id_dialog);
        ((TextView) findViewById(R.id.nav_header_text)).setText(ids + sp.getString("MyID", "null"));

        Date date = new Date();
        String nowtime;
        final DateFormat df = new SimpleDateFormat("MM/dd/HH");
        nowtime = "About " + df.format(date);

        if (!(sp.getString("ctime", "null")).equals(nowtime) || sp.getString("rank_list", "null").equals("")) {
            RankingGeter rg = new RankingGeter(TitleActivity.this, sp);
            rg.execute(UrlCollections.URL_RANKINGS);
            Log.v("getrank", "ok");
        } else {
            Log.v("getrank", "no");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            Intent intent = new Intent(TitleActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(TitleActivity.this, Setting_Activity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_photonum) {

        } else if (id == R.id.nav_point) {

        } else if (id == R.id.nav_camera) {
            Intent intent = new Intent(TitleActivity.this, CameraPreview.class);
            startActivity(intent);
        } else if (id == R.id.nav_list) {
            Intent intent = new Intent(TitleActivity.this, TabListview.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(TitleActivity.this, MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_rank) {
            Intent intent = new Intent(TitleActivity.this, TabRanking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_upload) {
            upload_dialog();
        } else if (id == R.id.nav_reset_upload_state) {
            dbupdate();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public List<Integer> getNotUploadedid() {

        Log.v("gets", "1");


        List<Integer> idlist = new ArrayList<>();

        TempOpenHelper toh = new TempOpenHelper(TitleActivity.this);
        SQLiteDatabase db = toh.getReadableDatabase();

        Cursor c = db.query(
                TempContract.TempImages.TABLE_NAME,
                null,
                TempContract.TempImages.COL_ISUPLOADED + " = " + "0",
                null,
                null,
                null,
                null,
                null
        );
        Log.v("gets", "2");


        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ImgContract.Images._ID));
            idlist.add(Integer.valueOf(id));
        }

        Log.v("gets", "3");

        c.close();
        db.close();

        return idlist;
    }

    public void upload_dialog() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("複数の写真のアップロードを行います。")
                    .setNegativeButton("いいえ", null)
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    List<Integer> idlist = getNotUploadedid();

                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TitleActivity.this);
                                    String username = sp.getString("username", "");

                                    Log.v("idlist184", String.valueOf(idlist));

                                    PhotoMultiTransfer pmt = new PhotoMultiTransfer(
                                            TitleActivity.this,
                                            idlist
                                    );
                                    pmt.execute(
                                            username
                                    );
                                }
                            }
                    ).show();
        } else {
            Toast.makeText(
                    this,
                    "ネットワークに接続していません",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

    public void dbupdate() {


        Log.v("gets", "1");


        List<Integer> idlist = new ArrayList<>();

        TempOpenHelper toh_r = new TempOpenHelper(TitleActivity.this);
        SQLiteDatabase db_r = toh_r.getReadableDatabase();

        Cursor c = db_r.query(
                TempContract.TempImages.TABLE_NAME,
                null,
                TempContract.TempImages.COL_ISUPLOADED + " = " + "1",
                null,
                null,
                null,
                null,
                null
        );
        Log.v("gets", "2");


        while (c.moveToNext()) {
            String ids = c.getString(c.getColumnIndex(ImgContract.Images._ID));
            idlist.add(Integer.valueOf(ids));
        }

        Log.v("gets", "3");

        c.close();
        db_r.close();



        TempOpenHelper toh = new TempOpenHelper(this);
        SQLiteDatabase db = toh.getWritableDatabase();

        for (int id: idlist) {

            ContentValues values = new ContentValues();
            values.put(TempContract.TempImages.COL_ISUPLOADED, "0");

            Log.v("temp","tmpdb:" + String.valueOf(id));
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
        }




        db.close();
        toh.close();
    }

}