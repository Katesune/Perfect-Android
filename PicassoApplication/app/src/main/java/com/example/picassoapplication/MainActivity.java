package com.example.picassoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.squareup.picasso.Picasso;
import static android.provider.Settings.System.AIRPLANE_MODE_ON;

public class MainActivity extends AppCompatActivity {

    PicturesDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = PicturesDB.create(this, false);
        ;

        Cursor cursor = db.query("SELECT * FROM pictures", null);

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.picture, cursor,
                        new String[] { "topic", "colors", "author", "object", "url"}, new int[]{
                        R.id.topic, R.id.colors, R.id.author, R.id.object, R.id.photo},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        class myViewBinder implements SimpleCursorAdapter.ViewBinder {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                Log.d("mytag", String.valueOf(view.getId()));
                if (view != null && Integer.toString(view.getId()).equals("2131231009")) {
                        ImageView image = (ImageView) view;
                        Uri url = Uri.parse(cursor.getString(columnIndex));
                        Log.d("mytag", url.toString());

                        Picasso p = new Picasso.Builder(getApplicationContext()).build();
                        p.load(url).into(image);
                    return true;
                    }
                return false;
            }
        }


        if (cursor != null && cursor.moveToFirst()) {
            adapter.setViewBinder(new myViewBinder());

            ListView listview = findViewById(R.id.listview);
            listview.setAdapter(adapter);
        }
    }
}