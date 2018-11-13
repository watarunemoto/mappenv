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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RecycleAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.GetMyLocation;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.LoadDataTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.MyLocation;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PhotoMultiTransfer;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankDonwloader;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.TempOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.OnRecycleListener;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;


/**
 * Commented by amimeyaY on 2016/12
 * カメラ、リスト、マップ、ランキングに遷移できる画面
 * navigation drawerからも遷移可となっている
 *
 */

public class TitleActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnRecycleListener
{

    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private List<OtherUsersList> otherUsersLists = new ArrayList<>();
    private RecycleAdapter recycleAdapter;
    private TextView title_dummy_textview;
    private LatLng latLng;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_title);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        /**
         * navigation drawerとtoolbarの取得と設定
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText(R.string.title_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
        );

//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        title_dummy_textview = (TextView) findViewById(R.id.title_dummy_text);

        /**
         * ImageButtonの読み込みと遷移先の設定
         */

        ImageButton camera = (ImageButton) findViewById(R.id.camera_button);
        ImageButton list = (ImageButton) findViewById(R.id.list_button);
        ImageButton map = (ImageButton) findViewById(R.id.map_button);
        ImageButton rank = (ImageButton) findViewById(R.id.rank_button);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


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
//                Intent intent = new Intent(TitleActivity.this, RankActivity.class);
                Intent intent = new Intent(TitleActivity.this, NewRankActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 位置情報の読み込み
         * 今ココ[位置情報の読み込み]→位置情報から周辺の画像取得→recycleadapterにセット
         * 非同期処理を多重に起動しないようにしているが、起動してしまう
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TitleActivity.this);
        sp.edit().putBoolean("Recyclerisdonwloadingnow",true).apply();
        if (latLng == null && sp.getBoolean("Recyclerisdonwloadingnow",true)) {
            GetMyLocation getMyLocation = new GetMyLocation(this);
            getMyLocation.getMylocation();
            sp.edit().putBoolean("Recyclerisdonwloadingnow",false).apply();
            Log.v("titleactivity",sp.getBoolean("Recyclerisdonwloadingnow",false)+"");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * データベースからデータ数を取得
         *
         */
        ImgOpenHelper imgOpenHelper = new ImgOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = imgOpenHelper.getReadableDatabase();
        int deta_num = (int) DatabaseUtils.queryNumEntries(
                sqLiteDatabase, ImgContract.Images.TABLE_NAME);

        /**
         * res/values-ja/strings.xmから文字列取得
         */
        String nums = getResources().getString(R.string.photo_num);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        menuItem.setTitle(nums + String.valueOf(deta_num));

        sqLiteDatabase.close();

        /**
         * navgation drawerのヘッダーにユーザー名を挿入
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TitleActivity.this);
        String ids = getResources().getString(R.string.id_dialog);
        Log.d("Titleid",sp.getString("MyID","unknonw"));
//        ((TextView) findViewById(R.id.nav_header_text)).setText(ids + sp.getString("MyID", "unknown"));
//        TextView drawertitle = findViewById(R.id.nav_header_text);
//        drawertitle.setText(ids + sp.getString("MyID","unknown"));


        Date date = new Date();
        String nowtime;
        final DateFormat df = new SimpleDateFormat("MM/dd/HH");
        nowtime = "About " + df.format(date);

        if (!(sp.getString("ctime", "null")).equals(nowtime) ||
                sp.getString("rank_list", "null").equals(""))
        {
            RankDonwloader rg = new RankDonwloader(TitleActivity.this, sp);
            rg.execute(UrlCollections.URL_RANKINGS);
            Log.v("getrank", "ok");
        } else {
            Log.v("getrank", "no");
        }
        recyclerView = (RecyclerView) findViewById(R.id.title_recycle);
        recyclerView.setLayoutManager
                (new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * @param menu
     * @return
     * メニューの読み込みと定義
     */


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

    /**
     * ナビゲーションドロワーメニューの処理の設定
     */

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
//            Intent intent = new Intent(TitleActivity.this, TabRanking.class);
            Intent intent = new Intent(TitleActivity.this, NewRankActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_upload) {
            upload_dialog();

//        } else if (id == R.id.nav_reset_upload_state) {
//            dbupdate();
//            Intent intent = new Intent(TitleActivity.this,FriendActivity.class);
//            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * アップロードしていない、つまり
     * sqliteのTempContract.TempImages.COL_ISUPLOADEが0
     * のデータをリストで取得して返す
     */
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
            String id = c.getString(
                    c.getColumnIndex(
                            ImgContract.Images._ID
                    )
            );
            idlist.add(Integer.valueOf(id));
        }

        Log.v("gets", "3");

        c.close();
        db.close();

        return idlist;
    }

    /**
     * 写真をアップロードするかしないかのダイアログ
     *
     */
    public void upload_dialog() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("複数の写真のアップロードを行います。")
                    .setNegativeButton("いいえ", null)
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    List<Integer> idlist = getNotUploadedid();

                                    SharedPreferences sp =
                                            PreferenceManager.getDefaultSharedPreferences(
                                                    TitleActivity.this
                                            );
                                    String username = sp.getString("MyID", "");

                                    Log.v("idlist184", String.valueOf(idlist));
//                                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    PhotoMultiTransfer pmt = new PhotoMultiTransfer(
                                            TitleActivity.this,
                                            idlist,
                                            progressBar
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
            progressBar.setVisibility(View.GONE);
        }

    }

    /**
     * データベースの再読み込み
     * とアップデート
     */
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


    /**
     * @param otherUsersLists
     * Eventbusからのイベント受け取り
     * LoadDatataskから
     * otherUsersListsに情報が打ち込まれたやつをリスト化
     * それをリサイクルアダプターにセット
     */

    @Subscribe
    public void onListload(List<OtherUsersList> otherUsersLists) {

        Log.v("titleacitivy_",otherUsersLists.size() +"");

        if (title_dummy_textview != null) {
            title_dummy_textview.setText("");
        }
        if (title_dummy_textview != null) {
            title_dummy_textview.setText("");
        }

        this.otherUsersLists = otherUsersLists;
        if (recyclerView.getAdapter() == null) {
            recycleAdapter = new RecycleAdapter(this, otherUsersLists, this);
            recyclerView.setAdapter(recycleAdapter);
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(TitleActivity.this);
        sp.edit().putBoolean("Recyclerisdonwloadingnow",true).apply();

    }

    /**
     *
     * @param myLocation
     * Eventbusのイベント受け取り
     * GetMyLocationから
     */

    @Subscribe
    public void onMylocationload(MyLocation myLocation) {
        latLng =
                new LatLng(myLocation.getMylocation().latitude,
                        myLocation.getMylocation().longitude
                );



        LoadDataTask loadDataTask = new LoadDataTask(this,latLng);
        loadDataTask.execute();
        Log.v("titleactivity","ranran"
                + "," + myLocation.getMylocation().latitude
                + "," + myLocation.getMylocation().longitude
        );
    }



    @Override
    public void onRecycleClicked(View v, int Position) {

        RecycleAdapter recycleAdapter = (RecycleAdapter) recyclerView.getAdapter();
        List<OtherUsersList> list = recycleAdapter.getOtherUsersList();

        Log.v("otheruserslist",list.size() + "," + Position);
        String filename_ = list.get(Position).getFilepath();
        String name_ =  list.get(Position).getName();
        String class_ = "others";
        String userid_ = list.get(Position).getUserid();
        String[] data = {filename_, name_, class_, userid_};

        OtherPhotoEvalFragment fragment = new OtherPhotoEvalFragment();
        Bundle args = new Bundle();
        args.putStringArray("filename_name_class", data);
        fragment.setArguments(args);

        Log.v("filename_name_class",
                String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])
        );

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.container, fragment);
        transaction.addToBackStack("fragment_map_list");
        transaction.commit();
    }
}