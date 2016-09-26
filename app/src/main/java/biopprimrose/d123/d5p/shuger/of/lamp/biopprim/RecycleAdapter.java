package biopprimrose.d123.d5p.shuger.of.lamp.biopprim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amemiyaY on 2016/05/07.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> data;
    private Context context;
    private OnRecycleListener listener;

    public RecycleAdapter(Context context, ArrayList<String> data, OnRecycleListener listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_title_recycle,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (data != null && data.size() > position && data.get(position) != null ) {
            holder.textView.setText(data.get(position));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecycleClicked(view,position);
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
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.title_recycle_text);
        }
    }
}
