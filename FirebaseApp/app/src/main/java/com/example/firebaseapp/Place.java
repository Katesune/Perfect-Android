package com.example.firebaseapp;

import android.util.Log;

public class Place {
    public String place;
    public int first_koord;
    public int sec_koord;

    Place() {}

    Place(String place, int first_koord, int sec_koord) {
        this.place = place;
        this.first_koord = first_koord;
        this.sec_koord = sec_koord;
    }

    public void showInLog() {
        Log.d("mytag", "Координата - " + place +" "+ String.valueOf(first_koord) + " " + String.valueOf(sec_koord));
    }


}
