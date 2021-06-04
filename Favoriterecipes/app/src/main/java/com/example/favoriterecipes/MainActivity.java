package com.example.favoriterecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    LinearLayout ln;

    EditText simple_search_v;
    Spinner diet_v;
    EditText min_calories_v;
    EditText max_calories_v;
    EditText excluded_v;

    ListView list_view;
    TextView error;

    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ln = (LinearLayout) findViewById(R.id.main);
        error = (TextView) findViewById(R.id.errorfound);

        diet_v = (Spinner) findViewById(R.id.diet);
        min_calories_v = (EditText) findViewById(R.id.mincalories);
        max_calories_v = (EditText) findViewById(R.id.maxcalories);
        simple_search_v = (EditText) findViewById(R.id.simple_search);
        excluded_v = (EditText) findViewById(R.id.excluded);

        min_calories_v.setText("0");
        max_calories_v.setText("0");

        list_view = (ListView) findViewById(R.id.listview);
    }


    public void search(View v){
        String q = simple_search_v.getText().toString();
        String app_id ="7e23602a";
        String app_key ="ea740edf008745d2be2c31f443c91c64";

        String diet = diet_v.getSelectedItem().toString();
        String range = min_calories_v.getText()+"-"+max_calories_v.getText().toString();
        String excluded = excluded_v.getText().toString();

        Log.d("mytag", range);

        String[] parametres ={diet, range, q, app_id, app_key, excluded};


        Call<Response> getRecipe = getSearch(parametres);
        //api.search(q, app_id, app_key);

        Callback<Response> recipecallback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                if (r!=null) {
                    showResult(r);
                } else error.setText("Results Not Found");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("mytag", "fail:" + t.getLocalizedMessage());
            }
        };
        getRecipe.enqueue(recipecallback);

        //Log.d("mytag", answer.toString() );
        //showResult();
    }

    public Call<Response> getSearch(String[] parametres) {
        String url = "https://api.edamam.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        API api = retrofit.create(API.class);
        Call<Response> getRecipe=null;

        ArrayList<String> excludes = new ArrayList<String>();
        String[] ex = parametres[5].split(",");
        for (int i=0; i<ex.length; i++) {
            excludes.add(ex[i]);
        }

        int command = getCommand(parametres);

        switch (command){
            case 0: getRecipe = api.search(parametres[2], parametres[3], parametres[4]);
                break;
            case 1: getRecipe = api.searchWithDiet(parametres[2], parametres[3], parametres[4], parametres[0]);
                break;
            case 2: getRecipe = api.searchWithCaloriesRange(parametres[2], parametres[3], parametres[4], parametres[1]);
                break;
            case 3: getRecipe = api.searchWitExcluded(parametres[2], parametres[3], parametres[4], excludes);
                break;
            case 4: getRecipe = api.searchWitExcludedAndCalories(parametres[2], parametres[3], parametres[4], parametres[1],excludes);
                break;
            case 5: getRecipe = api.searchWitExcludedAndDiet(parametres[2], parametres[3], parametres[4], parametres[0],excludes);
                break;
            case 6: getRecipe = api.searchWithCaloriesRangeAndDiet(parametres[2], parametres[3], parametres[4], parametres[0],parametres[1]);
                break;
            default: getRecipe = api.searchWithAllFilters(parametres[2], parametres[3], parametres[4], parametres[0],parametres[1], excludes);
                break;
        }


        return getRecipe;
    }

    int getCommand(String[] parametres) {
        //0- diet, 1-calories, 5 - excluded
        if (parametres[0].equals("none")
                &&(parametres[1].equals("0-0"))
                &&(parametres[5].equals("none"))) return 0;

        if (!parametres[0].equals("none")
                &&(!parametres[1].equals("0-0"))
                &&(!parametres[5].equals("none"))) return 7;


        if (parametres[0].equals("none")
                &&(parametres[1].equals("0-0"))) return  3;

        if (parametres[0].equals("none")
                &&(parametres[5].equals("none"))) return  2;

        if (parametres[1].equals("none")
                &&(parametres[5].equals("none"))) return  1;



        if (!parametres[0].equals("none")
                &&(!parametres[1].equals("0-0"))) return  6;

        if (!parametres[0].equals("none")
                &&(!parametres[5].equals("0-0"))) return  5;
        else return 4;

    }

    public void showResult(Response answer){
        LayoutInflater ltInflater = getLayoutInflater();

        for (Hit h: answer.hits) {

            View options_item = ltInflater.inflate(R.layout.recipe, ln, false);

            TextView label = options_item.findViewById(R.id.label);
            TextView calories_v = options_item.findViewById(R.id.calories);
            ImageView im_v = options_item.findViewById(R.id.im);

            label.setText("Label: "+h.recipe.label);
            calories_v.setText("Calories: "+h.recipe.calories);

            if (h.recipe.image != null) {
                Uri uri_pic = Uri.parse(h.recipe.image);
                Picasso p = new Picasso.Builder(getApplicationContext()).build();
                p.load(uri_pic).into(im_v);
            }
            ln.addView(options_item);
        }

    }
}