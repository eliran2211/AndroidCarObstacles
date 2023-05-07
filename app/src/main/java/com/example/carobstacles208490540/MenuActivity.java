package com.example.carobstacles208490540;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import android.Manifest;


public class MenuActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private ToggleButton menu_TOGGLE_speed;
    private MaterialButton menu_FAB_startPlay;
    private EditText menu_PLAINTXT_name;
    private int delay = 1000;
    private Switch menu_SWITCH_isSensorMode;
    private boolean isSensorMode;
    private Location userLocation;
    FusedLocationProviderClient fusedLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        getCurrentLocation();
        addViews();
        addListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLocation = location;
                        }
                    }
                });
        }
    }

    private void addViews(){
        menu_TOGGLE_speed = findViewById(R.id.menu_TOGGLE_speed);
        menu_FAB_startPlay = findViewById(R.id.menu_FAB_startPlay);
        menu_PLAINTXT_name = findViewById(R.id.menu_PLAIN_name);
        menu_SWITCH_isSensorMode = findViewById(R.id.menu_SWITCH_isSensorMode);
    }

    private void addListeners() {
        menu_FAB_startPlay.setOnClickListener(v -> {
            String name = menu_PLAINTXT_name.getText().toString().trim();
            if(name.isEmpty()){
                printRedBorder();
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.KEY_DELAY, delay);
            intent.putExtra(MainActivity.KEY_NAME, name);
            intent.putExtra(MainActivity.KEY_SENSOR_MODE, isSensorMode);
            intent.putExtra(MainActivity.KEY_LOCATION, userLocation);
            startActivity(intent);
        });

        menu_TOGGLE_speed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menu_TOGGLE_speed.isChecked()) {
                    delay = 500;
                } else {
                    delay = 1000;
                }
            }
        });

        menu_SWITCH_isSensorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSensorMode = true;
                } else {
                    isSensorMode = false;
                }
            }
        });
    }

    private void printRedBorder() {
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setStroke(2, Color.RED);
        border.setCornerRadius(10);
        menu_PLAINTXT_name.setHintTextColor(Color.RED);
        menu_PLAINTXT_name.setBackground(border);
    }
}
