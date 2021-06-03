package com.example.dbs;

import androidx.annotation.NonNull;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import java.util.Locale;

@Entity(tableName = "category")
public class Category {

    @PrimaryKey
    @NonNull
    int _id;
    @NonNull
    String name, region;

    public Category(int _id, @NonNull String name, @NonNull String region) {
        this._id = _id;
        this.name = name;
        this.region = region;
    }

    @Override
    public String toString() { return String.format(Locale.getDefault(), "%s: %s (%d)", name, region); }
}