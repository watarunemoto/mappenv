package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

public class EventContentsActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_contents);
        //非同期処理
        EventInfoReceiver receiver = new EventInfoReceiver();
        //EventInfoReceiverを実行
        receiver.execute();
/**
        //RecyclerViewを取得
        RecyclerView lvMenu = findViewById(R.id.MyRecyclerView);
        //LinearLayoutManagerオブジェクトを生成
        LinearLayoutManager layout = new LinearLayoutManager(EventContentsActivity.this);
        //RecyclerViewにレイアウトマネージャーとしてLinearLayoutManagerを設定
        lvMenu.setLayoutManager(layout);
        //メニューリストデータを生成
        List<Map<String,Object>> menuList = createTeishokuList();
        //アダプタオブジェクトを生成
        RecyclerListAdapter adapter = new RecyclerListAdapter(menuList);
        //RecyclearViewにアダプタオブジェクトを設定。
        lvMenu.setAdapter(adapter);
        **/
    }








    private class RecyclerListViewHolder extends RecyclerView.ViewHolder {
        //リスト一行分でメニュー名を表示する画面部品
        public TextView _tvMenuName;
        //リスト一行分で金額を表示する画面部品
        public TextView _tvMenuPrice;

        /**
         * コンストラクタ
         * @param itemView　リスト一行分の画面部品
         */
        public RecyclerListViewHolder(View itemView) {
            //親クラスのコンストラクタの呼び出し
            super(itemView);
            //引数で渡されたリスト一行分の画面部品中から表示に使われるTextViewを取得
            _tvMenuName = itemView.findViewById(R.id.tvMenuName);
            _tvMenuPrice = itemView.findViewById(R.id.tvMenuPrice);
        }
    }


    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListViewHolder> {
        //リストデータを保持するフィールド
        private List<Map<String, Object>> _listData;

        /**
         * コンストラクタ
         * @param listData
         */
        public RecyclerListAdapter(List<Map<String, Object>> listData) {
            //引数のリストデータをフィールドに格納
            _listData = listData;
        }


    @Override
    public RecyclerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //レイアウトインフレーターを取得
        LayoutInflater inflater = LayoutInflater.from(EventContentsActivity.this);
        //my_text_viewをインフレートし、一行分の画面部品とする。
        View view = inflater.inflate(R.layout.my_text_view, parent, false);
        //ビューホルダーを生成
        RecyclerListViewHolder holder = new RecyclerListViewHolder(view);
        //生成したビューホルダーを返す。
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerListViewHolder holder, int position) {
        //リストデータから該当一行分のデータを取得
        Map<String, Object> item = _listData.get(position);
        //メニュー名文字列を取得
        String menuName = (String) item.get("name");
        Log.d("aiuto",menuName);
        //メニュー金額を取得
        int menuPrice = (int) item.get("price");
        //金額を文字列に変換
        String menuPriceStr = String.valueOf(menuPrice);
        Log.d("aiueo",menuPriceStr);
        //メニュー名と金額をビューホルダー中のTextViewに設定。
        holder._tvMenuName.setText(menuName);
        holder._tvMenuPrice.setText(menuPriceStr);
    }

    @Override
    public int getItemCount() {
        //リストデータ中の件数をリターン
        return _listData.size();
    }

}


//RecyclerViewに設定するリストデータを用意

    private List<Map<String,Object>> createTeishokuList(){
        List<Map<String,Object>> menuList = new ArrayList<>();

        Map<String,Object> menu = new HashMap<>();
        menu.put("name","My name is hoge");
        menu.put("price", 700);
        menuList.add(menu);

        menu = new HashMap<>();
        menu.put("name", "Hello");
        menu.put("price",500);
        menuList.add(menu);


        return menuList;

    }





    //非同期処理のクラス

    private class EventInfoReceiver extends AsyncTask<String,String,String> {


        @Override
        public String doInBackground(String...params){
            //接続URLを作成
            String urlstr = UrlCollections.URL_GET_EVENT;
            //取得したイベント情報を格納する
            String result ="";

            //サーバに接続、JSONを取得する
            //HTTP接続を行うHttpURLConnectionオブジェクトを宣言
            HttpURLConnection con = null;
            //HTTP接続のレスポンスデータとして取得するInputStreamオブジェクトを宣言。
            InputStream is = null;
            try{
                //URLオブジェクトを生成
                URL url = new URL(urlstr);
                //URLオブジェクトからHttpURLConnectionオブジェクトを取得
                con = (HttpURLConnection) url.openConnection();
                //HTTP接続メソッドを設定
                con.setRequestMethod("GET");
                //接続
                con.connect();
                //HttpURLConnectionオブジェクトからレスポンスデータを取得
                is = con.getInputStream();
                //レスポンスデータであるInputStreamオブジェクトを文字列に変換
                result = is2String(is);
            }
            catch (MalformedURLException ex){
            }
            catch (IOException ex){
            }
            finally {
                //HttpURLConnectionオブジェクトがnullでななら解放。
                if(con != null){
                    con.disconnect();
                }
                //InputStreamオヴジェクトがnullでないなら解放
                if(is != null){
                    try{
                        is.close();
                    }
                    catch (IOException ex){

                    }
                }
            }

            //Json文字列を返す
            return result;
        }


        //
        @Override
        public void onPostExecute(String result){
            //JSON文字列を解析、List<Map<String,Object>>の形にする。
            try {
                //JSON文字列からJSONObjectオブジェクトを作成。これをルートJSONオブジェクトとする。
                //JSONObject rootJSON = new JSONObject(result);
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<String,Object>> type = new TypeRecerence<List<Map<String,Object>>>(){}
                List<Map<String,Object>> list = mapper.readValue(result,type);



                /**
                //ルートJSON直下の「description」JSONオブジェクトを取得
                JSONObject descriptionJSON = rootJSON.getJSONObject("description");
                //「description」プロパティ直下の「text」文字列を取得
                desc = descriptionJSON.getString("text");
                //ルートJSON直下の「forecasts」JSON配列を取得
                JSONArray forecasts = rootJSON.getJSONArray("forecasts");
                //「forecasts」JSON配列の一つ目のJSONobjectを取得
                JSONObject forecastNow = forecasts.getJSONObject(0);
                //「forecasts」一つ目のJSONオブジェクトから「telop」文字列を取得
                telop = forecastNow.getString("telop");
                 **/
            }
            catch (JSONException ex){
            }
            /**
            //RecyclerViewを取得
            RecyclerView lvMenu = findViewById(R.id.MyRecyclerView);
            //LinearLayoutManagerオブジェクトを生成
            LinearLayoutManager layout = new LinearLayoutManager(EventContentsActivity.this);
            //RecyclerViewにレイアウトマネージャーとしてLinearLayoutManagerを設定
            lvMenu.setLayoutManager(layout);
            //メニューリストデータを生成
            List<Map<String,Object>> menuList = createTeishokuList();
            //アダプタオブジェクトを生成
            RecyclerListAdapter adapter = new RecyclerListAdapter(menuList);
            //RecyclearViewにアダプタオブジェクトを設定。
            lvMenu.setAdapter(adapter);
             **/
            Log.d("aiueo",result);
        }


        //取得したInputStreamを文字列に変形するメソッド
        private String is2String(InputStream is) throws IOException{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"))
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))){
                sb.append(b,0,line);
            }
            return sb.toString();

        }
    }
}


