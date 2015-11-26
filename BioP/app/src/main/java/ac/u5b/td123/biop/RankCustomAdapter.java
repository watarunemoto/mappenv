package ac.u5b.td123.biop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by amimeyaY on 2015/11/05.
 */
public class RankCustomAdapter extends ArrayAdapter<String> {
	private LayoutInflater layoutInflater;

	public RankCustomAdapter(Context context,List<String> objects) {
		super(context,0,objects);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		DecimalFormat df = new DecimalFormat("0.000000");
		//locationから緯度経度の取得
		String score = df.format(Double.parseDouble(data[3]));
		TextView rank = (TextView) view.findViewById(R.id.rank);
		rank.setText(data[0]+"位");
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(data[1]);
		TextView num = (TextView) view.findViewById(R.id.datanum);
		num.setText("Data" + data[2]+"個");
		TextView point = (TextView) view.findViewById(R.id.point);
		point.setText(score+"ポイント");
		return view;
	}
}
