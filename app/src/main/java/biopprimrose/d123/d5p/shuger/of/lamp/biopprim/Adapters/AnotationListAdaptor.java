package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.ItemDto;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by tsuchiya on 2017/12/21.
 */

public class AnotationListAdaptor extends BaseExpandableListAdapter {

    private List<String> groups;
    private List<List<ItemDto>> children;
    private Context context = null;
    private int[] rowId;

    private List<Boolean> items;




    /**
     * Constructor
     */
    public AnotationListAdaptor(Context ctx, int[] rowId, List<String> groups, List<List<ItemDto>> children,List<Boolean> items) {
        this.context = ctx;
        this.groups = groups;
        this.children = children;
        this.rowId = rowId;
        this.items = items;
    }

    /**
     *
     * @return
     */
    public View getGenericView() {
        // xmlをinflateしてViewを作成する
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_anotationlist, null);
        return view;
    }



    public TextView getGroupGenericView() {
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(param);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(64, 0, 0, 0);

        return textView;
    }


    public int getRowId(int groupPosition) {
        return rowId[groupPosition];
    }


    @Override
    public Object getChild(int arg0, int arg1) {
        return children.get(arg0).get(arg1);
    }

    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return arg1;
    }
    @Override
    public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
                             ViewGroup arg4) {
        // 子供のViewオブジェクトを作成
        View childView = getGenericView();
//        if (arg3 == null) {
//            Log.i("MultipleChoiceListActivity", "constructing a new view for " + String.valueOf(arg1) + "-th list row");
//            View childView = getGenericView();
//            childView = LayoutInflater.from(context).inflate(R.layout.list_item_anotationlist, null);
//        } else {
//            Log.i("MultipleChoiceListActivity", "reusing the view for " + String.valueOf(arg1) + "-th list row");
//        }



        TextView textView = (TextView)childView.findViewById(R.id.member_list);
        ItemDto dto  = children.get(arg0).get(arg1);
        textView.setText(dto.getName());
//        final CheckBox chkBox = (CheckBox)childView.findViewById(R.id.checkbox1);
//        final int p = arg1;
//
////        chkBox.setText(dto.getName());
//        chkBox.setOnCheckedChangeListener(null);
////        chkBox.setOnCheckedChangeListener(null);
//        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                if (isChecked) {
////                    chkBox.setText("aaa");
////                } else {
////                    chkBox.setText("bbb");
////                }
//                items.set(p , isChecked);
//            }
//        });
//        chkBox.setChecked(items.get(arg1));
//

        return childView;
    }

    @Override
    public int getChildrenCount(int arg0) {
        return children.get(arg0).size();
    }

    @Override
    public Object getGroup(int arg0) {
        return groups.get(arg0);
    }

    @Override
    public int getGroupCount() {
        return children.size();
    }

    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }

    @Override
    public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
        TextView textView = getGroupGenericView();
        textView.setText(getGroup(arg0).toString());
        return textView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}



//        CheckBox checkBox = (CheckBox)childView.findViewById(R.id.checkbox);
//        checkBox.setText(dto.getName());

//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            // チェックボックスがクリックされた時に呼び出されます
//            public void onClick(View v) {
//                CheckBox checkBox = (CheckBox) v;
//                // チェックボックスのチェック状態を取得します
//                boolean checked = checkBox.isChecked();
//            }
//        });

