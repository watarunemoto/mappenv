package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.RankActivity;


public class TabRanking extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_ranking);

		// create the TabHost that will contain the Tabs
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

		TabHost.TabSpec tab1 = tabHost.newTabSpec("1");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		String ranking = getResources().getString(R.string.rankings);


		tab1.setIndicator(ranking);
		tab1.setContent(new Intent(this, RankActivity.class));

		/** Add the tabs  to the TabHost to display. */
		tabHost.setup();
		tabHost.addTab(tab1);
	}
}
