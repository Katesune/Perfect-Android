package com.example.dbs;

        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.Query;
        import androidx.room.Update;

        import java.util.List;

@Dao
public interface ManagerCategory {
    @Query("SELECT * FROM category ORDER BY _id")
    List<Category> selectAll();

    @Query("SELECT * FROM category WHERE _id=:id")
    Category findById(int id);

    @Insert
    void insert(Category... category);

    @Delete
    void delete(Category... category);

    @Update
    void update(Category... category);
}