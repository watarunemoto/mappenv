package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.NoUploadActivity;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views.PhotoResultActivity;


/**
 * Created by amemiyaY on 2016/01/26.
 */
public class TabListview extends TabActivity  {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_listview);


         // create the TabHost that will contain the Tabs
         TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

         TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
         TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");

         // Set the Tab name and Activity
         // that will be opened when particular Tab will be selected
         String photo_list = getResources().getString(R.string.photo_list);
         String no_upload_list = getResources().getString(R.string.no_upload_list);

         tab1.setIndicator(photo_list);
         tab1.setContent(new Intent(this, PhotoResultActivity.class));

         tab2.setIndicator(no_upload_list);
         tab2.setContent(new Intent(this, NoUploadActivity.class));

         //Add the tabs  to the TabHost to display.
         tabHost.setup();

         tabHost.addTab(tab1);
         tabHost.addTab(tab2);

    }

}
