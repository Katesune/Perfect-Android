package com.example.firebaseapp;

import android.util.Log;

public class Place {
    public String place;
    public double first_koord;
    public double sec_koord;
    int id;

    Place() {}

    Place(String place, double first_koord, double sec_koord) {
        this.place = place;
        this.first_koord = first_koord;
        this.sec_koord = sec_koord;
    }

    public void showInLog() {
        Log.d("mytag", "Координата - " + place +" "+ Double.toString(first_koord) + " " + Double.toString(sec_koord));
    }

    public String getStringPlace() {
          String new_first_koord = Double.toString(first_koord);
          String new_sec_koord = Double.toString(sec_koord);

          String new_marker = place + " ;   " + new_first_koord + " ;   " + new_sec_koord;
          return new_marker;
    }


}
