package com.example.picassoapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "pictures")
public class Picture {
    @PrimaryKey
    @NonNull
    int _id;
    @NonNull
    String topic, colors, author, object, url;

    public Picture(int _id, @NonNull String topic, @NonNull String colors, @NonNull String author, @NonNull String object, @NonNull String url) {
        this._id = _id;
        this.topic = topic;
        this.colors = colors;
        this.author = author;
        this.object = object;
        this.url = url;
    }

    @Override
    public String toString() { return String.format(Locale.getDefault(), "%d , %s, %s, %s, %s, %s", _id, topic, colors, author, object, url ); }

}