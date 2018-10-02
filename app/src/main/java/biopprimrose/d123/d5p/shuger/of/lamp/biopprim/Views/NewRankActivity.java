package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class NewRankActivity extends AppCompatActivity implements NewRankFragment.RankItemclicklistener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rank);


        // 渡すデータを準備する
        String[] kurage = { "密度", "アノテーション", "連続撮影", "ほげ", "hoge", "hogehoge" };
        Bundle bundle = new Bundle();
        bundle.putStringArray("kurage", kurage);

        // フラグメントを生成
        NewRankFragment fragment = new NewRankFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        // フラグメントをアクティビティに追加する FragmentTransaction を利用する
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_new_rank, fragment, "fragment");
//        transaction.addToBackStack(null);
        transaction.commit();


    }


    @Override
    public void onRankItemClicked(String selected) {
        Bundle bundle = new Bundle ();
        bundle.putString("selected",selected);
        RankCategory fragment = new RankCategory();
        fragment.setArguments(bundle);
        FragmentManager manager2 = getSupportFragmentManager();
        FragmentTransaction transaction = manager2.beginTransaction();
        transaction.replace(R.id.container_new_rank, fragment, "fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_rank);
//    }
}
