package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            packdata = getArguments().getStringArray("packdata");
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
        final TextView classs = (TextView)rootView.findViewById(R.id.classname);
        final TextView scores = (TextView)rootView.findViewById(R.id.score);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

        goodButton.setEnabled(false);
        badButton.setEnabled(false);

        pname.setText(packdata[0]);
        scores.setText( packdata[2]);

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

            if (picname.equals(packdata[3])) {
                badButton.setEnabled(false);
                goodButton.setEnabled(false);
                flag = false;
            }
        }

        OtherPhotoGetter opg = new OtherPhotoGetter(getActivity(),imageView,goodButton,badButton,flag);
        opg.execute(UrlCollections.URL_OTHER_PHOTO, packdata[3]);

        c.close();
        db.close();

        return rootView;
    }

    public void dbInsert(String selected) {
        OtherOpenHelper oph = new OtherOpenHelper(getActivity());
        SQLiteDatabase db = oph.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(OtherUsersPhotoData.OtherImages.COL_EVAED, selected);
        cv.put(OtherUsersPhotoData.OtherImages.COL_PNAME,packdata[0]);
        cv.put(OtherUsersPhotoData.OtherImages.COLUMN_FILE_NAME,packdata[3]);
        db.insert(
                OtherUsersPhotoData.OtherImages.TABLE_NAME,
                null,
                cv
        );
        PointTransfer pt = new PointTransfer(packdata[3],selected,username,packdata[4]);
        pt.execute();
    }
}
