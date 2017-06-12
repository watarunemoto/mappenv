package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.UrlCollections;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by amemiyaY on 2016/11/17.
 */
public class FriendRequestTask extends AsyncTask<String, Void, Void> {

    String result;

    private Context context;
    private String userid1;
    private String userid2;
    private String jobid = null;
    private int position = 0;
    private List<FriendList> list;

    public FriendRequestTask(Context context, String userid1) {
        this.context = context;
        this.userid1 = userid1;
    }

    public FriendRequestTask(Context context, String userid1, String userid2) {
        this(context, userid1);
        this.userid2 = userid2;
    }

    public FriendRequestTask(Context context, String userid1, String userid2, String jobid, int position) {
        this(context, userid1, userid2);
        this.jobid = jobid;
        this.position = position;
    }

    public FriendRequestTask(Context context, String userid1, List<FriendList> list ) {
        this(context, userid1);
        this.list = list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (result == null) {
            return;
        }

        result = result.replace("{", "").replace("[", "").replace("}", "").replace("]", "");

        Log.v("result", result);
        Map<String, String> map = new Json2List().parse(result);

        result = result.replace('"', ' ');
        result = result.replaceAll(" ", "");
        FriendResponse fr = new FriendResponse();
        Log.v("result", result);

        if (position >= 0) {
            result = result +","+ String.valueOf(position);
        }

        fr.setNormal_response(result);


        if (map.get("my_id") != null) {
            fr.setMyid(map.get("my_id"));
            Log.v("my_id", map.get("my_id"));
        }
        if (map.get("other_id") != null) {
            fr.setOther_id(map.get("other_id"));
            Log.v("other_id", map.get("other_id"));

        }
        if (map.get("other_name") != null) {
            fr.setOther_name(map.get("other_name"));
            Log.v("other_name", map.get("other_name"));

        }
        if (map.get("update_at") != null) {
            fr.setDate(map.get("update_at"));
            Log.v("update_at", map.get("update_at"));

        }

        if (map.get("job_id") != null) {
            fr.setJob_id(map.get("job_id"));
            Log.v("job_id", map.get("job_id"));
        }

        if (map.get("isChecked") != null) {
            fr.setIsChecked(map.get("isChecked"));
            Log.v("isChecked", map.get("isChecked"));
        }

        if (map.get("isAccepted") != null) {
            fr.setIsAccepted(map.get("isAccepted"));
            Log.v("isAccepted", map.get("isAccepted"));
        }

        EventBus.getDefault().post(fr);

    }

    @Override
    protected Void doInBackground(String... strings) {

        String check = strings[0];
        String URL_GET_USER_INFO = UrlCollections.URL_GET_USER_INFO;
        String URL_SEND_REQ = UrlCollections.URL_SEND_REQ;
        String URL_GET_REQ = UrlCollections.URL_GET_REQ;
        String URL_ACCEPT_REQ = UrlCollections.URL_ACCEPT_REQ;
        String URL_GET_MYREQ = UrlCollections.URL_GET_MY_REQ;

        if (check.equals("0")) {
            result = send_request(URL_SEND_REQ);
        } else if (check.equals("1")) {
            result = check_request(URL_GET_REQ);
        } else if (check.equals("2")) {
            if (jobid != null) {
                result = accept_request(URL_ACCEPT_REQ, jobid);
            }
        } else if (check.equals("3")) {
            result = get_user_info(URL_GET_USER_INFO);
        } else if (check.equals("4")) {
            check_my_request(URL_GET_MYREQ,list);
            result = null;
        }

        return null;
    }

    private String send_request(String url) {
        String res_st = null;
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "my_id", userid1
                ).addFormDataPart(
                        "other_id", userid2
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().
                readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .build();


        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res_st;
    }

    private String get_user_info(String url) {
        String res_st = null;
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "other_id", userid2
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().
                readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .build();


        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string();

            Log.v("respose", res_st);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res_st;
    }

    private String accept_request(String url, String job_id) {

        String res_st = null;
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "my_id", userid1
                )
                .addFormDataPart(
                        "other_id", userid2
                )
                .addFormDataPart(
                        "job_id", job_id
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().
                readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .build();


        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res_st;
    }

    private String check_request(String url) {

        String res_st = null;
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "my_id", userid1
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().
                readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .build();


        String[] res_array = null;
        try {
            Response res = client.newCall(request).execute();
            res_st = res.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res_st;
    }

    private String check_my_request(String url, List<FriendList> lists) {

        String res_st = null;
        FriendDatabase fd = new FriendDatabase(context);

        for (FriendList not_friend: lists) {

            userid2 = String.valueOf(not_friend.getUserid());

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                            "my_id", userid1
                    )
                    .addFormDataPart(
                            "other_id", userid2
                    )
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder().
                    readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .build();


            String[] res_array = null;
            try {
                Response res = client.newCall(request).execute();
                res_st = res.body().string();
                FriendResponse fr = new FriendResponse();
                Log.v("friend_check",res_st);
                if(res_st.replace("\n","").matches(".*normalaccepted.*")) {
                    fr.setOther_name(not_friend.getUsername());
                    fr.setOther_id(String.valueOf(not_friend.getUserid()));
                    fr.setIsAccepted("1");
                    fd.dbupdate(not_friend.get_id(), fr);
                    Log.v("update_db_intask",not_friend.get_id()+"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void check_basic_auth() {

        Authenticator authenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic("tom", "hankusu");
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        };

        try {

            final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] certificate = new X509Certificate[0];
                    return certificate;
                }

            }};

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient client = new OkHttpClient().newBuilder().
                    readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(sslSocketFactory)
                    .build();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


}