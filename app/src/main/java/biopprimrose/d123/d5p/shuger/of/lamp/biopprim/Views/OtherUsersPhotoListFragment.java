package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersListAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.LoadDataTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/11/04.
 */
public class OtherUsersPhotoListFragment extends Fragment {


    String[] packdata;
    List<OtherUsersList> otherUsersLists = new ArrayList<>();
    OtherUsersListAdapter otherUsersListAdapter;
    LatLngBounds latLngs;

    Button load_button;

    GridView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("fragment", "fragment loaded");

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Bundle args = getArguments();
        if (args != null) {
            packdata = getArguments().getStringArray("packdata");
            if (packdata != null) {
                Log.v("fragment", "args get");
                LatLng southwest = new LatLng(Double.valueOf(packdata[0]), Double.valueOf(packdata[1]));
                LatLng northeast = new LatLng(Double.valueOf(packdata[2]), Double.valueOf(packdata[3]));

                latLngs = new LatLngBounds(southwest, northeast);
                startAsyncLoadImage();
            }
        } else {
            Log.v("fragment", "args not gegt");
            return null;
        }

        otherUsersListAdapter = new OtherUsersListAdapter(getActivity());

        View rootView =
                inflater.inflate(R.layout.fragment_other_users_photo_list, container, false);

        listView = (GridView) rootView.findViewById(R.id.gridView);


        (rootView.findViewById(R.id.map_load_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAsyncLoadImages();
            }
        });


        load_button = (Button) getActivity().findViewById(R.id.map_load_button);
        load_button.setEnabled(false);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long size = otherUsersListAdapter.getAdapterSize();

                Log.v("onclick", i + "<" + l + "," + size + otherUsersLists.get((int) l) + otherUsersLists.get((int) l));

                String filename_ = otherUsersLists.get((int) l).getFilepath();
                String name_ = otherUsersLists.get((int) l).getName();
                String class_ = "others";
                String userid_ = otherUsersLists.get(i).getUserid();
                String[] data = {filename_, name_, class_, userid_};

                OtherPhotoEvalFragment fragment = new OtherPhotoEvalFragment();
                Bundle args = new Bundle();
                args.putStringArray("filename_name_class", data);
                fragment.setArguments(args);

                Log.v("filename_name_class", String.valueOf(data[0]) + "," + String.valueOf(data[1]) + "," + String.valueOf(data[2]));

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.add(R.id.map_eval_fragment, fragment);
                transaction.addToBackStack("fragmetn_map_list");
                transaction.commit();
            }
        });

        rootView.setClickable(true);
        rootView.setFocusableInTouchMode(true);

        rootView.setOnKeyListener(new View.OnKeyListener() {
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


        return rootView;
    }


    public void startAsyncLoadImage() {
        Log.v("fragment", "startload");
        LoadDataTask task = new LoadDataTask(getActivity(), latLngs);
        task.execute();
    }

    public void addAsyncLoadImages() {
        LoadDataTask task = new LoadDataTask(getActivity(), latLngs, otherUsersListAdapter);
        task.execute();
    }


    @Subscribe
    public void onListload(List<OtherUsersList> otherUsersLists) {
        for (int i = 0; i < otherUsersLists.size(); i++) {
            Log.v("otheruserphotolist", otherUsersLists.get(i).getFilepath());
        }
        this.otherUsersLists = otherUsersLists;
        otherUsersListAdapter.setOtherUsersLists(otherUsersLists);
        listView.setAdapter(otherUsersListAdapter);
    }
}

