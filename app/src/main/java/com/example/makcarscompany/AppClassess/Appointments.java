package com.example.makcarscompany.AppClassess;

public class Appointments {

    private String username;
    private String time;
    private String date;
    private long cid;

    public Appointments(){

    }

    public Appointments(String username, long cid, String date,String time) {

        this.username = username;
        this.cid = cid;
        this.date = date;
        this.time=time;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String tostring() {
        return
                "username: " + username +"\n\n"+
                "time: " + time +"\n\n"+
                "date: " + date +"\n\n"+
                "cid: " + cid ;
    }


}
