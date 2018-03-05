package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.PhotoPostTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;


/**
 * created by amemiya 8/2/2017
 */
public class PhotoPreviewFragment extends Fragment {


    public PhotoPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_preview, container, false);

        ImageView upload_img = (ImageView) view.findViewById(R.id.upload_img);
        final Button upload_button = (Button) view.findViewById(R.id.upload_button);
        final Button save_button = (Button) view.findViewById(R.id.save_button);
        final EditText editpname = (EditText) view.findViewById(R.id.editpname);


        Bundle bundle = getArguments();
        final String img_path  = bundle.getString("img_path","");
//      /data/data/biopprimrose.d123.d5p.shuger.of.lamp/cmr/
        final String last_latitude = bundle.getString("last_latitude","");
        final String last_longitude = bundle.getString("last_longitude","");
        final String userID = bundle.getString("userid");
        final String annotation = bundle.getString("annotation","");
        Log.v("aotation?:",""+ annotation);
        final String imgname = img_path.replace("/data/data/biopprimrose.d123.d5p.shuger.of.lamp/cmr/","");
//        final String photoname;

//        if (editpname.getText().toString().equals("")){
//            pname = "";
//        }else{
//            pname = editpname.getText().toString();
//        }






        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        upload_img.setImageBitmap(bitmap);

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_button.setClickable(false);
                getFragmentManager().popBackStack();


                String photoname;
                if (editpname.getText().toString().equals("")){
                    photoname = "unknown";
                }else{
                    photoname = editpname.getText().toString();
                }
                Log.v("photopreview",last_latitude+last_longitude+ photoname);
//                PhotoPostTask hpt = new PhotoPostTask(getActivity(), last_latitude, last_longitude, pname);
                PhotoPostTask hpt = new PhotoPostTask(getActivity(), last_latitude, last_longitude, photoname,annotation);
//                hpt.execute(UrlCollections.URL_UPLOAD_PHOTO , img_path, userID);
                Log.v("annotation",annotation);
                hpt.execute(UrlCollections.URL_UPLOAD_PHOTO , img_path, userID, imgname, annotation);
                Log.v("imgname",imgname);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_button.setClickable(false);
                getFragmentManager().popBackStack();
                String pname = editpname.getText().toString();
                String photoname;
                if (pname.equals("")){
                    photoname = "unknown";
                }else{
                    photoname = pname;
                }
                Log.v("photopreview",last_latitude+last_longitude+pname);
                Log.v("annotation:",""+ annotation);
                PhotoPostTask hpt = new PhotoPostTask(getActivity(), last_latitude, last_longitude, photoname, annotation);
                hpt.nouploaddb(img_path);
            }
        });
        view.setClickable(true);
        return view;
    }

}
