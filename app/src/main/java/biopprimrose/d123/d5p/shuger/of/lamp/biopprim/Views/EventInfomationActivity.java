package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class EventInfomationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_infomation);

        Intent intent = getIntent();

        String About = intent.getStringExtra("AboutEvent");

        TextView abouttext = findViewById(R.id.abouttext);


        abouttext.setText(About);
    }
}
