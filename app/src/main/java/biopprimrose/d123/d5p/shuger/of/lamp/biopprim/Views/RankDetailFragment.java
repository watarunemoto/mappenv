package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/07/02.
 */
public class RankDetailFragment extends Fragment {

    String username = "";
    String point = "";
    String photo_num ="";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_detail,container, false);


        if (getArguments() != null) {
            Bundle bundle = getArguments();
            username = bundle.getString("userid", "unknown");
            point = bundle.getString("point","666");
            photo_num = bundle.getString("num","666");
        }

        TextView userid_text = (TextView) view.findViewById(R.id.userid_rank_detail);
        userid_text.setText("ID:"+ username);
        TextView point_text = (TextView) view.findViewById(R.id.point_rank_detail);
//        point_text.setText("POINT:"+ point);
        point_text.setText(getResources().getString(R.string.ranking_count)+ photo_num);
        TextView photo_num_text = (TextView) view.findViewById(R.id.numphoto_rank_detail);
//        photo_num_text.setText(photo_num);
        photo_num_text.setText(getResources().getString(R.string.ranking_points) + point);

        return view;
    }
}
