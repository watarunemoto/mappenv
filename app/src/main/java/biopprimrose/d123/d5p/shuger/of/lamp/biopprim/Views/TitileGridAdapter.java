package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;


/**
 * Created by amemiyaY on 2016/01/28.
 */
public class TitileGridAdapter extends ArrayAdapter<String> {

	private LayoutInflater layoutInflater;
	private Resources resources;
	private AssetManager assets;

	public TitileGridAdapter(Context context,List<String> objects,Resources resources,AssetManager assets) {
		super(context,0,objects);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resources = resources;
		this.assets = assets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		//シアン
		int blue = Color.rgb(188, 226, 232);
		//薄緑
		int grenn = Color.rgb(190, 210, 187);
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.gird_title_item, parent, false);
		} else {
			view = convertView;
		}

		String data = getItem(position);
		String[] grid_data = data.split(",");
		ImageView imageView = (ImageView) view.findViewById(R.id.image_title);
		imageView.setImageBitmap(BitmapFactory.decodeResource(resources, Integer.parseInt(grid_data[0])));
		TextView textView = (TextView) view.findViewById(R.id.text_title);
		//textView.setTypeface(Typeface.createFromAsset(assets, "fonts/HuiFontP29.ttf"));
		textView.setTextSize(28);
		textView.setText(grid_data[1]);
		if (position == 0 || position == 3) {
			view.setBackgroundColor(blue);
		} else {
			view.setBackgroundColor(grenn);
		}
		return view;
	}
}
