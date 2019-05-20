package com.dcservicez.a247services.objects;

public class Task {
    public long time=System.currentTimeMillis();
    public  String id;
    public   int status=0;
//0 new 1 accpeted 2 rejected 3 arrived 4 completed

    public Task(String id) {
        this.id = id;
    }
}
