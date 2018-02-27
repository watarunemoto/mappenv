package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.GetItemList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.AnnotationActivity;

/**
 * Created by tsuchiya on 2017/12/21.
 */

public class AnnotationListAdaptor extends BaseExpandableListAdapter {

    private List<String> groups;
    private List<List<GetItemList>> children;
    private Context context = null;
    private int[] rowId;

    private ArrayList<List<Boolean>> items;
    private String addition;

    String anos =  "記録した特徴 : ";
    private Toast t;


    public AnnotationListAdaptor(Context ctx, int[] rowId, List<String> groups, List<List<GetItemList>> children, ArrayList<List<Boolean>> items) {
        this.context = ctx;
        this.groups = groups;
        this.children = children;
        this.rowId = rowId;
        this.items = items;
    }

    public View getGenericView() {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_annotationlist, null);
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
        View childView = getGenericView();
        TextView textView = (TextView)childView.findViewById(R.id.member_list);
        GetItemList GIL  = children.get(arg0).get(arg1);
        textView.setText(GIL.getName());
        final String name = GIL.getName();
        final int p = arg0;
        final int q = arg1;
        final CheckBox chkBox = (CheckBox)childView.findViewById(R.id.checkbox1);

        chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                // チェックボックスのチェック状態を取得します
                boolean checked = checkBox.isChecked();
                if (checked) {
                    if (anos == null|| anos.length() == 0 ) {
                        anos = anos + "記録した特徴 : ";
                    }
                    if (anos.equals("記録した特徴 : ")) {
                        anos = anos + name + "";
                    }else{
                        anos = anos + "、" + name ;
                    }
                    if(t != null) {
                        t.cancel();
                    }
                    t = Toast.makeText(context, "特徴を記録しました\n"+ anos, Toast.LENGTH_LONG);
                    t.show();

                    addition = anos.replaceAll("記録した特徴 : ","");
                    AnnotationActivity setanos = new AnnotationActivity();
//                    AnnotationActivity getanos = new AnnotationActivity();
                    setanos.setAnnotation(addition);

                } else {
                    String hoge = name;
                    anos = anos.replaceAll("、" + hoge + "、", "、");
                    anos = anos.replaceAll(hoge+"、", "");
                    anos = anos.replaceAll("、" + hoge, "");
                    anos = anos.replaceAll(hoge, "");
                    addition = anos.replaceAll("記録した特徴 : ","");

                    if (t != null) {
                        t.cancel();
                    }
                    if (anos.equals("記録した特徴 : ")) {
                        t = Toast.makeText(context, "キャンセルしました\n" + anos + "なし", Toast.LENGTH_LONG);
                    } else{
                        t = Toast.makeText(context, "キャンセルしました\n" + anos, Toast.LENGTH_LONG);
                    }

                    AnnotationActivity setanos = new AnnotationActivity();
                    setanos.setAnnotation(addition);
//                    AnnotationActivity getanos = new AnnotationActivity();
                    t.show();
                }
            }
        });


        chkBox.setOnCheckedChangeListener(null);
        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                items.get(p).set(q,isChecked);
            }
        });
        chkBox.setChecked(items.get(arg0).get(arg1));


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

