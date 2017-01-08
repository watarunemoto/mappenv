package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.graphics.BitmapFactory;
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
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/07/12.
 */
public class PhotoResultFormFragment extends Fragment {

    /**
     * マップで自分のマーカーをクリックした時にフォームを表示させるフラグメント
     */

    String[] data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_result_form,container,false);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            data = bundle.getStringArray("packdata");
        }
        view.setClickable(true);
        TextView title = (TextView) view.findViewById(R.id.fragment_form_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_form_image);

        title.setText(data[1]);
        imageView.setImageBitmap(BitmapFactory.decodeFile(data[0]));


        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                FragmentManager manager = getChildFragmentManager();
                Fragment fragment = manager.findFragmentByTag("fragment_map_list");
                Log.v("OtherPhotoEvalFragment","push back button");
                if (fragment != null) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.remove(fragment);
                        transaction.commit();
                    }
                }
                return true;
            }
        });

        return view;
    }
}


