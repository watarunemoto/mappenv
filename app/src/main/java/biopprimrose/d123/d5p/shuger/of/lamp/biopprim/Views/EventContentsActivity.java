package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.nfc.Tag;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class EventContentsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_contents);
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
}


