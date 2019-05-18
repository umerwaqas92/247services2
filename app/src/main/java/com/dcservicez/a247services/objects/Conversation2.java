package com.dcservicez.a247services.objects;

public class Conversation2 {
    public long time=System.currentTimeMillis();
    public  String last_msg;
    public boolean isread=false;

    public Conversation2(String last_msg) {
        this.last_msg = last_msg;
    }

    public long getTime() {
        return time;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public boolean isIsread() {
        return isread;
    }
}
