package com.example.favoritefilmsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

public class FilmListActivity extends AppCompatActivity {

    Gson gson = new GsonBuilder().create();
    String api_key = "2afab14a5f39728f8f613d627e5dd9bb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        String api_key = "2afab14a5f39728f8f613d627e5dd9bb";

        Call<Movies> getFilmCall = getFilmSearch("1");
        //api.search(q, app_id, app_key);

        Callback<Movies> filmcallback = new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, retrofit2.Response<Movies> response) {

                Log.d("mytag","response.raw().request().url();"+response.raw().request().url());
                Movies movies = response.body();

                if (movies!=null) {
                    setPopularFilms( movies);
                    Log.d("mytag", movies.results[0].original_title);
                    //showResult(r);
                } //else error.setText("Results Not Found");
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d("mytag", "fail:" + t.getLocalizedMessage());
            }
        };

        getFilmCall.enqueue(filmcallback);

    }

    public Call<Movies> getFilmSearch (String film) {
        String url = "https://api.themoviedb.org/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //1043758
        Manager api = retrofit.create(Manager.class);

        Call <Movies> call_film = api.getPopular(api_key, "en-US", 1);
        return call_film;
    }

    public void setPopularFilms(Movies movies){

    }
}