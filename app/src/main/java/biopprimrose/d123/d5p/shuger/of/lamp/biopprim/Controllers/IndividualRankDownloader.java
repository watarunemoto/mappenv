package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class  IndividualRankDownloader {
    public IndividualRankDownloader(){

    }

    public String GetInformation(String uri) {
        HttpURLConnection connection = null;
        String urlStr = uri;
        Log.d(urlStr, "Urlstr");

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
    }

//    オーバーライド 各カテゴリランキングのuriを叩く
//    public String GetInformation(String uri ,String field,String value) {
//        HttpURLConnection connection = null;
//        String urlStr = uri;
//        Log.d(urlStr, "Urlstr");
//
//        StringBuilder sb = new StringBuilder();
//        try {
//            URL url = new URL(urlStr);
//            connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestProperty(field,value);
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            InputStream is = connection.getInputStream();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String line = "";
//            while ((line = reader.readLine()) != null)
//                sb.append(line);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            connection.disconnect();
//        }
//        Log.d(sb.toString(), "Stringbuilder");
//
//
////        return "task complete: " + mExtraParam + sb.toString();
//        try {
//            return sb.toString();
//        } catch (NullPointerException e) {
//            Log.d("ぬるぽ", "ぬるぽ");
//            return null;
//        }
//    }
}


