package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.util.Log;

public class RankCategoryGetDownloader extends AsyncTaskAbstract {
    private String mURI;
    private String Requestbody;
    private String Requestkey;



    public RankCategoryGetDownloader(Context context, String extraParam) {
        super(context);
        mURI = extraParam;

    }

    @Override
    public String loadInBackground() {

        Log.d("SampleLoader", "loadInBackground");
        Log.d("muri",mURI);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {

        }

//        HttpURLConnection connection = null;
//        String urlStr = mURI[0];
//        Log.d(urlStr,"Urlstr");
//
//
//        StringBuilder sb = new StringBuilder();
//        try {
//            URL url = new URL(urlStr);
//            connection = (HttpURLConnection) url.openConnection();
//            InputStream is = connection.getInputStream();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String line = "";
//            while ((line = reader.readLine()) != null)
//                sb.append(line);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally{
//            connection.disconnect();
//        }
//        Log.d(sb.toString(),"Stringbuilder");
//
//
////        return "task complete: " + mURI + sb.toString();
//        try {
//            return sb.toString();
//        }catch(NullPointerException e) {
//            Log.d("ぬるぽ","ぬるぽ");
//            return null;
//        }

        IndividualRankDownloader downloader = new IndividualRankDownloader();
        return downloader.GetInformation(mURI);

    }
}
