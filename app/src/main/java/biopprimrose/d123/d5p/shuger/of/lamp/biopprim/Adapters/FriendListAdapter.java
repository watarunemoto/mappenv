package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.OnRecycleListener;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amemiyaY on 2016/11/17.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<FriendList> data;
    private Context context;
    private OnRecycleListener listener;

    public FriendListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public FriendListAdapter(Context context, List<FriendList> data, OnRecycleListener listener){
        this(context);
        this.data = data;
        this.listener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_friends,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.v("recycleradapter",position+"");

        if (data != null && data.size() > position && data.get(position) != null ) {
            if (data.get(position).getIsFrined().equals("1")) {
                holder.icon.setText(R.string.friend);
            } else {
                holder.icon.setText(R.string.notfriend);
            }
            holder.username.setText(data.get(position).getUsername());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecycleClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView icon;
        TextView username;
        public ViewHolder(View view) {
            super(view);
            icon = (TextView) view.findViewById(R.id.icon);
            username = (TextView) view.findViewById(R.id.username);
        }
    }
}
