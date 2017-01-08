package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/11/07.
 */
public class OtherUsersListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater layoutInflater = null;
    List<OtherUsersList> otherUsersLists;

    public OtherUsersListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOtherUsersLists(List<OtherUsersList> otherUsersLists) {
        this.otherUsersLists = otherUsersLists;
    }

    public void addOtherUsersLists(List<OtherUsersList> otherUsersLists) {
        this.otherUsersLists.addAll(otherUsersLists);

    }

    public void removeOtherUsersListitem() {
        this.otherUsersLists.remove(otherUsersLists.size() - 1);

    }

    public long getAdapterSize(){
        return this.otherUsersLists.size();
    }

    @Override
    public int getCount() {
        return otherUsersLists.size();
    }

    @Override
    public Object getItem(int i) {
        return otherUsersLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return otherUsersLists.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_other_users, viewGroup, false);
        }

        ((TextView) view.findViewById(R.id.detected_result)).setText(otherUsersLists.get(i).getName());
        ((ImageView) view.findViewById(R.id.icon)).setImageBitmap(BitmapFactory.decodeFile(otherUsersLists.get(i).getFilepath()));

        return view;
    }
}
