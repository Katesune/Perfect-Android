package com.example.picassoapplication;

import android.content.Context;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Picture.class}, version=2)
public abstract class PicturesDB extends RoomDatabase {
    abstract Manager manager_category();

    private static final String DB_NAME = "picasso.db";
    private static volatile com.example.picassoapplication.PicturesDB INSTANCE = null;

    synchronized static com.example.picassoapplication.PicturesDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static com.example.picassoapplication.PicturesDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<com.example.picassoapplication.PicturesDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    com.example.picassoapplication.PicturesDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), com.example.picassoapplication.PicturesDB.class,
                    DB_NAME);
        }
        return (b.createFromAsset("pictures.db").build());

    }
}