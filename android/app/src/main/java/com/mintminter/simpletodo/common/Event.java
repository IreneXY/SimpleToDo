package com.mintminter.simpletodo.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Irene on 8/21/17.
 */

public class Event {

    public String mTitle = "";
    public long mCreateTime = 0;
    public long mDoneTime = -1;
    public int mPriority = 1;

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final String KEY_TITLE = "title";
    public static final String KEY_CREATETIME = "ctime";
    public static final String KEY_DONETIME = "dtime";
    public static final String KEY_PRIORITY = "priority";

    public Event(String title, long createTime){
        mTitle = title;
        mCreateTime = createTime;
    }

    public Event(String title){
        this(title, Calendar.getInstance().getTimeInMillis());
    }

    public Event(){

    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_TITLE, this.mTitle);
            json.put(KEY_CREATETIME, this.mCreateTime + "");
            json.put(KEY_DONETIME, this.mDoneTime + "");
            json.put(KEY_PRIORITY, this.mPriority + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void fromJson(JSONObject json){
        mTitle = json.optString(KEY_TITLE, "");
        mCreateTime = Long.parseLong(json.optString(KEY_CREATETIME, "0"));
        mDoneTime = Long.parseLong(json.optString(KEY_DONETIME, "-1"));
        mPriority = Integer.parseInt(json.optString(KEY_PRIORITY, "1"));
    }

    public void fromString(String string){
        try {
            JSONObject json = new JSONObject(string);
            fromJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return toJson().toString();
    }
}
