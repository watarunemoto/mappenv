package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RankCustomAdapter;


public class RankActivity extends AppCompatActivity {
	ListView listView;
	Activity activity;
	TextView date_rank;

	//ネットワークの状態を取得する
	ConnectivityManager cm;
	SharedPreferences sp;

	public static final String EXTRA_RAID = "RankActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_bar_rank);
		activity = this;

		listView = (ListView) findViewById(R.id.myListView);
		date_rank = (TextView) findViewById(R.id.date_rank_get);
	//	date_rank.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HuiFontP29.ttf"));

		//ネットワークの状態の取得関連
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		sp = PreferenceManager.getDefaultSharedPreferences(RankActivity.this);

		String lists =  sp.getString("rank_list",null);
		String[] data = lists.split(",");
		final List<String> alist = new ArrayList<String>();
		for (int i = 0; i < Math.min(data.length - 1,30); i++) {
			alist.add(data[i]);
			Log.v("list_data",data[i]);
		}

		RankCustomAdapter customAdapter = new RankCustomAdapter(activity, alist);
		listView.setAdapter(customAdapter);
		date_rank.setText(sp.getString("ctime",null));


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String[] row_array = alist.get(i).split(":");
				String rowdata = row_array[1];
                String point = row_array[3];
                String num = row_array[2];


				Log.v("users_id",rowdata);
                /**
				Intent intent = new Intent(getApplicationContext(),RankUsersDetailActivity.class);
				intent.putExtra(EXTRA_RAID,rowdata);
				startActivity(intent);
                 */

				FragmentManager fm = getSupportFragmentManager();
				RankDetailFragment rdf = new RankDetailFragment();

				FragmentTransaction transaction = fm.beginTransaction();

				Bundle bundle = new Bundle();
				bundle.putString("userid", rowdata);
                bundle.putString("point", point);
                bundle.putString("num", num);
//				bundle.putString("num", point);
//                bundle.putString("point", num);

                // フラグメントに渡す値をセット
				rdf.setArguments(bundle);

				Log.v("putbundole", rowdata);
				transaction.replace(R.id.contena_rank, rdf);
                transaction.addToBackStack("aa");
				transaction.commit();

			}
		});


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rank);
		((TextView) findViewById(R.id.rank_toolbar_text)).setText(R.string.rank_toolbar);
		setSupportActionBar(toolbar);
	}
}
