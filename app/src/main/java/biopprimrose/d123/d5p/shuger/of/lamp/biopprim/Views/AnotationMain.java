package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anotation_main);

        ExpandableListView listView = (ExpandableListView)findViewById(R.id.sample_list);
        int[] rowId = {0,1,2};
        listView.setAdapter(new AnotationListAdaptor(this, rowId, createGroupItemList(), createChildrenItemList()));
    }

    /**
     *
     * @return
     */
    private List<List<ItemDto>> createChildrenItemList() {
        List<ItemDto> child = new ArrayList<ItemDto>();
        child.add(new ItemDto("ねこ１"));
        child.add(new ItemDto("ねこ２"));
        child.add(new ItemDto("コーギー１"));
        child.add(new ItemDto("コーギー２"));
        child.add(new ItemDto("しばいぬ１"));

        List<ItemDto> child2 = new ArrayList<ItemDto>();
        child2.add(new ItemDto("猫１", R.drawable.icon_biop_map_no_title));
        child2.add(new ItemDto("猫２", R.drawable.icon_biop004));
        child2.add(new ItemDto("コーギー１", R.drawable.icon_biop003));
        child2.add(new ItemDto("コーギー２", R.drawable.icon_biop002));
        child2.add(new ItemDto("柴犬", R.drawable.icon_biop001));

        List<List<ItemDto>> result = new ArrayList<List<ItemDto>>();
        result.add(child);
        result.add(child2);

        return result;
    }

    /**
     *
     * @return
     */
    private List<String> createGroupItemList() {
        List<String> groups = new ArrayList<String>();
        groups.add("葉っぱの様子");
        groups.add("茎の様子");
        groups.add("撮影した環境");
        return groups;
    }
}

//public class AnotationMain extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}
