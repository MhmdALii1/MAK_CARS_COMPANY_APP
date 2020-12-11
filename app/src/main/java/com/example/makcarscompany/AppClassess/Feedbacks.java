package com.example.makcarscompany.AppClassess;

public class Feedbacks {

    private long cid;
    private String username;
    private String comment;

    public Feedbacks(){

    }

    public Feedbacks(String username, String comment,long cid) {
        this.cid=cid;
        this.username = username;
        this.comment = comment;

    }
    public void setCid(long cid) {
        this.cid = cid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCid() {
        return cid;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public String tostring() {
                 return
                          "username: " + username +"\n\n"+
                          "cid: " + cid +"\n\n"+
                          "comment: " + comment ;
    }







}
