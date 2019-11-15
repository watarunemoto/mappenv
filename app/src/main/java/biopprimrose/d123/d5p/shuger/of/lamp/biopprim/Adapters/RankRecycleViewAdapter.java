package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.RankViewHolder;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class RankRecycleViewAdapter extends RecyclerView.Adapter<RankViewHolder> {

    private List<RankRecyclerRow> list;

    public RankRecycleViewAdapter(List<RankRecyclerRow> list) {
        this.list = list;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_rankcategory_row, parent,false);
        RankViewHolder vh = new RankViewHolder(inflate);
        return vh;

    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.detailView.setText(list.get(position).getDetail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCardInfoList(List<HashMap<String,String>> newdata) {
        List<RankRecyclerRow> dataset = new ArrayList<>();
        for (HashMap<String, String> map : newdata) {
            RankRecyclerRow data = new RankRecyclerRow();
            double prescore = Double.parseDouble(map.get("Score"));
            int score = (int)prescore;
            data.setTitle("UserId : " + map.get("userid"));
//            data.setDetail("Score : " +map.get("Score"));
            data.setDetail("Score :" + Integer.toString(score));
            Log.d("kaesardata", data.toString());
            dataset.add(data);
        }
        this.list = dataset;
    }

}
