package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendRequestTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.FriendResponse;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequestFragment extends Fragment {


    String[] put_string_array = new String[4];
    IBinder token;
    public FriendRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        final EditText edit = (EditText) view.findViewById(R.id.input_userid);
        ImageButton search = (ImageButton) view.findViewById(R.id.search_friend);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String username = sp.getString("userid","");

        token = edit.getWindowToken();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid;
                userid = edit.getText().toString();
                Log.v("use", userid);
                FriendRequestTask task = new FriendRequestTask(getActivity(),username, userid);
                task.execute("3");
               // put_string_array[0] = username;
                put_string_array[0] = "951737";
                put_string_array[1] = userid;
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
    public void req_sended(FriendResponse fr) {
        if (!fr.getOther_id().equals("") && !fr.getOther_name().equals("")) {
            put_string_array[2] = fr.getOther_id();
            put_string_array[3] = fr.getOther_name();

            Bundle bundle = new Bundle();
            bundle.putStringArray("userid_otheruserid", put_string_array);

            FriendRequestFormFragment fragment = new FriendRequestFormFragment();
            fragment.setArguments(bundle);
            FragmentTransaction manager = getChildFragmentManager().beginTransaction();
            manager.replace(R.id.container,fragment);
            manager.addToBackStack("fragment_form");
            manager.commit();
            EventBus.getDefault().unregister(this);

            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService((Context.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(token,InputMethodManager.HIDE_IMPLICIT_ONLY);

        } else {
            String text = getActivity().getResources().getString(R.string.no_user_message);
            Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
        }
    }

}
