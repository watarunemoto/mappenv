package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankCategoryDownloader;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankCategory extends Fragment {
    private static final int LOADER_ID = 1;
    private static final String SAVE_INSTANCE_TASK_RESULT = "info.loader.RankCategoryDownloader.SAVE_INSTANCE_TASK_RESULT";
    private static final String ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM";

    private String mTaskResult;

    public RankCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_rank_category, container, false);
        View view = inflater.inflate(R.layout.fragment_rank_category, container, false);


        TextView textview = (TextView) view.findViewById(R.id.categorybutton);
        if (getArguments() != null && getArguments().containsKey("selected")) {
            String kurage = getArguments().getString("selected");
            textview.setText(kurage);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String username = sp.getString("MyID", "");


        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
        }
        if (mTaskResult == null) {
            Bundle args = new Bundle();
            args.putString(ARG_EXTRA_PARAM, UrlCollections.URL_GET_CATEGORY);
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, args, mCallback);
        }
        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
        }
        if (mTaskResult != null) {
            textview.setText(mTaskResult);
        }

        return view;
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
            return new RankCategoryDownloader(getActivity(), extraParam);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            try {
                getActivity().getSupportLoaderManager().destroyLoader(loader.getId());
                // 結果は data に出てくる
                mTaskResult = data;
//            try{
                TextView textview = (TextView) getView().findViewById(R.id.categorybutton);
                textview.setText(mTaskResult);
//            }catch(NullPointerException e){
//                return;
//            }

            }catch (NullPointerException e){
                Log.d("ぬるぽ", data);
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            // do nothing
        }
    };

}
