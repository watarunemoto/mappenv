package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters;

/**
 * Created by amemiyaY on 2016/11/17.
 */
public class FriendList {

    int userid;
    String username;
    String date;
    String isFrined;
    String job_id;
    int _id;


    public FriendList() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUserid() {
        return userid;
    }
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsFrined() {
        return isFrined;
    }

    public void setIsFrined(String isFrined) {
        this.isFrined = isFrined;
    }

}
