package com.example.dbs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Manager {
    @Query("SELECT * FROM product ORDER BY _id")
    List<Product> selectAll();

    @Query("SELECT * FROM product WHERE _id=:id")
    Product findById(int id);

    @Query("SELECT COUNT(_id) FROM product")
    int getNumberOfRows();

    @Transaction
    @Query("SELECT * FROM product ORDER BY _id")
    List<CategoryProduct> selectAllWithCategory();

    @Transaction
    @Query("SELECT * FROM product WHERE category_id=:id")
    CategoryProduct showProductsByCategory(int id);

    @Query("DELETE FROM product")
    void deleteTable();

    @Query("DELETE FROM product WHERE _id=:id")
    void deleteItem(int id);

    @Insert
    void insert(Product... products);

    @Delete
    void delete(Product... products);

    @Update
    void update(Product... products);
}