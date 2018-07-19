package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.AnnotationListAdaptor;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.GetItemList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class AnnotationActivity extends AppCompatActivity {
    /** Called when the activity is first created. */


//    static String annotation;
    static String annotation;


    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    public String getAnnotation() {
        return annotation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_annotation_main);


        Button button1 = (Button) findViewById(R.id.AnnoToCamera);
        annotation = "";

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Annotation", annotation);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        ExpandableListView listView = (ExpandableListView)findViewById(R.id.sample_list);
        int[] rowId = {0,1,2};

        listView.setAdapter(new AnnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList(), AnnotationBooleanlist()));
    }


    //戻るボタンのイベントを拾う
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("Annotation", annotation);
            setResult(RESULT_OK, intent);
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }



    private ArrayList<List<Boolean>> AnnotationBooleanlist() {
        ArrayList<List<Boolean>> check = new ArrayList<>();
        for (int i = 0 ; i < createChildrenItemList().size(); i++){
            check.add(new ArrayList<Boolean>());
            for (int j = 0 ; j < createChildrenItemList().get(i).size(); j++){
                check.get(i).add(j,false);
            }
        }
        return check;
    }

    private List<List<GetItemList>> createChildrenItemList() {
        List<GetItemList> child = new ArrayList<GetItemList>();
        child.add(new GetItemList("植物の上部に多く生えている"));
        child.add(new GetItemList("植物の下部に多く生えている"));
        child.add(new GetItemList("植物の全体で均一に生えている"));
        child.add(new GetItemList("互い違いに生えている"));
        child.add(new GetItemList("左右対称に生えている"));
        child.add(new GetItemList("輪のように生えている"));
        child.add(new GetItemList("先端が丸い"));
        child.add(new GetItemList("先端が尖っている"));
        child.add(new GetItemList("縁が丸い"));
        child.add(new GetItemList("縁が波打っている"));
        child.add(new GetItemList("縁がギザギザ"));
        child.add(new GetItemList("ギザギザが葉の中程まで達している"));
        child.add(new GetItemList("ギザギザが葉の中心まで達している"));

        List<GetItemList> child2 = new ArrayList<GetItemList>();

        child2.add(new GetItemList("広がっている"));
        child2.add(new GetItemList("毛が生えている"));
        child2.add(new GetItemList("自立している"));
        child2.add(new GetItemList("他のものに巻きついている"));
        List<GetItemList> child3 = new ArrayList<GetItemList>();

        child3.add(new GetItemList("白色"));
        child3.add(new GetItemList("黄色"));
        child3.add(new GetItemList("淡紅色"));
        child3.add(new GetItemList("褐色"));
        child3.add(new GetItemList("青色"));
        child3.add(new GetItemList("紫色"));


        List<GetItemList> child4 = new ArrayList<GetItemList>();

        child4.add(new GetItemList("都市部"));
        child4.add(new GetItemList("山間"));
        child4.add(new GetItemList("森林"));
        child4.add(new GetItemList("水辺"));
        child4.add(new GetItemList("乾燥地帯"));

        List<List<GetItemList>> result = new ArrayList<List<GetItemList>>();
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


