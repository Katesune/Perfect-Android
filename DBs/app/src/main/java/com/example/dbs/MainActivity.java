package com.example.dbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TunesDB db;
    ProductsDB products_db;
    CategoryDB category_db;
    LinearLayout main_lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_lin = (LinearLayout) findViewById(R.id.main_lin);

        products_db = ProductsDB.create(this, false);
        category_db = CategoryDB.create(this, false);
        db = TunesDB.create(this, false); // открывает БД, если её нет, создаёт


        setProducts();
        setCategorys();

    }

    public void setProducts() {
        Cursor product_c = products_db.query("SELECT * FROM product", null);

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.product_item, product_c, product_c.getColumnNames(), new int[]{
                        R.id._id, R.id.category_id, R.id.name, R.id.price, R.id.vendor}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView product_lv = findViewById(R.id.products_listview);
        product_lv.setAdapter(adapter);

        product_lv.setOnItemClickListener(getProductlistener(adapter));
    }

    public AdapterView.OnItemClickListener getProductlistener(SimpleCursorAdapter adapter) {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ColorDrawable d = (ColorDrawable) view.getBackground();

                if (d == null) {
                    view.setBackgroundColor(Color.YELLOW);
                }

                if (d != null && d.getColor() == Color.YELLOW) {
                        Cursor c = adapter.getCursor();
                        int index = c.getPosition() + 1;
                        removeItem(index);
                        setProducts();
                }

            }
        };

        return listener;
    }

    public void setCategoryProducts(int i) {
        Cursor product_c = products_db.query("SELECT * FROM product WHERE category_id="+String.valueOf(i), null);

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.product_item, product_c, product_c.getColumnNames(), new int[]{
                        R.id._id, R.id.category_id, R.id.name, R.id.price, R.id.vendor}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView product_lv = findViewById(R.id.products_listview);
        product_lv.setAdapter(adapter);
    }

    public void setCategorys() {
        Cursor category_c = category_db.query("SELECT * FROM category", null);

        if( category_c != null && category_c.moveToFirst() ) {
            Log.d("mytag", String.valueOf(category_c.getString(1)));
        }

        SimpleCursorAdapter category_adapter =
                new SimpleCursorAdapter(this, R.layout.category_item, category_c, category_c.getColumnNames(), new int[]{
                        R.id._id, R.id.name, R.id.region}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView category_lv = findViewById(R.id.category_listview);
        category_lv.setAdapter(category_adapter);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ColorDrawable d = (ColorDrawable) view.getBackground();
                if (d != null && d.getColor() == Color.YELLOW) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                } else view.setBackgroundColor(Color.YELLOW);

                Cursor cur = category_adapter.getCursor();
                int index = cur.getInt(0);
                setCategoryProducts(index);
                Log.d("mytag", "clicked on id" + index);
            }
        };

        category_lv.setOnItemClickListener(getCategoryListener(category_adapter));
    }

    public AdapterView.OnItemClickListener getCategoryListener(SimpleCursorAdapter adapter) {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ColorDrawable d = (ColorDrawable) view.getBackground();
                if (d != null && d.getColor() == Color.YELLOW) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                } else view.setBackgroundColor(Color.YELLOW);
                // TODO задание: удалить запись, которая выделена пользователем (position - номер в списке)

                Cursor cur = adapter.getCursor();
                int index = cur.getInt(0);
                setCategoryProducts(index);
                Log.d("mytag", "clicked on id" + index);
            }
        };
        return listener;
    }

    public void setCursorInUIThread(Cursor c) {
        Context ctx = getApplicationContext();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleCursorAdapter adapter =
                        new SimpleCursorAdapter(ctx, R.layout.product_item, c, c.getColumnNames(), new int[]{
                                R.id._id, R.id.category_id, R.id.name, R.id.price, R.id.vendor}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                Log.d("mytag", "Records in adapter: " + adapter.getCount());
                ListView lv = findViewById(R.id.products_listview);
                lv.setAdapter(adapter);
            }
        });

    }

    public void showAll(View v) {
        setProducts();
        ListView category_lv = findViewById(R.id.category_listview);
        for (int i=0; i<3; i++) {
            category_lv.getChildAt(i).setBackgroundColor(Color.WHITE);
        }

    }

    public void getAll(View v) {
        new Thread() {
            @Override
            public void run() {
                Cursor c = products_db.query("SELECT * FROM product ORDER BY _id", null);
                //List<CategoryProduct> main = products_db.manager().selectAllWithCategory();
                CategoryProduct main = products_db.manager().showProductsByCategory(1);
                Log.d("mytag", " my name "+main.category.name);
            }
        }.start();

    }


    public void onClearClick(View v) {
        new Thread() {
            @Override
            public void run() {
//                Cursor c = db.query("SELECT * FROM tunes", null);
                products_db.manager().deleteTable();
                category_db.manager_category().deleteTable();
                Log.d("mytag", String.valueOf(products_db.manager().getNumberOfRows()));
                Log.d("mytag", String.valueOf(category_db.manager_category().getNumberOfRows()));
            }
        }.start();
    }

    public void AddProduct(View v) {
        new Thread() {
            @Override
            public void run() {

                Manager manager = products_db.manager();

                int id = manager.getNumberOfRows()+1;

                Random r = new Random();
                Product p = new Product(id, r.nextInt(10000),"flippers", "Nike", 560);
                manager.insert(p);

                Cursor c = products_db.query("SELECT * FROM product", null);
                Log.d("mytag", "Records after insert: "+c.getCount());
                setCursorInUIThread(c);
            }
        }.start(); // запустит созданный поток
    }

    public void removeItem(int i) {
        new Thread() {
            @Override
            public void run() {
                Manager manager = products_db.manager();
                manager.deleteItem(i);
                Cursor c = products_db.query("SELECT * FROM product", null);
                setCursorInUIThread(c);
            }
        }.start(); // запустит созданный поток
    }
}