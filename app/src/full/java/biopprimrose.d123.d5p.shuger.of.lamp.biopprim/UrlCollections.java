package biopprimrose.d123.d5p.shuger.of.lamp.biopprim;

/**
 * Created by amemiyaY on 2016/09/21.
 */
public class UrlCollections  {

    public UrlCollections(){}

    private static final String SERVER_NAME = "http://133.14.168.203:5900/api/v1/";//gitで持ってくる前
    //private static final String SERVER_NAME = "http://10.0.1.19:5900/api/v1/";研究室内のみのとき
    //private static final String SERVER_NAME = "http://133.14.168.180:12320/api/v1/";学校内の時
//    private static final String SERVER_NAME = "http://133.14.168.108:5900/api/v1/";
    /**
    public static final String URL_UPLOAD_PHOTO = "http://133.14.168.203/inviawefiLigand.php";
    public static final String URL_RANKINGS = "http://133.14.168.203/gacDojisafvesveywo.php";
    public static final String URL_OTHER_LATLNG = "http://133.14.168.203/iceCreamCet.php";
    public static final String URL_USERNAME_UPLOAD = "http://133.14.168.203/Udli3indkadjD849.php";
    public static final String URL__POINT = "http://133.14.168.203/randbures.php";
    public static final String URL_OTHER_PHOTO = "http://133.14.168.203/ifjvjngkjDKcm.php";
    public static final String URL_THUMBNAL ="http://133.14.168.203/drandbures_thumb.php";
    public static final String URL_EVAL_UPLOAD = "http://133.14.168.203/eifasdfceinn.php";
    public static final String URL_FROM_USERNAME = "http://133.14.168.203/download_from_username.php";
    public static final String URL_GET_USER_INFO = "";
    public static final String URL_SEND_REQ ="";
    public static final String URL_GET_REQ ="";
    public static final String URL_ACCEPT_REQ ="";
     */

    /**
     * flask api 用のurl
     */


    public static final String URL_GET_CATEGORY = SERVER_NAME + "download/rankingcategory";
    public static final String URL_GET_CATEGORYRANKING = SERVER_NAME + "download/newranking";

    public static final String URL_UPLOAD_PHOTO = SERVER_NAME +  "upload/photo";
    public static final String URL_RANKINGS = SERVER_NAME + "download/ranking";
    public static final String URL_USERNAME_UPLOAD = SERVER_NAME +"usermaked";
    public static final String URL_AREA_POINT = SERVER_NAME + "download/area_point";
    public static final String URL_OTHER_PHOTO = SERVER_NAME +"download/other_photo";
    public static final String URL_THUMBNAL =SERVER_NAME + "download/other_photo";
    public static final String URL_EVAL_UPLOAD = SERVER_NAME + "upload/evaluation";
    public static final String URL_FROM_USERNAME = SERVER_NAME + "download/friend_photo";
    public static final String URL_GET_USER_INFO = SERVER_NAME + "friend/get";
    public static final String URL_SEND_REQ =SERVER_NAME + "friend/req";
    public static final String URL_GET_REQ =SERVER_NAME + "friend/check/other_req";
    public static final String URL_ACCEPT_REQ = SERVER_NAME + "friend/accept";
    public static final String URL_GET_MY_REQ =SERVER_NAME + "friend/check/my_req";

    public static final String URL_GET_LATLNG = SERVER_NAME + "download/latlng";

    //event で使用するurl
    public static final String URL_GET_EVENT = SERVER_NAME + "event";
}
