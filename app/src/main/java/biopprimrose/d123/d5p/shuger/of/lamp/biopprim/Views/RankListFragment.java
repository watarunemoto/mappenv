package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class RankListFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;

    public RankListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //        return inflater.inflate(R.layout.fragment_rank_list, container, false);
        View view = inflater.inflate(R.layout.fragment_rank_list, container, false);

        recyclerView = view.findViewById(R.id.my_recycler_view);
//        layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager = new GridLayoutManager(container.getContext(),3);
        layoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        if (getArguments() != null && getArguments().containsKey("kurage")) {
            String[] kurage = getArguments().getStringArray("kurage");
//            String[] kurage = {"hoge","fuga"};
//            mAdapter = new RankListRecycleViewAdapter(kurage);
            mAdapter = new RankListRecycleViewAdapter(kurage){
                @Override
                protected void onRankListClicked(@NonNull String onrank) {
                    super.onRankListClicked(onrank);
                    Toast.makeText(getContext(),onrank,Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle ();
                            bundle.putString("selected",onrank);
                            RankCategory fragment = new RankCategory();
                            fragment.setArguments(bundle);
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container_new_rank, fragment, "fragment");
                            transaction.addToBackStack(null);
                            transaction.commitAllowingStateLoss();
//        transaction.commit();
                }
            };
            recyclerView.setAdapter(mAdapter);
            Log.d("Ranklist","ranklist");
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
//                void onFragmentInteraction(String );
    }
}
