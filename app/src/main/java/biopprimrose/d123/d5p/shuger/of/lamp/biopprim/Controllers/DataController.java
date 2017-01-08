package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by amemiyaY on 2016/11/17.
 * This is database like data storage;
 * column splitter is ","
 * row splitter is "@"
 */
public class DataController {


    Context context;
    String[] datas;
    SharedPreferences sp;
    String name;


    public DataController(Context context, String name) {
        this.context = context;
        this.name = name;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        String tmp = sp.getString(name,"");
        if (!tmp.equals("")) {
            datas = tmp.split("@");
        }
    }

    public int init(String tablename, String[] columns){
        String output = "";

        for (int i = 0; i < columns.length; i++) {
            Log.v("data_cont",columns[i]);
            if (i != columns.length - 1) {
                output += columns[i] +",";
            } else {
                output += columns[i];
            }

        }

        output += "@";
        sp.edit().putString(tablename,output).apply();
        return 1;
    }

    public int alter(int num, String data) {
        String output = "";
        if (num > datas.length - 1) {
            return 0;
        }
        for (int i = 1; i < datas.length; i++) {
            if (i == num) {
                output += data +"@";
            } else if (i == datas.length - 1) {
                output += datas[i];
            } else {
                output += datas[i] + "@";
            }
        }
        sp.edit().putString(name,output).apply();
        return 1;
    }

    public String getalldata(){
        String output = "";
        if (datas != null) {
            for (int i = 1; i < datas.length; i++) {
                if (i == datas.length - 1) {
                    output += datas[i];
                } else {
                    output += datas[i] + "@";
                }
            }
        }
        return output;
    }

    public int insert(String data ) {
        String output = "";
        Log.v("datacontroller",data+"@"+datas[0]+"@"+data.split(",").length+"@"+datas[0].split(",").length);

        if (data.split(",").length != datas[0].split(",").length){
            return 0;
        }

        for (int i = 0; i < datas.length; i++) {
            output += datas[i]+"@";
        }
        output += data;
        Log.v("datacontroller",output);
        this.datas = output.split("@");
        sp.edit().putString(name,output).apply();
        return 1;
    }

    public int remove(int num) {
        String output = "";
        if (num > datas.length - 1) {
            return 0;
        }
        for (int i = 1; i < datas.length; i++) {
            if (i != num) {
                output += datas[i]+"@";
            } else if (i == datas.length - 1) {
                output += datas[i];
            }
        }
        sp.edit().putString(name,output).apply();
        return 1;
    }

    public String getdata(int num) {
        return datas[num];
    }

}
