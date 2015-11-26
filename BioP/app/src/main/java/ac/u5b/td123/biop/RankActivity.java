package ac.u5b.td123.biop;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class RankActivity extends Activity {
	ListView listView;
	Activity activity;

	//ネットワークの状態を取得する
	ConnectivityManager cm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		activity = this;

		listView = (ListView) findViewById(R.id.myListView);

		//ネットワークの状態の取得関連
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		Log.v("net",cm.toString());
	}

	public void btn34(View view) {
		NetworkInfo nwi = cm.getActiveNetworkInfo();
		if (nwi != null) {
			if (nwi.isConnected()) {
				RankingGeter rg = new RankingGeter(activity, listView);
				rg.execute("http://133.14.168.53/gacDojisafvesveywo.php");
			} else {
				Toast.makeText(this, "No Network Connection!", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "No Network Connection!", Toast.LENGTH_LONG).show();
		}
	}
}
