package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements ValueEventListener {

    GoogleSignInClient mGoogleSignInClient;
    LocationManager locationManager;

    LinearLayout ln;
    EditText tv_place;
    EditText tv_first_koord;
    EditText tv_sec_koord;
    TextView name, mail, tv_message;
    ImageView ava;
    ListView listView;

    DatabaseReference dbRef;
    FirebaseDatabase db;
    final int RC_SIGN_IN = 123;
    private final int LOCATION_PERMISSION = 1001;
    final String CHILD = "places";

    boolean granted;
    Location location;
    Location my_stage;
    int count = 0;

    ArrayList<String> places = new ArrayList<>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ln = (LinearLayout) findViewById(R.id.profile_lin);

        listView =(ListView) findViewById(R.id.listview);

        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        tv_message = findViewById(R.id.message);
        ava = findViewById(R.id.avatar);
        tv_place = findViewById(R.id.place);
        tv_first_koord = findViewById(R.id.first_koord);
        tv_sec_koord = findViewById(R.id.sec_koord);

        dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-5d9a1-default-rtdb.firebaseio.com/");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Place new_p = new Place("Novosib", 313, 424);
                count =  (int)snapshot.child("places").getChildrenCount() + 1;

                for (DataSnapshot s : snapshot.child("places").getChildren()) {
                    Place db_place = s.getValue(Place.class);
                    String new_place = db_place.getStringPlace();

                    places.add(new_place);
                    updateUI();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("mytag", "Failed to read value");
            }
        });

        double lat = getIntent().getDoubleExtra("latitude", 0);
        double lon = getIntent().getDoubleExtra("longitude", 0);

        if(lat!=0 && lon!=0) {
            Place my_place = new Place("Моя остановка", lat, lon);
            tv_place.setText(my_place.place);
            tv_first_koord.setText(String.valueOf(my_place.first_koord));
            tv_sec_koord.setText(String.valueOf(my_place.sec_koord));
        }
    }

    public void updateUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, places);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {
                        // по позиции получаем выбранный элемент
                        String selectedItem = places.get(position);

                        // установка текста элемента TextView
                        onMyStage(selectedItem);
                        Log.d("mytag", selectedItem);
                    }
                });
    }

    public void showMap(View v) {
        Intent intent = new Intent(ProfileActivity.this,MapsActivity.class);
        if (location != null) {
            intent.putExtra("latitude", location.getLatitude());
            intent.putExtra("longitude", location.getLongitude());
        }
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mytag", "size =" +String.valueOf(places.size()));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count =  (int)snapshot.child("places").getChildrenCount() + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("mytag", "Failed to read value");
            }
        });

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

        if (my_stage!=null) {
            if (location.distanceTo(my_stage) < 100 && location.distanceTo(my_stage)>30) {
                tv_message.setText("Скоро вы будете на месте");
                tv_message.setBackgroundColor(Color.RED);
                tv_message.setTextColor(Color.WHITE);
            } else if (location.distanceTo(my_stage) < 30) {
                tv_message.setText("Вы совсем близко к цели!");
                tv_message.setBackgroundColor(Color.RED);
                tv_message.setTextColor(Color.WHITE);
            } else {
                tv_message.setText("");
                tv_message.setBackgroundColor(Color.WHITE);
                tv_message.setTextColor(Color.BLACK);
            }
        }

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

    public void onMyStage(String item) {

        String[] stage = item.split(";");

        String name_stage = stage[0];
        Double first_koord_stage = Double.parseDouble(stage[1]);
        Double sec_koord_stage = Double.parseDouble(stage[1]);

        my_stage = new Location(name_stage);
        my_stage.setLatitude(first_koord_stage);
        my_stage.setLongitude(sec_koord_stage);

        tv_place.setText(stage[0]);
        tv_first_koord.setText(String.valueOf(stage[1]).replaceAll("\\s+",""));
        tv_sec_koord.setText(String.valueOf(stage[2]).replaceAll("\\s+",""));
    }

    public void onSend(View v) {
        String place = tv_place.getText().toString();
        String first_koord = tv_first_koord.getText().toString();
        String sec_koord = tv_sec_koord.getText().toString();

        if (place.isEmpty() || first_koord.isEmpty() || sec_koord.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Данные не были введены", Toast.LENGTH_SHORT).show();
        } else {
            Place new_p = new Place(place, Double.parseDouble(first_koord), Double.parseDouble(sec_koord));
            dbRef.child("places").child(String.valueOf(count)).setValue(new_p);
        }
    }



    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Place place = snapshot.getValue(Place.class);
        Log.d("mytag", "place: " + place);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        name.setText(signInAccount.getDisplayName());
        mail.setText(signInAccount.getEmail());
        Uri url = signInAccount.getPhotoUrl();

        Picasso p = new Picasso.Builder(getApplicationContext()).build();
        p.load(url).into(ava);
    }

    public void onGoogleSignOut(View v) {
        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient = new GoogleAuth().googleAuthentification(this);
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
    }
}