package com.viba.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HomeActivity extends  AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), Testing.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_switchesadded:
                        startActivity(new Intent(getApplicationContext(),Switches.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
    }
    }

