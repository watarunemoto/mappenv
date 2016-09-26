package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.Hash_id_transfar;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;


public class LoginActivity extends Activity {

    EditText editView;

    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;
    public static boolean isOk = false;
    Context context;

    final android.os.Handler handler = new android.os.Handler();

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

        //IDが決まっていれば5秒後にタイトル画面へ遷移
        if (PREFERENCE_INIT != getState()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, TitleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }, 5000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        alert_set();
    }

    private void alert_set() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);

        final String message_1 = getResources().getString(R.string.id_dialog);

        if (PREFERENCE_INIT == getState()) {
            alertDialog
                    .setTitle(R.string.set_id)
                    .setMessage(R.string.input_id)
                    .setView(editView)
                    .setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setState(PREFERENCE_BOOTED, editView.getText().toString().trim());
                            alertDialog2.setTitle(R.string.your_id_ok)
                                    .setMessage(message_1 + editView.getText().toString())
                                    .setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            isOk = true;
                                            String your_id_is = getResources().getString(R.string.your_id_is);
                                            Toast.makeText(LoginActivity.this, your_id_is + editView.getText().toString(), Toast.LENGTH_LONG).show();
                                            Hash_id_transfar hit = new Hash_id_transfar(sp);
                                            hit.execute(UrlCollections.URL_USERNAME_UPLOAD, editView.getText().toString());
                                        }
                                    }).setNegativeButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            alertDialog2.create();
                            alertDialog2.show();
                        }
                    });
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
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(LoginActivity.this, TitleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        //とりあえずactivityをバックグラウンドにもっていく
        handler.removeCallbacksAndMessages(null);
        moveTaskToBack(true);
    }


}
