package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng myPlace;
    DatabaseReference dbRef;
    LatLng choose_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-5d9a1-default-rtdb.firebaseio.com/");

        double lat = getIntent().getDoubleExtra("latitude", 52.27537);
        double lon = getIntent().getDoubleExtra("longitude", 104.2774);
        myPlace = new LatLng(lat, lon);
    }

    public void addMarker (View v){
        if (choose_place!=null) {
            Random r = new Random();
            int count = r.nextInt(100000 + 1);

            Double my_lat = choose_place.latitude;
            Double my_lon = choose_place.longitude;

            Place new_p = new Place("my_place", my_lat, my_lon);
            dbRef.child("places").child(String.valueOf(count)).setValue(new_p);

            Intent intent = new Intent(MapsActivity.this,ProfileActivity.class);

            intent.putExtra("latitude", my_lat);
            intent.putExtra("longitude", my_lon);

            startActivity(intent);

        } else Toast.makeText(MapsActivity.this, "Точка не выбрана", Toast.LENGTH_SHORT).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(myPlace).title("Мое местоположение"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
        CameraPosition cameraPosition = new CameraPosition(myPlace, 23, 45, 15);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Выбранная точка"));
                choose_place = latLng;
            }
        });
    }
}