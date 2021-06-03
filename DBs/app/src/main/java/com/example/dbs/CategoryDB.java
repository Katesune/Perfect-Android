package com.example.dbs;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities={Category.class}, version=2)
public abstract class CategoryDB extends RoomDatabase {
    abstract ManagerCategory manager();

    private static final String DB_NAME = "mycategory2.db";
    private static volatile com.example.dbs.CategoryDB INSTANCE = null;

    synchronized static com.example.dbs.CategoryDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static com.example.dbs.CategoryDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<com.example.dbs.CategoryDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    com.example.dbs.CategoryDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), com.example.dbs.CategoryDB.class,
                    DB_NAME);
        }
        return (b.createFromAsset("mybase.db").build());

    }
}