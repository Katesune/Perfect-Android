package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements ValueEventListener {

    GoogleSignInClient mGoogleSignInClient;
    LocationManager locationManager;

    EditText tv_place;
    EditText tv_first_koord;
    EditText tv_sec_koord;

    DatabaseReference dbRef;
    final int RC_SIGN_IN = 123;
    private final int LOCATION_PERMISSION = 1001;
    final String CHILD = "places";
    boolean granted;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-5d9a1-default-rtdb.firebaseio.com/");

        Place new_p = new Place("Moscow", 33, 44);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Place new_p = new Place("Novosib", 313, 424);
                Log.d("mytag", "count: " + snapshot.getChildrenCount());

                for (DataSnapshot s : snapshot.getChildren()) {
                    Place db_place = s.getValue(Place.class);
                    db_place.showInLog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("mytag", "Failed to read value");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationListener listener = helpLocation();

        if (granted || checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 20, listener);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) showLocation(location);
        }
        }
    }

    public LocationListener helpLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location == null) {
                    return;
                } else {
                    showLocation(location);
                }
            }
        };

        return listener;
    }

    private void showLocation(Location location) {
        String koord = String.valueOf(location.getLatitude());
        Log.d("mytag", koord);
        koord = String.valueOf(location.getLongitude());
        koord = new Date(location.getTime()).toString();
        //location.distanceTo();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION) {
            granted = true;
            if (grantResults.length>0) {
                for(int i=0; i<grantResults.length; i++) {
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
            } else {
                granted = false;
            }
        }
    }

    public void onSend(View v) {
        Place new_p = new Place("Moscow", 33, 44);

        dbRef.child("places").setValue(new_p.place);

        String id=dbRef.push().getKey();
        dbRef.setValue(new_p);
        dbRef.child(id).setValue(new_p);
    }



    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Place place = snapshot.getValue(Place.class);
        Log.d("mytag", "place: " + place);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    public void onGoogleSignOut(View v) {
        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient = new GoogleAuth().googleAuthentification(this);
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
    }
}