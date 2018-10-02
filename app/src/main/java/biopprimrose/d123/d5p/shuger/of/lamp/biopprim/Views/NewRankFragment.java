package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class NewRankFragment extends ListFragment {


    public interface RankItemclicklistener {
        void onRankItemClicked(String selected);
    }

    private RankItemclicklistener mListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("kurage")) {
            String[] kurage = getArguments().getStringArray("kurage");
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.fragment_new_rank,kurage);
            setListAdapter(adapter);
        }



//        RankCategory fragment = new RankCategory();




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof RankItemclicklistener == false) {
            throw new ClassCastException("activity が Listener を実装していません.");

        }
        mListener = ((RankItemclicklistener) activity);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String kura = l.getAdapter().getItem(position).toString();
        Toast.makeText(getActivity(),kura,Toast.LENGTH_SHORT).show();


        if (mListener != null) {
            mListener.onRankItemClicked(kura);
        }


    }




}

