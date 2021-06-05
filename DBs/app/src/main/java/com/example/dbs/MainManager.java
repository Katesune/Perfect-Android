package com.example.dbs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MainManager {
    @Transaction
    @Query("SELECT * FROM product ORDER BY _id")
    List<CategoryProduct> selectAllWithCategory();
}
