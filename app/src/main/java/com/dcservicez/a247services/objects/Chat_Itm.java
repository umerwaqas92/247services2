package com.dcservicez.a247services.objects;


public class Chat_Itm {
    String msg;
    long time;
    int msg_type;


    public Chat_Itm(String msg, long time, int msg_type) {
        this.msg = msg;
        this.time = time;
        this.msg_type = msg_type;
    }


    public String getMsg() {
        return msg;
    }

    public long getTime() {
        return time;
    }

    public int getMsg_type() {
        return msg_type;
    }
}
