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

import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/12/03.
 */
public class FriendListViewAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater layoutInflater = null;
    private List<FriendList> friendLists;

    public FriendListViewAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setFriendLists(List<FriendList> friendLists) {
        this.friendLists = friendLists;
    }

    public void addfriendLists(List<FriendList> friendLists) {
        this.friendLists.addAll(friendLists);

    }

    public void removeOtherUsersListitem() {
        this.friendLists.remove(friendLists.size() - 1);

    }

    public long getAdapterSize(){
        return this.friendLists.size();
    }
    @Override
    public int getCount() {
        return friendLists.size();
    }

    @Override
    public Object getItem(int i) {
        return friendLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return friendLists.get(i).get_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_friends, viewGroup, false);
        }
        if (friendLists.get(i).getIsFrined()== null){
            return null;
        }


        TextView icon = (TextView) view.findViewById(R.id.icon);
        TextView username = (TextView) view.findViewById(R.id.username);

        Log.v("database_",friendLists.get(i).getIsFrined());

        if (friendLists.get(i).getIsFrined().equals("1\n") || friendLists.get(i).getIsFrined().equals("1") ) {
            icon.setText(R.string.friend);
        } else if (friendLists.get(i).getIsFrined().equals("0\n") ||friendLists.get(i).getIsFrined().equals("0") )  {
            icon.setText(R.string.notfriend);
        }else if (friendLists.get(i).getIsFrined().equals("2\n") ||friendLists.get(i).getIsFrined().equals("2")) {
            icon.setText(R.string.requested);
        }
        username.setText(friendLists.get(i).getUsername());

        return view;
    }
}
