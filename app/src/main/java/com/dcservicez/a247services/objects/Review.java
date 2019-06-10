package com.dcservicez.a247services.objects;

public class Review{
    public int rate;
    public String comment;



    public Review(int rate, String comnt) {
        this.rate = rate;
        this.comment = comnt;
    }

    public String getComment() {
        return comment;
    }

    public int getRate() {
        return rate;
    }
}