package com.example.dbs;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities={Product.class}, version=2)
public abstract class ProductsDB extends RoomDatabase {
    abstract Manager manager();

    private static final String DB_NAME = "myproducts5.db";
    private static volatile com.example.dbs.ProductsDB INSTANCE = null;

    synchronized static com.example.dbs.ProductsDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static com.example.dbs.ProductsDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<com.example.dbs.ProductsDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    com.example.dbs.ProductsDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), com.example.dbs.ProductsDB.class,
                    DB_NAME);
        }
        return (b.createFromAsset("mybase.db").build());

    }
}