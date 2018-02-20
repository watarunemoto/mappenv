package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.AnotationListAdaptor;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.ItemDto;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class AnotationMain extends AppCompatActivity {
    /** Called when the activity is first created. */
//    int PARENT_DATA = 3;
//    int CHILD_DATA = 3



    static String anotation;
    int counts;
//    SharedPreferences sp = getSharedPreferences("Annotation", Context.MODE_PRIVATE);
//    public String aaa = "aaa";

    public void setAnotation(String anotation) {
        this.anotation = anotation;
    }

    public String getAnotation() {
        return anotation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anotation_main);


//        final Intent intent = getIntent();
//        anotation = "hhogheohijehoijoij";


        Resources res = getResources();
        String initialcom = res.getString(R.string.anotxt);
        Button button1 = (Button) findViewById(R.id.AnoToCamera);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("Annotation", anotation);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        ExpandableListView listView = (ExpandableListView)findViewById(R.id.sample_list);
        int[] rowId = {0,1,2};
//        listView.setAdapter(new AnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList()));
        listView.setAdapter(new AnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList(),buildTestData()));
//
    }

     private ArrayList<List<Boolean>> buildTestData() {

         List<Boolean> check1 = new ArrayList<Boolean>();
         List<Boolean> check2 = new ArrayList<Boolean>();
         List<Boolean> check3 = new ArrayList<Boolean>();
         List<Boolean> check4 = new ArrayList<Boolean>();

         ArrayList<List<Boolean>> check = new ArrayList<>();

         check.add(check1);
         check.add(check2);
         check.add(check3);
         check.add(check4);

         for (int i = 0 ; i < createChildrenItemList().size(); i++){
             for (int j = 0 ; j < createChildrenItemList().get(i).size(); j++){
                 check.get(i).add(j,false);
             }

         }




         return check;
    }




    private List<List<ItemDto>> createChildrenItemList() {
        List<ItemDto> child = new ArrayList<ItemDto>();
        child.add(new ItemDto("植物の上部に多く生えている"));
        child.add(new ItemDto("植物の下部に多く生えている"));
        child.add(new ItemDto("植物の全体で均一に生えている"));
        child.add(new ItemDto("互い違いに生えている"));
        child.add(new ItemDto("左右対称に生えている"));
        child.add(new ItemDto("輪のように生えている"));
        child.add(new ItemDto("先端が丸い"));
        child.add(new ItemDto("先端が尖っている"));
        child.add(new ItemDto("縁が丸い"));
        child.add(new ItemDto("縁が波打っている"));
        child.add(new ItemDto("縁がギザギザ"));
        child.add(new ItemDto("ギザギザが葉の中程まで達している"));
        child.add(new ItemDto("ギザギザが葉の中心まで達している"));

        List<ItemDto> child2 = new ArrayList<ItemDto>();

        child2.add(new ItemDto("広がっている"));
        child2.add(new ItemDto("毛が生えている"));
        child2.add(new ItemDto("自立している"));
        child2.add(new ItemDto("他のものに巻きついている"));
        List<ItemDto> child3 = new ArrayList<ItemDto>();

        child3.add(new ItemDto("白色"));
        child3.add(new ItemDto("黄色"));
        child3.add(new ItemDto("淡紅色"));
        child3.add(new ItemDto("褐色"));
        child3.add(new ItemDto("青色"));
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


