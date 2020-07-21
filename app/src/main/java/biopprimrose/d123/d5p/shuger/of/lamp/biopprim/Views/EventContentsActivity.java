package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

public class EventContentsActivity extends AppCompatActivity {
    //イベント情報リストにおけるイベント名のキー値
    String key1 = "eventname";
    //イベント情報リストにおける主催者のキー値
    String key2 = "organizer";
    //イベント情報リストにおけるコンテンツ内容のキー値
    String key3 = "eventcontents";
    //イベントIDのキー値
    String key4 = "event_id";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_contents);
        //非同期処理
        EventInfoReceiver receiver = new EventInfoReceiver();
        //EventInfoReceiverを実行
        receiver.execute();
    }









    //非同期処理のクラス
    private class EventInfoReceiver extends AsyncTask<String,String,String> {
        @Override
        public String doInBackground(String...params){
            //接続URLを作成
            String urlstr = UrlCollections.URL_GET_EVENT;
            //取得したイベント情報を格納する
            String result ="";

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


        //HTTP接続後、得られたJSON文字列からrecyclerViewを設定
        @Override
        public void onPostExecute(String result){
            try {
                //取得できたJSON文字列を、リストの形に成形する。
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<Map<String,Object>>> type = new TypeReference<List<Map<String, Object>>>(){};
                List<Map<String,Object>> eventlist = mapper.readValue(result,type);

                //RecyclerViewを取得
                RecyclerView lvMenu = findViewById(R.id.MyRecyclerView);
                //LinearLayoutManagerオブジェクトを生成
                LinearLayoutManager layout = new LinearLayoutManager(EventContentsActivity.this);
                //RecyclerViewにレイアウトマネージャーとしてLinearLayoutManagerを設定
                lvMenu.setLayoutManager(layout);
                //アダプタオブジェクトを生成
                RecyclerListAdapter adapter = new RecyclerListAdapter(eventlist);
                //RecyclearViewにアダプタオブジェクトを設定。
                lvMenu.setAdapter(adapter);
            }
            catch (IOException ex){
            }
            Log.d("aiueo",result);
        }


        //取得したInputStreamを文字列に変形するメソッド
        private String is2String(InputStream is) throws IOException{
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







    private class RecyclerListViewHolder extends RecyclerView.ViewHolder {
        //リスト一行分でメニュー名を表示する画面部品
        public TextView _tvMenuName;
        //リスト一行分で金額を表示する画面部品
        public TextView _tvMenuPrice;
        //詳細ボタンの画面部品
        public Button _AboutButton;
        //参加ボタンの画面部品
        public Button _JoinButton;

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
            _AboutButton = itemView.findViewById(R.id.aboutButton);
            _JoinButton  = itemView.findViewById(R.id.JoinButton);
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
            View view = inflater.inflate(R.layout.event_choice_elements, parent, false);
            //ビューホルダーを生成
            RecyclerListViewHolder holder = new RecyclerListViewHolder(view);
            //生成したビューホルダーを返す。
            return holder;
        }








        public void onBindViewHolder(RecyclerListViewHolder holder, int position) {
            //リストデータから該当一行分のデータを取得
            Map<String, Object> item = _listData.get(position);
            //”eventname”の文字列を取得
            String menuName = (String) item.get(key1);
            //"organizer"の文字列を取得
            String menuPrice = (String) item.get(key2);
            //金額を文字列に変換
            String menuPriceStr = String.valueOf(menuPrice);
            //メニュー名と金額をビューホルダー中のTextViewに設定。
            holder._tvMenuName.setText(menuName);
            holder._tvMenuPrice.setText(menuPriceStr);



            String contents = (String) item.get(key3);
            //holder._AboutText.setText(contents);
            int eventid = (int) item.get(key4);

            holder._AboutButton.setOnClickListener(new ItemClickListener(contents,eventid));
            holder._JoinButton.setOnClickListener(new ItemClickListener(contents,eventid));

            //Log.d("aiueo",contents);

        }

        @Override
        public int getItemCount() {
            //リストデータ中の件数をリターン
            return _listData.size();
        }
    }

    private class ItemClickListener implements View.OnClickListener{
        private String contents;
        private int eventID;


        public ItemClickListener(String Eventcontents, int eventid){
            this.contents = Eventcontents;
            this.eventID = eventid;
        }

        @Override
        public void onClick(View view){
            switch (view.getId()){
                case (R.id.aboutButton):
                    Intent intent = new Intent(EventContentsActivity.this, EventInfomationActivity.class);
                    intent.putExtra("AboutEvent",contents);
                    startActivity(intent);
                    break;

                case (R.id.JoinButton):

                    Toast.makeText(EventContentsActivity.this, String.valueOf(eventID),Toast.LENGTH_LONG).show();

                    break;
            }


        }
    }

}


