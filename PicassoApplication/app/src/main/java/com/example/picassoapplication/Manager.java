package com.example.picassoapplication;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Manager {
    @Query("SELECT * FROM pictures ORDER BY _id")
    List<Picture> selectAll();

    @Query("SELECT * FROM pictures WHERE _id=:id")
    Picture findById(int id);

    @Query("SELECT COUNT(_id) FROM pictures")
    int getNumberOfRows();

    @Query("DELETE FROM pictures")
    void deleteTable();

    @Query("DELETE FROM pictures WHERE _id=:id")
    void deleteItem(int id);

    @Insert
    void insert(Picture... pictures);

    @Delete
    void delete(Picture... pictures);

    @Update
    void update(Picture... pictures);
}
