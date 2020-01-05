package com.example.project1.Classes;


import android.view.View;

public class myTime {
    public String startTime;
    public String endTime;
    public myTime() {
        this.startTime = "00:00:00";
        this.endTime = "00:00:00";

    }
    public myTime(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public String getstartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }




    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime= endTime;
    }

}