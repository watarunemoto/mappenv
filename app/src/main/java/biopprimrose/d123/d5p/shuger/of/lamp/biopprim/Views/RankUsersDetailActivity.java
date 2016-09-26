package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class RankUsersDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_users_detail);

        Intent intent = getIntent();
        String userid = intent.getStringExtra(RankActivity.EXTRA_RAID);

        FragmentManager fm = getSupportFragmentManager();
        RankDetailFragment rdf = new RankDetailFragment();

        FragmentTransaction transaction = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        // フラグメントに渡す値をセット
        rdf.setArguments(bundle);

        Log.v("putbundole", userid);
        transaction.replace(R.id.contena, rdf);
        transaction.commit();

    }
}
