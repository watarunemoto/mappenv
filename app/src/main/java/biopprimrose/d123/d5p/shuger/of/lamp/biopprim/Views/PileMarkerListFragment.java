package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersListAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PileMarkerListFragment extends Fragment {

    List<OtherUsersList> otherUsersLists;

    public PileMarkerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] packdata = getArguments().getStringArray("snippets");

        View view =  inflater.inflate(R.layout.fragment_pile_marker_list, container, false);


        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        otherUsersLists =  new ArrayList<>();

        int count = 0;
        try{
        for (String data : packdata) {
            OtherUsersList otherUsersList = new OtherUsersList();

            String[] marker_snippets =  data.split(",");

            otherUsersList.setId((long) count);
            otherUsersList.setFilepath(marker_snippets[0]);
            otherUsersList.setUserid(marker_snippets[1]);

            otherUsersLists.add(otherUsersList);
            count++;
        }
        }catch (ArrayIndexOutOfBoundsException e){
        }

        OtherUsersListAdapter otherUsersListAdapter = new OtherUsersListAdapter(getActivity());
        otherUsersListAdapter.setOtherUsersLists(otherUsersLists);
        gridView.setAdapter(otherUsersListAdapter);

        //表示したやつをくりっくしたら詳細画面へ飛ぶ
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String filename_ = otherUsersLists.get((int) l).getFilepath();
                String userid_ = otherUsersLists.get(i).getUserid();
                String[] data = {filename_, userid_};

                PhotoResultFormFragment fragment = new PhotoResultFormFragment();
                Bundle args = new Bundle();
                args.putStringArray("packdata", data);
                fragment.setArguments(args);


                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.add(R.id.map_eval_fragment, fragment);
                transaction.addToBackStack("fragmetn_map_list");
                transaction.commit();

            }
        });

        view.setClickable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                FragmentManager manager = getFragmentManager();
                if (manager != null) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        manager.popBackStack();
                    }
                }
                return true;
            }
        });


        return view;
    }

}
