package com.dcservicez.a247services.objects;

public class Conversation {
    String last_msg;
    String id;
    long time;
    boolean isRead;
    String url;
    String usr_id;

    public Conversation(String last_msg, String id, long time, boolean isRead, String url, String usr_id) {
        this.last_msg = last_msg;
        this.id = id;
        this.time = time;
        this.isRead = isRead;
        this.url = url;
        this.usr_id = usr_id;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public String getUrl() {
        return url;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }
}
