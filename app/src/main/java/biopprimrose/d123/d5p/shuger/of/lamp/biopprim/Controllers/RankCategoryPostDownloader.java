package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class RankCategoryPostDownloader extends AsyncTaskAbstract {

    private String mURI;
    private String mQueryKey;
    private String mQueryValue;
    private Bundle mbundle;

    public RankCategoryPostDownloader(Context context, Bundle bundle) {
        super(context);
        mbundle = bundle;
        mURI = bundle.getString("URI",null);
        mQueryKey = bundle.getString("RequestBody",null);
        mQueryValue = bundle.getString("RequestValue",null);

    }

    @Override
    public String loadInBackground() {

        Log.d("SampleLoader", "loadInBackground");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {

        }


        HttpURLConnection connection = null;
        URL url = null;
        String urlStr = mURI;
        Log.d(urlStr, "Urlstr");

        HashMap<String,String> queryParams = new HashMap<>();
        queryParams.put(mQueryKey, mQueryValue);


        try {
            if (queryParams == null) {
                url = new URL(urlStr);
            } else {
                Uri.Builder builder = new Uri.Builder();
                Set keys = queryParams.keySet();
                for(String key : keys) {
                    builder.appendQueryParameter(key, queryParams.get(key));
                }
                url = new URL(url + builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try {
//            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null)
                sb.append(line);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        Log.d(sb.toString(), "Stringbuilder");


//        return "task complete: " + mExtraParam + sb.toString();
        try {
            return sb.toString();
        } catch (NullPointerException e) {
            Log.d("ぬるぽ", "ぬるぽ");
            return null;
        }




//        HttpURLConnection connection = null;
//        String urlStr = mURI[0];
//        Log.d(urlStr,"Urlstr");
//
//
//        StringBuilder sb = new StringBuilder();
//        try {
//            URL url = new URL(urlStr);
//            connection = (HttpURLConnection) url.openConnection();
//            InputStream is = connection.getInputStream();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String line = "";
//            while ((line = reader.readLine()) != null)
//                sb.append(line);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally{
//            connection.disconnect();
//        }
//        Log.d(sb.toString(),"Stringbuilder");
//
//
////        return "task complete: " + mURI + sb.toString();
//        try {
//            return sb.toString();
//        }catch(NullPointerException e) {
//            Log.d("ぬるぽ","ぬるぽ");
//            return null;
//        }



    }
}
