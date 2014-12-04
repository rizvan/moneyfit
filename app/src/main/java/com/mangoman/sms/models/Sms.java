package com.mangoman.sms.models;

/**
 * Created by Harshit Rastogi on 9/20/2014 at 12:32 PM.
 */
public class Sms {
    private String _id;
    private String _address;
    private String _msg;
    private String _time;

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public String getMsg() {
        return _msg;
    }

    public long getTime() {
        return Long.parseLong(_time);
    }

    public void setId(String id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setTime(String time) {
        _time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: " + _address).append("\n");
        sb.append("Time: " + _time).append("\n");
        sb.append("Body: " + _msg).append("\n");
        return sb.toString();
    }
}

