package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankCategoryPostDownloader;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.Util;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

public class NewRankActivity extends AppCompatActivity implements NewRankFragment.RankItemclicklistener{
    private static final int LOADER_ID = 1;
    private static final String SAVE_INSTANCE_TASK_RESULT = "info.loader.RankCategoryGetDownloader.SAVE_INSTANCE_TASK_RESULT";
    private static final String ARG_URI = "URI";
    private String mTaskResult;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rank);

        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
        }
        if (mTaskResult != null) {
        }
        if (mTaskResult == null) {
            Bundle args = new Bundle();
            args.putString(ARG_URI, UrlCollections.URL_GET_CATEGORY);
            Log.d("bundle",args.getString(ARG_URI));
            if ( Util.netWorkCheck(this.getApplicationContext() )){
                // 非同期通信などの処理をかく

                getSupportLoaderManager().initLoader(LOADER_ID, args, mCallback);
            } else {
                Toast.makeText(this , R.string.camera_err_network, Toast.LENGTH_LONG).show();
                // 繋がらなかったよ…
            }


        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INSTANCE_TASK_RESULT, mTaskResult);
    }

    private final LoaderManager.LoaderCallbacks<String> mCallback = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
//            String extraParam = args.getString(ARG_URI);
            prog = new ProgressDialog(NewRankActivity.this);
            prog.setMessage(getString(R.string.label_getinfonotification));
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.setCancelable(false);
            prog.show();
//            return new RankCategoryGetDownloader(NewRankActivity.this, extraParam);
            return new RankCategoryPostDownloader(NewRankActivity.this, args);

        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            prog.dismiss();
            getSupportLoaderManager().destroyLoader(loader.getId());
            // 結果は data に出てくる
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> posts = gson.fromJson(data, type);
            mTaskResult = posts.toString().replaceAll("\\[|\\]","");
//            mTaskResult = gson.fromJson(mTaskResult,ArrayList.class).;

            Bundle bundle = new Bundle();
            Log.d("onloadfinishRank",mTaskResult);
            bundle.putStringArray("kurage" , mTaskResult.split(","));
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

}
