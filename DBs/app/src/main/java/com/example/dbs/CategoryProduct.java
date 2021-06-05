package com.example.dbs;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CategoryProduct {
    @Embedded public Category category;
    @Relation(
            parentColumn = "_id",
            entityColumn = "category_id")
    public Product product;
}
