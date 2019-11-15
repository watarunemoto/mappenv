package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class JmaDataDisp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jma_data_disp);
    }



//非同期処理
    private class WeatherInforeciever extends AsyncTask<String,String,String> {
        //intent処理
        Intent intent = getIntent();
        double markerlat = intent.getDoubleExtra("lat",0.0);
        double markerlng = intent.getDoubleExtra("lng",0.0);

        //画面部品取得処理
        TextView ODW =(TextView)findViewById(R.id.owa_data_weather);
        TextView ODMin=(TextView)findViewById(R.id.owa_data_min);
        TextView ODMax=(TextView)findViewById(R.id.owa_data_max);

          @Override
        public String doInBackground(String... params) {
            //URLの成形
            String OWMurl = "api.openweathermap.org/data/2.5/forecast?lat={"+markerlat+"}&lon={"+markerlng+"}";
            //それぞれの結果
            InputStream is = null;
            //URL接続を行うオブジェクトの生成
            HttpURLConnection con = null;
            String result="";
            try {
                URL url = new URL(OWMurl);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = is2String(is);
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    }
                catch(IOException ex){
                }
            }
        }
        return result;
    }



        @Override
        public void onPostExecute(String result){
              int min = 0;
              int max = 0;
              String weather ="";

            try{
                //JSON文字列からJSONObjectオブジェクトを生成、ルートJSON
                JSONObject rootJSON = new JSONObject(result);
                //ルートJSON直下の[list]オブジェクトを取得
                JSONArray list = rootJSON.getJSONArray("list");
                //[list]内のリストを取得
                JSONArray weatherlist = list.getJSONArray(2);
                JSONArray weathermain = list.getJSONArray(1);
                //それぞれのリストから必要な情報を取得
                min = weathermain.getInt(1);
                max = weathermain.getInt(2);
                weather = weatherlist.getString(1);


            }
            catch (JSONException ex){
            }

            ODMax.setText(max);
            ODMin.setText(min);
            ODW.setText(weather);
        }
    }



    //InputStreamオブジェクトを文字列に変換する。
    private String is2String (InputStream is) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while(0 <= (line = reader.read(b))){
            sb.append(b,0,line);
        }
        return sb.toString();
    }

}
