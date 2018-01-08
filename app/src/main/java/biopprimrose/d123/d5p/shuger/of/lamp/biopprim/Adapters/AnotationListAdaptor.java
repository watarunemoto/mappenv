package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    /**
     * Constructor
     */
    public AnotationListAdaptor(Context ctx, int[] rowId, List<String> groups, List<List<ItemDto>> children) {
        this.context = ctx;
        this.groups = groups;
        this.children = children;
        this.rowId = rowId;
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
        TextView textView = (TextView)childView.findViewById(R.id.member_list);
        ItemDto dto  = children.get(arg0).get(arg1);
        textView.setText(dto.getName());

        Drawable icon = context.getResources().getDrawable(dto.getResourceId());
        int width = (int)(48 * context.getResources().getDisplayMetrics().density + 0.5f);
        icon.setBounds(0, 0, width, width);
        textView.setCompoundDrawables(icon, null, null, null);

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

