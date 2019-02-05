package com.example.onestop_admin;

public class Upload {

    public String hostel;
    public String month;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String hostel, String month) {
        this.hostel = hostel;
        this.month = month;
    }

    public String gethostel() {
        return hostel;
    }

    public String getmonth() {
        return month;
    }
}