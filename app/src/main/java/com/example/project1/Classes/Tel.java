package com.example.project1.Classes;

import com.google.gson.annotations.SerializedName;

public class Tel implements Comparable<Tel>{
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;

    public Tel(String name, String phone){
        this.name = name;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String tel) {
        this.phone = phone;
    }

    @Override
    public int compareTo(Tel t) {
        return name.compareTo(t.getName());
    }
}

