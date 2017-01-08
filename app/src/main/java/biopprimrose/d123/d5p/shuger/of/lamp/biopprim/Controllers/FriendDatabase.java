package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Adapters.FriendList;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.FriendContract;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.FriendOpenHelper;
import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases.ImgContract;

/**
 * Created by amemiyaY on 2016/12/02.
 */
public class FriendDatabase {

    private Context context;

    public FriendDatabase(Context context) {
        this.context = context;
    }


    public void dbch(FriendResponse fr) {

//        List<FriendList> fl =  Read_db();
//        int list_size =  fl.size();

        FriendOpenHelper foh = new FriendOpenHelper(context);
        SQLiteDatabase db = foh.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(FriendContract.Friends.COL_OTHER_ID, fr.getOther_id());
        values.put(FriendContract.Friends.COL_OTHER_NAME, fr.getOther_name());
        values.put(FriendContract.Friends.COL_IS_ACCEPTED, fr.getIsAccepted());
        values.put(FriendContract.Friends.COL_JOB_ID, fr.getJob_id());


        db.insert(
                FriendContract.Friends.TABLE_NAME,
                null,
                values
        );
        db.close();
    }

    public List<FriendList> Read_db() {
        List<FriendList> list = new ArrayList<>();

        FriendOpenHelper foh = new FriendOpenHelper(context);
        SQLiteDatabase db = foh.getReadableDatabase();
        Cursor c = db.query(
                FriendContract.Friends.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FriendContract.Friends.COL_ID
        );

        while (c.moveToNext()) {
            FriendList friendList = new FriendList();
            friendList.set_id(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_ID)));
            friendList.setUsername(c.getString(c.getColumnIndex(FriendContract.Friends.COL_OTHER_NAME)));
            friendList.setUserid(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_OTHER_ID)));
            friendList.setIsFrined(c.getString(c.getColumnIndex(FriendContract.Friends.COL_IS_ACCEPTED)));
            friendList.setJob_id(c.getString(c.getColumnIndex(FriendContract.Friends.COL_JOB_ID)));
            list.add(friendList);
        }

        c.close();
        db.close();
        return list;
    }

    public FriendList Read_db_num(int num) {
        FriendOpenHelper foh = new FriendOpenHelper(context);
        SQLiteDatabase db = foh.getReadableDatabase();
        Cursor c = db.query(
                FriendContract.Friends.TABLE_NAME,
                null,
                FriendContract.Friends._ID + " = " + num,
                null,
                null,
                null,
                null
        );
        FriendList friendList = new FriendList();



        c.moveToFirst();

        friendList.set_id(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_ID)));
        friendList.setUsername(c.getString(c.getColumnIndex(FriendContract.Friends.COL_OTHER_NAME)));
        friendList.setUserid(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_OTHER_ID)));
        friendList.setIsFrined(c.getString(c.getColumnIndex(FriendContract.Friends.COL_IS_ACCEPTED)));
        friendList.setJob_id(c.getString(c.getColumnIndex(FriendContract.Friends.COL_JOB_ID)));

        Log.v("database_in",friendList.getUserid()+","+friendList.getUsername()+","+friendList.getJob_id()+","+friendList.getIsFrined());

        c.close();
        db.close();
        return friendList;
    }

    public List<FriendList> Read_db_requested() {
        List<FriendList> list = new ArrayList<>();

        FriendOpenHelper foh = new FriendOpenHelper(context);
        SQLiteDatabase db = foh.getReadableDatabase();
        Cursor c = db.query(
                FriendContract.Friends.TABLE_NAME,
                null,
                FriendContract.Friends.COL_IS_ACCEPTED + " = 2",
                null,
                null,
                null,
                FriendContract.Friends.COL_ID
        );

        while (c.moveToNext()) {
            FriendList friendList = new FriendList();
            friendList.set_id(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_ID)));
            friendList.setUsername(c.getString(c.getColumnIndex(FriendContract.Friends.COL_OTHER_NAME)));
            friendList.setUserid(c.getInt(c.getColumnIndex(FriendContract.Friends.COL_OTHER_ID)));
            friendList.setIsFrined(c.getString(c.getColumnIndex(FriendContract.Friends.COL_IS_ACCEPTED)));
            friendList.setJob_id(c.getString(c.getColumnIndex(FriendContract.Friends.COL_JOB_ID)));
            list.add(friendList);
        }

        c.close();
        db.close();
        return list;
    }



    public void dbupdate(int id, FriendResponse fr) {

        Log.v("dbupdate",id+"");
        FriendOpenHelper foh = new FriendOpenHelper(context);
        SQLiteDatabase db = foh.getWritableDatabase();

        FriendList fl = Read_db_num(id);

        ContentValues values = new ContentValues();
        values.put(FriendContract.Friends.COL_ID, id);
        values.put(FriendContract.Friends.COL_OTHER_ID, fl.getUserid());
        values.put(FriendContract.Friends.COL_OTHER_NAME, fl.getUsername());
        values.put(FriendContract.Friends.COL_IS_ACCEPTED, 1);
        values.put(FriendContract.Friends.COL_JOB_ID, fl.getJob_id());

        db.update(
                FriendContract.Friends.TABLE_NAME,
                values,
                FriendContract.Friends._ID + "=" + id,
                null
        );
        db.close();
    }
}
