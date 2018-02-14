package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.AnotationListAdaptor;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.ItemDto;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class AnotationMain extends AppCompatActivity {
    /** Called when the activity is first created. */
//    int PARENT_DATA = 3;
//    int CHILD_DATA = 3;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anotation_main);

        Resources res = getResources();
        String initialcom = res.getString(R.string.anotxt);
//        TextView information = (TextView)findViewById(R.id.anotext);
//        information.setText(initialcom);
        
//
//
//        // 親ノードのリスト
//        List<Map<String, String>> parentList = new ArrayList<Map<String, String>>();
//        // 全体の子ノードのリスト
//        List<List<Map<String, String>>> allChildList = new ArrayList<List<Map<String, String>>>();
//
//        // 親ノードに表示する内容を生成
//        for (int i = 0; i < PARENT_DATA; i++) {
//            Map<String, String> parentData = new HashMap<String, String>();
//            parentData.put("title", "title" + Integer.toString(i));
//            // 親ノードのリストに内容を格納
//            parentList.add(parentData);
//        }
//
//        // 子ノードに表示する文字を生成
//        for (int i = 0; i < PARENT_DATA; i++) {
//            // 子ノード全体用のリスト
//            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();
//
//            // 各子ノード用データ格納
//            for (int j = 0; j < 3; j++) {
//                Map<String, String> childData = new HashMap<String, String>();
//                childData.put("TITLE", "child" + Integer.toString(j));
//                childData.put("SUMMARY", "summary" + Integer.toString(j));
//                // 子ノードのリストに文字を格納
//                childList.add(childData);
//            }
//            // 全体の子ノードリストに各小ノードリストのデータを格納
//            allChildList.add(childList);
//        }
//
//        // アダプタを作る
//        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
//                this, parentList,
//                android.R.layout.simple_expandable_list_item_1,
//                new String[] { "title" }, new int[] { android.R.id.text1 },
//                allChildList, android.R.layout.simple_expandable_list_item_2,
//                new String[] { "TITLE", "SUMMARY" }, new int[] {
//                android.R.id.text1, android.R.id.text2 });
//
//        ExpandableListView lv = (ExpandableListView) findViewById(R.id.expandableListView1);
//        //生成した情報をセット
//        lv.setAdapter(adapter);
//
//        // リスト項目がクリックされた時の処理
//        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View view,
//                                        int groupPosition, int childPosition, long id) {
//                ExpandableListAdapter adapter = parent
//                        .getExpandableListAdapter();
//
//                // クリックされた場所の内容情報を取得
//                Map<String, String> item = (Map<String, String>) adapter
//                        .getChild(groupPosition, childPosition);
//
//
//                CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox1);
//                // チェックボックスのチェック状態を設定します
//                checkBox.setChecked(true);
//                checkBox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    // チェックボックスがクリックされた時に呼び出されます
//                    public void onClick(View v) {
//                        CheckBox checkBox = (CheckBox) v;
//                        // チェックボックスのチェック状態を取得します
//                        boolean checked = checkBox.isChecked();
//                        Toast.makeText(AnotationMain.this,
//                                "onClick():" + String.valueOf(checked),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // トーストとして表示
////                Toast.makeText(
////                        getApplicationContext(),
////                        "child clicked " + item.get("TITLE") + " "
////                                + item.get("SUMMARY"), Toast.LENGTH_LONG)
////                        .show();
//                return false;
//            }
//        });
//
//        // グループの親項目がクリックされた時の処理
//        lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View view,
//                                        int groupPosition, long id) {
//
//                ExpandableListAdapter adapter = parent
//                        .getExpandableListAdapter();
//
//                // クリックされた場所の内容情報を取得
//                Map<String, String> item = (Map<String, String>) adapter
//                        .getGroup(groupPosition);
//                // トーストとして表示
//                Toast.makeText(getApplicationContext(),
//                        "parent clicked " + item.get("title"),
//                        Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
//    }

        ExpandableListView listView = (ExpandableListView)findViewById(R.id.sample_list);
        int[] rowId = {0,1,2};
//        listView.setAdapter(new AnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList()));
        listView.setAdapter(new AnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList(),buildTestData()));
//


//
//
//        // リスト項目がクリックされた時の処理
//        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View view,
//                                        int groupPosition, int childPosition, long id) {
//                ExpandableListAdapter adapter = parent
//                        .getExpandableListAdapter();
//
//                // クリックされた場所の内容情報を取得
//                Map<String, String> item = (Map<String, String>) adapter
//                        .getChild(groupPosition, childPosition);
//
//
//                CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
//                // チェックボックスのチェック状態を設定します
//                checkBox.setChecked(true);
//                checkBox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    // チェックボックスがクリックされた時に呼び出されます
//                    public void onClick(View v) {
//                        CheckBox checkBox = (CheckBox) v;
//                        // チェックボックスのチェック状態を取得します
//                        boolean checked = checkBox.isChecked();
//                        Toast.makeText(AnotationMain.this,
//                                "onClick():" + String.valueOf(checked),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });

////                 トーストとして表示
//                Toast.makeText(
//                        getApplicationContext(),
//                        "child clicked " + item.get("TITLE") + " "
//                                + item.get("SUMMARY"), Toast.LENGTH_LONG)
//                        .show();
//                return false;
//            }
//        });
//


//        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        "child clicked "
//                        , Toast.LENGTH_LONG)
//                        .show();
//
//                return false;
//            }
//        });
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (view.getId()) {
//                    case R.id.checkbox1:
//                        Toast.makeText(AnotationMain.this, List<List<ItemDto>> + "の編集ボタンが押されました", Toast.LENGTH_SHORT).show();
//                        break;
////                    case R.id.delete:
////                        Toast.makeText(AnotationMain.this, List<> + "の削除ボタンが押されました", Toast.LENGTH_SHORT).show();
////                        break;
//                }
//            }
//        });


    }

     List<Boolean> buildTestData() {
        List<Boolean> items = new ArrayList<Boolean>();
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
//        items.add(true);
//        items.add(false);
        items.add(false);
        items.add(false);
        items.add(false);
        items.add(false);
        items.add(false);
        items.add(false);
        items.add(false);

        return items;
    }


    private List<List<ItemDto>> createChildrenItemList() {
        List<ItemDto> child = new ArrayList<ItemDto>();
        child.add(new ItemDto("植物の上部に多く生えている"));
        child.add(new ItemDto("植物の下部に多く生えている"));
        child.add(new ItemDto("植物の全体で均一に生えている"));
        child.add(new ItemDto("互い違いに生えている", R.drawable.icon_biop003));
        child.add(new ItemDto("左右対称に生えている", R.drawable.icon_biop003));
        child.add(new ItemDto("輪のように生えている", R.drawable.icon_biop003));
        child.add(new ItemDto("先端が丸い"));
        child.add(new ItemDto("先端が尖っている"));
        child.add(new ItemDto("縁が丸い"));
        child.add(new ItemDto("縁が波打っている"));
        child.add(new ItemDto("縁がギザギザ"));
        child.add(new ItemDto("ギザギザが葉の中程まで達している"));
        child.add(new ItemDto("ギザギザが葉の中心まで達している"));

        List<ItemDto> child2 = new ArrayList<ItemDto>();

        child2.add(new ItemDto("広がっている", R.drawable.icon_biop003));
        child2.add(new ItemDto("毛が生えている", R.drawable.icon_biop002));
        child2.add(new ItemDto("自立している", R.drawable.icon_biop002));
        child2.add(new ItemDto("他のものに巻きついている", R.drawable.icon_biop002));
        List<ItemDto> child3 = new ArrayList<ItemDto>();

        child3.add(new ItemDto("白色", R.drawable.icon_biop_map_no_title));
        child3.add(new ItemDto("黄色", R.drawable.icon_biop_map_no_title));
        child3.add(new ItemDto("淡紅色", R.drawable.icon_biop_map_no_title));
        child3.add(new ItemDto("褐色", R.drawable.icon_biop_map_no_title));
        child3.add(new ItemDto("青色", R.drawable.icon_biop_map_no_title));
        child3.add(new ItemDto("紫色", R.drawable.icon_biop_map_no_title));


        List<ItemDto> child4 = new ArrayList<ItemDto>();

        child4.add(new ItemDto("都市部", R.drawable.icon_biop_map_no_title));
        child4.add(new ItemDto("山間", R.drawable.icon_biop_map_no_title));
        child4.add(new ItemDto("森林", R.drawable.icon_biop_map_no_title));
        child4.add(new ItemDto("水辺", R.drawable.icon_biop_map_no_title));
        child4.add(new ItemDto("乾燥地帯", R.drawable.icon_biop_map_no_title));

        List<List<ItemDto>> result = new ArrayList<List<ItemDto>>();
        result.add(child);
        result.add(child2);
        result.add(child3);
        result.add(child4);

        return result;
    }
    private List<String> createGroupItemList() {
        List<String> groups = new ArrayList<String>();
        groups.add("葉の様子");
        groups.add("茎の様子");
        groups.add("花の色");
        groups.add("撮影した環境");
        return groups;
    }

}


