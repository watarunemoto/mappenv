package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RankCategoryDownloader extends NewRankDownloader {
    private String[] mExtraParam;

    public RankCategoryDownloader(Context context, String... extraParam) {
        super(context);
        mExtraParam = extraParam;
    }

    @Override
    public String loadInBackground() {

        Log.d("SampleLoader", "loadInBackground");

        try {
            Thread.sleep(3000);
        }        catch (InterruptedException ignored) {
        }

        HttpURLConnection connection = null;
        String urlStr = mExtraParam[0];
        Log.d(urlStr,"Urlstr");


        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null)
                sb.append(line);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            connection.disconnect();
        }
        Log.d(sb.toString(),"Stringbuilder");


//        return "task complete: " + mExtraParam + sb.toString();
        try {
            return sb.toString();
        }catch(NullPointerException e) {
            Log.d("ぬるぽ","ぬるぽ");
            return null;
        }

    }
}
