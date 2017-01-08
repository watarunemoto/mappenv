package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amemiyaY on 2016/11/28.
 */
public class Json_to_list {

    private String json;

    public Json_to_list() {}

    public HashMap<String,String> parse(String json) {

        HashMap<String,String> output = new HashMap<>();



        if (json != null) {
            json = json.replace("{", "").replace("[", "").replace("}", "").replace("]", "");

            String[] _json = json.split(",");
            for (String ele :_json) {
                ele = ele.replace('"',' ');
                ele = ele.replaceAll(" ","");
                String[] tmp = ele.split(":");

                if (tmp.length == 2) {
                    output.put(tmp[0], tmp[1]);
                    Log.v("tmp",tmp[0] + "," + tmp[1]);
                }
            }
        }
        return output;
    }
}
