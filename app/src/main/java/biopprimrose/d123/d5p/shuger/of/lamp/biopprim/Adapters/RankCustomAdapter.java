package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class RankCustomAdapter extends ArrayAdapter<String> {
	private LayoutInflater layoutInflater;
    Context context = null;

	public RankCustomAdapter(Context context,List<String> objects) {
		super(context,0,objects);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.list_item_rank, parent, false);
		} else {
			view = convertView;
		}


		String item = getItem(position);
		String[] data = item.split(":");
        String ranking_points = context.getString(R.string.ranking_points);
        String ranking_counts = context.getString(R.string.ranking_count);
        Log.v("テスト",item);

		Log.v("get_num",data+"");
		DecimalFormat df = new DecimalFormat("0.000000");
		//locationから緯度経度の取得
		String score = df.format(Double.parseDouble(data[3]));
		TextView rank = (TextView) view.findViewById(R.id.rank);
		rank.setText("No. " + data[0]);
		//rank.setTypeface(Typeface.createFromAsset(assets, "fonts/HuiFontP29.ttf"));
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(data[1]);
		//name.setTypeface(Typeface.createFromAsset(assets, "fonts/HuiFontP29.ttf"));
		TextView num = (TextView) view.findViewById(R.id.datanum);
//		num.setText(score);
        num.setText(ranking_counts + data[2]);
		//num.setTypeface(Typeface.createFromAsset(assets, "fonts/HuiFontP29.ttf"));
		TextView point = (TextView) view.findViewById(R.id.point);
//		point.setText(data[2]);
        point.setText(ranking_points + score);
		//point.setTypeface(Typeface.createFromAsset(assets, "fonts/HuiFontP29.ttf"));
		return view;
	}
}
