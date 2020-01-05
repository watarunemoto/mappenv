package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class RankListRecycleViewAdapter extends RecyclerView.Adapter<RankListRecycleViewAdapter.RankListRecycleViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RankListRecycleViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public View rv;
        public TextView ranklist_titleView;
        public RankListRecycleViewHolder(View v) {
            super(v);
            rv = v;
            ranklist_titleView = (TextView) itemView.findViewById(R.id.ranklist_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RankListRecycleViewAdapter(String[] myDataset) {
        mDataset = myDataset;
    }
    protected void onRankListClicked(@NonNull String onrank) {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RankListRecycleViewAdapter.RankListRecycleViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        Log.d("RankListRecycleVA","oncreateviewholder");
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.recycle_ranklist_row, parent, false);
        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ranklist_row, parent, false);
        final RankListRecycleViewHolder vh = new RankListRecycleViewHolder(v2);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vh.getAdapterPosition();
                String kurage = mDataset[position];
                onRankListClicked(kurage);
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RankListRecycleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.setText(mDataset[position]);
        holder.ranklist_titleView.setText(mDataset[position]);
        Log.d("RankListRecycleVA",mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
