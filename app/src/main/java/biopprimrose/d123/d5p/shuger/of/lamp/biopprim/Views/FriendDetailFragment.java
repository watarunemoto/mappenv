package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.OtherUsersList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.RecycleAdapter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.DataController;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendDatabase;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.LoadDataTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.OnRecycleListener;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


public class FriendDetailFragment extends Fragment
    implements OnRecycleListener
{
    RecyclerView recyclerView;


    public FriendDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_detail, container, false);

        TextView username = (TextView) view.findViewById(R.id.userid);
        TextView score = (TextView) view.findViewById(R.id.score);
        recyclerView = (RecyclerView) view.findViewById(R.id.friend_photo_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Log.v("pos", "frienddetailfragment");

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int pos = bundle.getInt("pos");
            Log.v("pos", String.valueOf(pos));
            FriendDatabase fd = new FriendDatabase(getActivity());
            FriendList fl = fd.Read_db_num(pos);
            username.setText(fl.getUsername());

            String friend_id = String.valueOf(fl.getUserid());
            friend_id = "126958";
            LoadDataTask task = new LoadDataTask(getActivity(),friend_id);
            task.execute();
        }

        return view;

    }

    @Subscribe
    public void onloaded(List<OtherUsersList> otherUsersListList) {
        Log.v("onloaded","Detail");
        for (int i = 0; i < otherUsersListList.size(); i++) {
            Log.v("res_array",otherUsersListList.get(i).getFilepath());
        }

        RecycleAdapter recycleAdapter = new RecycleAdapter(getActivity(),otherUsersListList,this);
        recyclerView.setAdapter(recycleAdapter);
    }

    @Override
    public void onRecycleClicked(View v, int Position) {



    }
}
