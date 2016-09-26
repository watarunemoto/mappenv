package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Setting_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Settings_Fragment())
                .commit();
    }
}
