package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class RankCategoryPostDownloader extends AsyncTaskAbstract {

    private String mURI;
    private String mQueryKey;
    private String mQueryValue;

    public RankCategoryPostDownloader(Context context, Bundle bundle) {
        super(context);
        mURI = bundle.getString("URI", null);
        mQueryKey = bundle.getString("QueryKey", null);
        mQueryValue = bundle.getString("QueryValue", null);

    }



    @Override
    public String loadInBackground() {

        Log.d("SampleLoader", "loadInBackground");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {

        }

        int responseCode = 0;
        String responseData = "";
        HttpURLConnection connection = null;
        URL url = null;
        String urlStr = mURI;
        Log.d(urlStr, "Urlstr");

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put(mQueryKey, mQueryValue);
//        StringBuilder sb = new StringBuilder();
        Log.d(queryParams.toString(),"PostQueryParams");
        try {
            if (mQueryKey != null | mQueryValue != null) {
                Uri.Builder builder = new Uri.Builder();
                Set keys = queryParams.keySet();
//            URL url = new URL(urlStr);
                for (Object key : keys) {
                    builder.appendQueryParameter(key.toString(), queryParams.get(key));
                }
                url = new URL(urlStr + builder.toString());
            }else{
                url = new URL(urlStr);
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.setRequestProperty("User-Agent", "Android");
            connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();

            Gson gson = new Gson();

            responseCode = connection.getResponseCode();

            if (responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                responseData = convertToString(connection.getInputStream());
//                Gson gson = new Gson();
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//                responseData =  gson.fromJson(br,String.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        Log.d("execute", "URL:" + urlStr);
        Log.d("execute", "HttpStatusCode:" + responseCode);
        Log.d("execute", "ResponseData:" + responseData);
//        return jsonObject;
        return responseData;
    }

    public String convertToString(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
