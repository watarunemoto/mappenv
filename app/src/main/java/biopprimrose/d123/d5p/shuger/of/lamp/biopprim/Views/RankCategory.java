package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RankRecycleViewAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RankRecyclerRow;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankCategoryPostDownloader;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankCategory extends Fragment {
    private static final int LOADER_ID = 1;
    private static final String SAVE_INSTANCE_TASK_RESULT = "info.loader.RankCategoryGetDownloader.SAVE_INSTANCE_TASK_RESULT";
    private static final String ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM";

    private String mTaskResult;
    private List<HashMap<String,String>> mTaskResultList;

    private Activity mActivity = null;
    private View mView;
    private RecyclerFragmentListener mFragmentListener = null;
    private RankRecycleViewAdapter madapter;
    private ProgressDialog prog;

    public interface RecyclerFragmentListener {
        void onRecyclerEvent();
    }

    public RankCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_rank_category, container, false);
//        View view = inflater.inflate(R.layout.fragment_rank_category, container, false);
        View view = inflater.inflate(R.layout.fragment_rank_category, container, false);

        String kurage;

        TextView textview = (TextView) view.findViewById(R.id.categorybutton);
        if (getArguments() != null && getArguments().containsKey("selected")) {
            kurage = getArguments().getString("selected");
//            textview.setText("情報を取得中です...");
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String username = sp.getString("MyID", "");
//        RecyclerView rv = inflater.inflate(R.id., container, false);
//        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rankcategoryrecyclerView);


        if (savedInstanceState != null) {
            mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
        }

        if (mTaskResult != null) {
//            textview.setText(mTaskResult);
        }
        if (mTaskResult == null) {
            Bundle args = new Bundle();
            args.putString("URI",UrlCollections.URL_GET_CATEGORYRANKING);
            args.putString("QueryKey" , "category");
            args.putString("QueryValue",getArguments().getString("selected"));


            RecyclerView rv = (RecyclerView) view.findViewById(R.id.rankcategoryrecyclerView);
//            RankRecycleViewAdapter adapter = new RankRecycleViewAdapter(this.createDataset());
            madapter = new RankRecycleViewAdapter(this.createDataset());
            LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
//            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);

            rv.setAdapter(madapter);
            RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                    llm .getOrientation());
            rv.addItemDecoration(dividerItemDecoration);

            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, args, mCallback);


        }

        return view;
    }

    private List<RankRecyclerRow> createDataset() {

        List<RankRecyclerRow> dataset = new ArrayList<>();

        return dataset;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INSTANCE_TASK_RESULT, mTaskResult);
    }

    private final LoaderManager.LoaderCallbacks<String> mCallback = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
//            String extraParam = args.getString(ARG_EXTRA_PARAM);
            prog = new ProgressDialog(getActivity());
            prog.setMessage(getString(R.string.label_getinfonotification));
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.setCancelable(false);
            prog.show();
            return new RankCategoryPostDownloader(getActivity(), args);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            try {
                prog.dismiss();
                getActivity().getSupportLoaderManager().destroyLoader(loader.getId());
                mTaskResult = data;
                Gson gson = new Gson();
                Type amount = new TypeToken<List<HashMap<String,String>>>(){}.getType();
                List<HashMap<String,String>> posts = gson.fromJson(data, amount);
                mTaskResultList = posts;
                if (mTaskResultList != null && !mTaskResultList.isEmpty()) {
                    madapter.setCardInfoList(mTaskResultList);
                    madapter.notifyDataSetChanged();
                }

//                Log.d("onloadfinishcat",mTaskResultList.toString());
                Log.d("hoge",mTaskResultList.get(1).get("Score"));



//                try{
//                    TextView textview = (TextView) getView().findViewById(R.id.categorybutton);
////                    textview.setText(mTaskResultList.toString());
//                    textview.setText("");
//                }catch(NullPointerException e){
//                    return;
//                }



            }catch (NullPointerException e){
                Log.d("ぬるぽ", data);
            }
        }

        class hashmap {

        }


        @Override
        public void onLoaderReset(Loader<String> loader) {
            // do nothing
        }
    };

}
