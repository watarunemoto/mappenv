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
        child.add(new GetItemList("縁が波打っている"));
        child.add(new GetItemList("縁がギザギザ"));
        child.add(new GetItemList("ギザギザが葉の中程まで達している"));

        List<GetItemList> child2 = new ArrayList<GetItemList>();
        ;
        child2.add(new GetItemList("自立している"));
        child2.add(new GetItemList("茎に毛が生えている"));
        child2.add(new GetItemList("複数の茎を持つ"));
        List<GetItemList> child3 = new ArrayList<GetItemList>();

        child3.add(new GetItemList("紅色"));
        child3.add(new GetItemList("花弁が4枚程度"));
        child3.add(new GetItemList("雄しべがたくさんある"));




        List<List<GetItemList>> result = new ArrayList<List<GetItemList>>();
        result.add(child);
        result.add(child2);
        result.add(child3);

        return result;
    }

    private List<String> createGroupItemList() {
        List<String> groups = new ArrayList<String>();
        groups.add("葉の様子");
        groups.add("茎の様子");
        groups.add("花の様子");
        return groups;
    }

}


