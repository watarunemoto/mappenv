package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers.IdPostTask;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;

/**
 * Commented by amemiya on 2017/01
 * 一番最初に表示されるログイン画面
 * 遷移先は、TitleActivtiyのみ
 * 最初であれば、ユーザー名を決めるダイアログが出現
 * それ以降は、そのままTitleActivtiyに遷移
 */


public class LoginActivity extends Activity {

    private EditText editView;

    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;
    public static boolean isOk = false;

    private String text = "";

    final android.os.Handler handler = new android.os.Handler();

    AlertDialog.Builder input_id_dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //テキストボックスの生成と入力制限
        editView = new EditText(LoginActivity.this);
        //テキストボックスにヒントを表示
        editView.setHint("userid");

        editView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /**
         * Edittextの入力制限
         * 16文字以内、英数字のみ、1行に表示
         * 40m文字にします tsuchiya
         */

        InputFilter inputFilter =
                new InputFilter() {
//                    dstart,dendは入力文字数
                    @Override
                    public CharSequence filter(
                            CharSequence source,
                            int start,
                            int end,
                            Spanned spanned,
                            int dstart,
                            int dend
                    ) {
                        if (source.toString().matches("[a-zA-Z0-9]"))
                        {
                            if (dend < 40) {
                                return source;
                            } else {
                                return "";
                            }
                        }
                        else {
                            return "";
                        }
                    }
                };



        InputFilter[] filter = {inputFilter};
        editView.setFilters(filter);
        editView.setMaxLines(1);


        /**
         * ログイン画面に、ロゴを表示
         */
        ImageView img = (ImageView) findViewById(R.id.login_img);
        img.setImageResource(R.drawable.login);

        //IDが決まっていれば5秒後にタイトル画面へ遷移
        if (PREFERENCE_INIT != getState()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(
                            LoginActivity.this,
                            TitleActivity.class
                    );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }, 5000);
        } else {
            makeAppShortCut(LoginActivity.this);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        input_dialog_set();
    }

    /**
     * 初期id入力ダイアログ
     */
    private void input_dialog_set() {
        final SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(
                        LoginActivity.this
                );
        input_id_dialog = new AlertDialog.Builder(this);
        final AlertDialog.Builder recheck_dialog = new AlertDialog.Builder(this);

        final String your_id = getResources().getString(R.string.label_id);
        
        if (PREFERENCE_INIT == getState()) {
            input_id_dialog
                    .setTitle(R.string.set_id)
                    .setMessage(R.string.label_inputidrequest)
                    .setView(editView)
                    .setPositiveButton(R.string.ok_dialog,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setState(
                                            PREFERENCE_BOOTED,
                                            editView.getText().toString().trim()
                                    );
                                    recheck_dialog.setTitle(R.string.your_id_ok)
                                            .setMessage(your_id + editView.getText().toString())
                                            .setPositiveButton(
                                                    R.string.ok_dialog,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialogInterface, int i
                                                        ) {

                                                            isOk = true;
                                                            String your_id_is =
                                                                    getResources().getString(R.string.your_id_is);

                                                            Toast.makeText(
                                                                    LoginActivity.this,
                                                                    your_id_is + editView.getText().toString(),
                                                                    Toast.LENGTH_LONG)
                                                                    .show();

                                                            IdPostTask hit =
                                                                    new IdPostTask(sp);

                                                            hit.execute(
                                                                    UrlCollections.URL_USERNAME_UPLOAD,
                                                                    editView.getText().toString()
                                                            );

                                                        }
                                                    }).setNegativeButton(
                                            R.string.no_dialog,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                    recheck_dialog
                                            .create()
                                            .show();
                                }
                            });


            input_id_dialog
                    .create()
                    .show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (input_id_dialog != null ){

            input_id_dialog = null;
        }
    }

    private void setState(int state, String id) {
        SharedPreferences sp =
                PreferenceManager.
                        getDefaultSharedPreferences(
                                LoginActivity.this
                        );
        sp.edit()
                .putInt(
                        "InitState", state
                ).apply();
        sp.edit()
                .putString(
                        "MyID", id
                ).apply();
    }

    private int getState() {
        int state;
        SharedPreferences sp =
                PreferenceManager
                        .getDefaultSharedPreferences(
                                LoginActivity.this
                        );
        state = sp
                .getInt(
                        "InitState",
                        PREFERENCE_INIT
                );
        return state;
    }

    /**
     * タッチTitleActivityにしたら遷移
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(
                LoginActivity.this,
                TitleActivity.class
        );
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );
        startActivity(intent);
        return true;
    }

    /**
     * バックボタンを押したら
     * handlerを止めて
     * バックタスクにアプリケーションを遷移
     */
    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        moveTaskToBack(true);
    }



    private void makeAppShortCut(Context context){
//         アプリケーションを起動するためのIntentを作成
        Intent targetIntent = new Intent(Intent.ACTION_MAIN);
        targetIntent.setClassName(LoginActivity.this, "biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.LoginActivity");
        makeShortCut(context, targetIntent, getString(R.string.app_name), R.drawable.mark);
    }

    private void makeShortCut(Context context, Intent targetIntent, String title, int iconResource){
        // ショートカット作成依頼のためのIntent
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // ショートカットのタップ時に起動するIntentを指定
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);

        //これをつけておかないと、すでにホームアイコンがあっても、また作成してしまう
        intent.putExtra("duplicate", false);

        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconResource);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);

        // BroadCastを使って、システムにショートカット作成を依頼する
        context.sendBroadcast(intent);
    }


}
