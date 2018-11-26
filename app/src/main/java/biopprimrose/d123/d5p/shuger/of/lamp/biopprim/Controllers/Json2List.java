package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by amemiyaY on 2016/11/28.
 */
public class Json2List {

    private String json;

    public Json2List() {}

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


//    public List<HashMap<String,Object>> parseStringStringHashMap(String json) {
//        ArrayList<HashMap<String,Object>> hashmap = new ArrayList<>()>;
//        ArrayList hogehoge = new ArrayList();
//
//
//        if (json != null) {
//            Gson gson = new Gson();
//            List<Foo> obj = gson.fromJson(jsonText,new TypeToken<Foo<Bar>>())
//             hogehoge = gson.fromJson(json,ArrayList.class);
//            }
//
//            for (Object hoge : hogehoge) {
//            hashmap.add(hoge.);
//            }
//        }
//
//        return output;
//    }

    class Foo<T> {
    T value;
    }

//    class UserInfo {
//        private int Score;
//        private String userid;
//
//        public UserInfo(int Score, String userid) {
//            this.Score = Score;
//            this.userid = userid;
//        }
//
//    }
//
//    class gsonlist {
//        private Map hoge;
//        private List<UserInfo> output ;
//
//        public gsonlist(UserInfo userInfo){
//            this.hoge = hoge;
//            this.output = new ArrayList<userInfo>();
//    }
//    }

}
