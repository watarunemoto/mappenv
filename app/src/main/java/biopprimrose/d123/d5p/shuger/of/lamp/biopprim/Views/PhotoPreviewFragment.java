package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.preference.PreferenceManager;
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






//        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        matrix.postRotate(90);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), bitmap, options);
//        Bitmap bitmap = decodeSampledBitmapFromResource(img_path, 600, 800);
        Bitmap bitmap = decodeSampledBitmapFromResource(img_path, bundle.getInt("width"), bundle.getInt("height"));
        upload_img.setImageBitmap(bitmap);
        upload_img.setRotation(90);
        upload_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        upload_img.getMaxHeight()



//        upload_img.setImageBitmap(
//                decodeSampledBitmapFromResource(img_path, 240, 320));

//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        upload_img.setImageBitmap(bitmap);

        //SharedPreferenceに保存されているイベントIDを取得する
        SharedPreferences pref = getContext().getSharedPreferences("EventIDSave",Context.MODE_PRIVATE);
        final int Event_ID = pref.getInt("EventID",100);
        //Log.d("event",String.valueOf(intvalue));

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
                PhotoPostTask hpt = new PhotoPostTask(getActivity(), last_latitude, last_longitude, photoname,annotation, Event_ID);
//                hpt.execute(UrlCollections.URL_UPLOAD_PHOTO , img_path, userID);
                Log.v("annotation",annotation);
                hpt.execute(UrlCollections.URL_UPLOAD_PHOTO , img_path, userID, imgname, annotation);
                Log.v("imgname",imgname);
                Log.v("photoname",photoname);
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
                PhotoPostTask hpt = new PhotoPostTask(getActivity(), last_latitude, last_longitude, photoname, annotation, Event_ID);
                hpt.nouploaddb(img_path);
            }
        });
        view.setClickable(true);
        return view;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 4;
            final int halfWidth = width / 4;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 4;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String resPath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(resPath,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;



        return BitmapFactory.decodeFile(resPath,options);
    }

}
