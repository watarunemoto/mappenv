package ac.u5b.td123.biop;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.logging.Handler;


public class LoginActivity extends Activity {

	EditText editView;

	public static final int PREFERENCE_INIT = 0;
	public static final int PREFERENCE_BOOTED = 1;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//テキストボックスの生成と入力制限
		editView = new EditText(LoginActivity.this);
		//ヒント
		editView.setHint("userid");
		//入力制限
		editView.setMaxLines(1);
		InputFilter inputFilter =
				new InputFilter() {
					//dstart,dendは入力文字数
					@Override
					public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int dstart, int dend) {
						if (source.toString().matches("^[a-zA-Z0-9]$")) {
							if (dend < 16) {
								return source;
							} else {
								return "";
							}
						} else {
							return "";
						}
					}
				};
		InputFilter[] filter = {inputFilter};
		editView.setFilters(filter);

		ImageView img = (ImageView) findViewById(R.id.login_img);
		img.setImageResource(R.drawable.login);
	}

	@Override
	protected void onResume() {
		super.onResume();
		alert_set();
	}

	private void alert_set() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog
				.setTitle("IDを設定します")
				.setMessage("IDを入力してください")
				.setView(editView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						setState(PREFERENCE_BOOTED, editView.getText().toString().trim());
						Toast.makeText(LoginActivity.this, "あなたのIDは" + editView.getText().toString() + "です", Toast.LENGTH_LONG).show();

					}
				});
		if (PREFERENCE_INIT == getState()) {
			alertDialog.create();
			alertDialog.show();
		}
	}

	private void setState(int state, String id) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		sp.edit().putInt("InitState", state).apply();
		sp.edit().putString("MyID", id).apply();
	}

	private int getState() {
		int state;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		state = sp.getInt("InitState", PREFERENCE_INIT);
		return state;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(LoginActivity.this, TitleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	}

	@Override
	public void onBackPressed() {
		//とりあえずactivityをバックグラウンドにもっていく
		moveTaskToBack(true);
	}
}
