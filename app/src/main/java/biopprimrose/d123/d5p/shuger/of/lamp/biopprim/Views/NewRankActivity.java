package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankCategoryGetDownloader;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

public class NewRankActivity extends AppCompatActivity implements NewRankFragment.RankItemclicklistener{
    private static final int LOADER_ID = 1;
    private static final String SAVE_INSTANCE_TASK_RESULT = "info.loader.RankCategoryGetDownloader.SAVE_INSTANCE_TASK_RESULT";
    private static final String ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM";
    private String mTaskResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rank);


        // 渡すデータを準備する
//        String[] kurage = { "密度", "アノテーション", "連続撮影", "ほげ", "hoge", "hogehoge" };
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("kurage", kurage);
//        bundle.putStringArray("kurage" , kurage);


        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
        }
        if (mTaskResult != null) {
//            Log.d("onloadfinishRank", mTaskResult);
//            bundle.putStringArray("kurage", mTaskResult.split(",", 0));
//            NewRankFragment fragment = new NewRankFragment();
//            fragment.setArguments(bundle);
//            FragmentManager manager = getSupportFragmentManager();
//            // フラグメントをアクティビティに追加する FragmentTransaction を利用する
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.add(R.id.container_new_rank, fragment, "fragment");
//            transaction.addToBackStack(null);
//            transaction.commit();
//            Log.d("finish transaction",mTaskResult);

        }
        if (mTaskResult == null) {
            Bundle args = new Bundle();
            args.putString(ARG_EXTRA_PARAM, UrlCollections.URL_GET_CATEGORY);
            Log.d("bundle",args.getString(ARG_EXTRA_PARAM));
            getSupportLoaderManager().initLoader(LOADER_ID, args, mCallback);
        }


        // フラグメントを生成

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INSTANCE_TASK_RESULT, mTaskResult);
    }

    private final LoaderManager.LoaderCallbacks<String> mCallback = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            String extraParam = args.getString(ARG_EXTRA_PARAM);
//            ProgressDialog prog;
//            prog = new ProgressDialog(NewRankActivity.this);
//            prog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            prog.show();
            return new RankCategoryGetDownloader(NewRankActivity.this, extraParam);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            getSupportLoaderManager().destroyLoader(loader.getId());
            // 結果は data に出てくる
            mTaskResult = data.replaceAll("\\[|\\]|\"","");;
//            mTaskResult = mTaskResult.replaceAll("cont","");

            Bundle bundle = new Bundle();
            Log.d("onloadfinishRank",mTaskResult);
            bundle.putStringArray("kurage" , mTaskResult.split(",",0));
            NewRankFragment fragment = new NewRankFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            // フラグメントをアクティビティに追加する FragmentTransaction を利用する
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container_new_rank, fragment, "fragment");
//        transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            Log.d("finish transaction",mTaskResult);


        }


        @Override
        public void onLoaderReset(Loader<String> loader) {
            // do nothing
        }
    };

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
        transaction.commitAllowingStateLoss();
//        transaction.commit();
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_rank);
//    }
}
