package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendListAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendListViewAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RecycleAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.DataController;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.DividerItemDecoration;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendDatabase;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendRequestTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendResponse;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.OnRecycleListener;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class FriendActivity extends AppCompatActivity implements OnRecycleListener {

    RecyclerView recyclerView;
    String friend_data;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_friend);
//        recyclerView = (RecyclerView) findViewById(R.id.friendactivity_recyclerview);
        listView = (ListView) findViewById(R.id.friend_listview);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(FriendActivity.this);
        String userid = "951737";

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FriendDatabase fd = new FriendDatabase(this);
        List<FriendList> lists = fd.Read_db();

        DividerItemDecoration di = new DividerItemDecoration(this);

//        FriendListAdapter adapter = new FriendListAdapter(this, lists, this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(di);

        FriendListViewAdapter adapter = new FriendListViewAdapter(this);
        adapter.setFriendLists(lists);
        listView.setAdapter(adapter);

        FriendRequestTask task = new FriendRequestTask(this, userid);
        task.execute("1");

        List<FriendList> my_reqs_list = fd.Read_db_requested();
        FriendRequestTask task_1 = new FriendRequestTask(this, userid, lists);
        task_1.execute("4");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friend);
        ((TextView) findViewById(R.id.friend_toolbar_text)).setText(R.string.friend_toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_friend:
                FriendRequestFragment fragment = new FriendRequestFragment();
                FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
                manager.add(R.id.container, fragment, "fragment_activity");
                manager.addToBackStack("friend_");
                manager.commit();
                Log.v("menu", "menu_clicked");
                EventBus.getDefault().unregister(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public void isRequestLoaded(FriendResponse friendResponse) {
        Log.v("friend_load", friendResponse.getNormal_response());
        FriendDatabase fd = new FriendDatabase(this);

        if (friendResponse.getNormal_response().split(",")[0].replace("\n", "").equals("normaladdfriend")) {
            String[] res = friendResponse.getNormal_response().split(",");
            fd.dbupdate(Integer.valueOf(res[1]), friendResponse);

            Toast.makeText(FriendActivity.this, "Friend add", Toast.LENGTH_SHORT).show();
            Log.v("aaaa", "add_friend");

            List<FriendList> lists = fd.Read_db();
//            FriendListAdapter adapter = new FriendListAdapter(this, lists, this);
//            recyclerView.setAdapter(adapter);
            FriendListViewAdapter adapter = new FriendListViewAdapter(this);
            adapter.setFriendLists(lists);
            listView.setAdapter(adapter);

        } else if (friendResponse.getNormal_response().split(",")[0].replace("\n", "").equals("normalnotexisted")) {
            Log.v("friendactivity","isnotfriendrequest");

        } else if  (friendResponse.getJob_id() != null) {
            Log.v("friendactivity","autoaccepted"+friendResponse.getJob_id());
            fd.dbch(friendResponse);
            List<FriendList> lists = fd.Read_db();
            FriendListViewAdapter adapter = new FriendListViewAdapter(this);
            adapter.setFriendLists(lists);
            listView.setAdapter(adapter);
//                FriendListAdapter adapter = new FriendListAdapter(this, lists, this);
//                recyclerView.setAdapter(adapter);

        }
    }


    @Override
    public void onRecycleClicked(View v, final int Position) {

        FriendDatabase fd = new FriendDatabase(this);
        final FriendList fl = fd.Read_db_num(Position);

        AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
        String title_text = getResources().getString(R.string.friend_accept_title);
        String message_text = getResources().getString(R.string.friend_accept_message);
        final String userid = "117122";
        Log.v("isfriend_friend_activty", fl.getIsFrined());
        if (fl.getIsFrined().equals("0")) {
            builder.setTitle(title_text)
                    .setMessage(message_text)
                    .setNegativeButton(R.string.no_dialog, null)
                    .setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //ontext context, String userid1, String userid2, String jobid, int position
                            FriendRequestTask task =
                                    new FriendRequestTask(FriendActivity.this, userid, String.valueOf(fl.getUserid()), fl.getJob_id(), Position);
                            task.execute("2");
                            Log.v("friend_request", Position + "");
                        }
                    }).show();

        } else if (fl.getIsFrined().equals("1")) {

            FriendDetailFragment fragment = new FriendDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", Position);
            fragment.setArguments(bundle);
            FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
            manager.add(R.id.container, fragment);
            manager.addToBackStack("friend_d");
            manager.commit();
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long position) {
                final int pos_inc = pos + 1;
                Log.v("positions_listview", pos + "," + position);

                FriendDatabase fd = new FriendDatabase(FriendActivity.this);
                final FriendList fl = fd.Read_db_num(pos_inc);

                AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
                String title_text = getResources().getString(R.string.friend_accept_title);
                String message_text = getResources().getString(R.string.friend_accept_message);
                final String userid = "117122";
                Log.v("isfriend_friend_activty", fl.getIsFrined()+fl.getIsFrined().equals("0"));
                if (fl.getIsFrined().equals("0\n")) {
                    builder.setTitle(title_text)
                            .setMessage(message_text)
                            .setNegativeButton(R.string.no_dialog, null)
                            .setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //ontext context, String userid1, String userid2, String jobid, int pos
                                    FriendRequestTask task =
                                            new FriendRequestTask(FriendActivity.this, userid,
                                                    String.valueOf(fl.getUserid()), fl.getJob_id(), pos_inc);
                                    task.execute("2");
                                    Log.v("friend_request", pos_inc + "");
                                }
                            }).show();

                } else if (fl.getIsFrined().equals("1")) {

                    FriendDetailFragment fragment = new FriendDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", pos_inc);
                    fragment.setArguments(bundle);
                    FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
                    manager.add(R.id.container, fragment);
                    manager.addToBackStack("friend_d");
                    manager.commit();
                    EventBus.getDefault().unregister(this);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
