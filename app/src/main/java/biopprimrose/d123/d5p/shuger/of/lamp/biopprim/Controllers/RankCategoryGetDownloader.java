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


        IndividualRankDownloader downloader = new IndividualRankDownloader();
        return downloader.GetInformation(mURI);

    }
}
