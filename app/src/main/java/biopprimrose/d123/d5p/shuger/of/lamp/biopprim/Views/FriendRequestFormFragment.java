package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.DataController;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendDatabase;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendRequestTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendResponse;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/11/18.
 * This class display a request result.
 */
public class FriendRequestFormFragment extends Fragment {

    FriendResponse fr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_request_form, container, false);

        TextView userid = (TextView) view.findViewById(R.id.form_userid);
        TextView username = (TextView) view.findViewById(R.id.form_username);
        Button request_send_button = (Button) view.findViewById(R.id.request_send_button);

        request_send_button.setEnabled(false);

        Log.v("yellow", "form read");
        String[] userid_otheruserid;
        if (getArguments() == null) {
            return null;
        }

        Log.v("yellow", "form read");

        Bundle bundle = getArguments();
        userid_otheruserid = bundle.getStringArray("userid_otheruserid");

        if (userid_otheruserid != null && userid_otheruserid.length != 4) {
            return null;
        }

        final String myid = userid_otheruserid[0];
        final String other_id = userid_otheruserid[1];

        userid.setText(userid_otheruserid[2]);
        username.setText(userid_otheruserid[3]);

        fr = new FriendResponse();
        fr.setOther_id(userid_otheruserid[2]);
        fr.setOther_name(userid_otheruserid[3]);
        fr.setIsAccepted("2");
        fr.setJob_id("");

        request_send_button.setEnabled(true);


        request_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean flag = true;
                FriendDatabase fd = new FriendDatabase(getActivity());
                List<FriendList> friendListLists = fd.Read_db();
                for (int i = 0;i<friendListLists.size();i++) {
                    int id =  friendListLists.get(i).getUserid();

                    if (id == Integer.valueOf(other_id)) {
                        flag = false;
                        Log.v("id_id",id + other_id);
                    }
                }
                if (flag) {
                    FriendRequestTask task = new FriendRequestTask(getActivity(), myid, other_id);
                    task.execute("0");
                } else {
                    Toast.makeText(getActivity(),"すでにリクエストが存在するか友達です",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void requested(FriendResponse response) {
        String normal_response = response.getNormal_response().replace(" ","").replace("\n","");
        Log.v("yellow__", normal_response + "," + "normalsended"+normal_response.equals("normalsended"));

        if (normal_response.matches(".*sended.*")){
            String text = getActivity().getResources().getString(R.string.friend_request_sent_text);
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            int req_id = Integer.parseInt(fr.getOther_id());
            Boolean flag = true;
            FriendDatabase fd = new FriendDatabase(getActivity());
            List<FriendList> friendListLists = fd.Read_db();
            for (int i = 0;i<friendListLists.size();i++) {
                int id =  friendListLists.get(i).getUserid();
                if (id == req_id) {
                    flag = false;
                }
            }
            if (flag) {
                fd.dbch(fr);
            }

        } else if (normal_response.equals("normalisExisted")){
            String text = getActivity().getResources().getString(R.string.is_existed);
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }else{
            String text = getActivity().getResources().getString(R.string.something_to_wrong);
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
        getChildFragmentManager().beginTransaction().hide(this).commit();
    }
}
