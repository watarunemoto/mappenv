package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class RankViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView detailView;
    public RankViewHolder(View itemView){
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        detailView = (TextView) itemView.findViewById(R.id.detail);

    }



}
