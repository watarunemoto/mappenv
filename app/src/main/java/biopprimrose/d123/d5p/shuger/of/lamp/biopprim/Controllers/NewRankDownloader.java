package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class NewRankDownloader extends AsyncTaskLoader<String>{
    private String mResult;
    private boolean mIsStarted = false;

    public NewRankDownloader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
            return;
        }
        if (!mIsStarted || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mIsStarted = true;
    }

    @Override
    public void deliverResult(String data) {
        mResult = data;
        super.deliverResult(data);
    }
}
//public class NewRankDownloader extends AsyncTask<String, String, String> {
//
//    public NewRankDownloader(){
//
//    }
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        // doInBackground前処理
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        HttpURLConnection con = null;
//        String urlStr = params[0];
//
//
//        // finally 内で利用するため try の前に宣言します。
//        InputStream inputStream = null;
//        try {
//            URL url = new URL(urlStr);
//            StringBuilder sb = new StringBuilder();
//
//            // URLの作成
//            con = (HttpsURLConnection)url.openConnection();
////            con.setConnectTimeout(3000); // タイムアウト 3 秒
////            con.setReadTimeout(3000);
//            con = (HttpURLConnection)url.openConnection();
//            // リクエストメソッドの設定
//            con.setRequestMethod("POST");
//            // リダイレクトを自動で許可しない設定
//            con.setInstanceFollowRedirects(false);
//            // URL接続からデータを読み取る場合はtrue
//            con.setDoInput(true);
//            // URL接続にデータを書き込む場合はtrue
//            con.setDoOutput(false);
//
//            // 接続
//            con.connect(); // ①
//            InputStream in = con.getInputStream();
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        // doInBackground後処理
//    }
//}
