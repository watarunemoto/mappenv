package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.LoadDataTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.OtherPhotoGetter;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.OtherUsersPhotoData;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PointTransfer;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

/**
 * Created by amemiyaY on 2016/05/07.
 */
public class OtherPhotoEvalFragment extends Fragment {

    String[] packdata;
    private String username;
    Boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null){
            packdata = getArguments().getStringArray("filename_name_class");
        } else {
            return null;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sp.getString("MyID", "null");

        View rootView =
                inflater.inflate(R.layout.activity_other_photo_view_form ,container,false);

        final Button goodButton = (Button) rootView.findViewById(R.id.btn_good);
        final Button badButton = (Button) rootView.findViewById(R.id.btn_bad);
        final TextView pname = (TextView)rootView.findViewById(R.id.text_pname);
        final TextView scores = (TextView)rootView.findViewById(R.id.score);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

        goodButton.setEnabled(false);
        badButton.setEnabled(false);

        pname.setText(packdata[1]);
        scores.setText(packdata[2]);

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodButton.setEnabled(false);
                badButton.setEnabled(false);
                Toast.makeText(getActivity(), R.string.push_good, Toast.LENGTH_SHORT).show();
                dbInsert("1");
            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodButton.setEnabled(false);
                badButton.setEnabled(false);
                Toast.makeText(getActivity(), R.string.push_bad, Toast.LENGTH_SHORT).show();
                dbInsert("-1");
            }
        });

        OtherOpenHelper oph = new OtherOpenHelper(getActivity());
        SQLiteDatabase db = oph.getReadableDatabase();
        Cursor c = db.query(
                OtherUsersPhotoData.OtherImages.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (c.moveToNext()) {
            String picname = c.getString(c.getColumnIndex(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME));

            if (picname.equals(packdata[0])) {
                badButton.setEnabled(false);
                goodButton.setEnabled(false);
                flag = false;
            }
        }

        if (flag) {
            badButton.setEnabled(true);
            goodButton.setEnabled(true);
        }

        /**
        LoadDataTask ldt = new LoadDataTask(getActivity(),imageView);
        ldt.execute(packdata[0]);
         */

        imageView.setImageBitmap(BitmapFactory.decodeFile(packdata[0]));

        c.close();
        db.close();


        rootView.setClickable(true);
        rootView.setFocusableInTouchMode(true);
        rootView.setOnKeyListener(new View.OnKeyListener() {
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


        return rootView;
    }

    public void dbInsert(String selected) {
        OtherOpenHelper oph = new OtherOpenHelper(getActivity());
        SQLiteDatabase db = oph.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(OtherUsersPhotoData.OtherImages.COL_EVAED, selected);
        cv.put(OtherUsersPhotoData.OtherImages.COL_PNAME,packdata[2]);
        cv.put(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME,packdata[0]);
        db.insert(
                OtherUsersPhotoData.OtherImages.TABLE_NAME,
                null,
                cv
        );

//        (String filename, String point, String eva_user, String evaed_user
        PointTransfer pt = new PointTransfer(packdata[2],selected,username,packdata[3]);
        pt.execute(UrlCollections.URL_EVAL_UPLOAD);
    }


}
