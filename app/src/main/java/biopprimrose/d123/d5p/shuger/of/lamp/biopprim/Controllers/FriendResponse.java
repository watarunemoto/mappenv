package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

/**
 * Created by amemiyaY on 2016/11/29.
 */
public class FriendResponse {

    private String myid = "";
    private String other_id = "";
    private String other_name = "";
    private String job_id = "";
    private String date = "";
    private String isAccepted = "";
    private String isChecked = "";
    private String normal_response = "";


    public FriendResponse() {
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getNormal_response() {
        return normal_response;
    }

    public void setNormal_response(String normal_response) {
        this.normal_response = normal_response;
    }
}
