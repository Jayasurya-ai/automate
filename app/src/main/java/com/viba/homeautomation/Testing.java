package com.viba.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Testing extends AppCompatActivity {
    ImageView espdevice;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        espdevice = findViewById(R.id.espdevice);
       


        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        typea();

        Intent i = new Intent(Testing.this, VerifyPhoneActivity.class);
        i.putExtra("ip",ip);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_orders);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

//        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#47a626")));
//        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#47a626")));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_switchesadded:
                        startActivity(new Intent(getApplicationContext(), Switches.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

        espdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(Testing.this).inflate(R.layout.pinlayout, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(Testing.this);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.CENTER);

                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();
                Button verify = dialogView.findViewById(R.id.verifypin);
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        typeb();
                        startActivity(new Intent(Testing.this, Profile.class));
                        finish();
                    }
                });

            }
        });


    }

    public void typea(){
        try {


            RequestQueue requestQueue = Volley.newRequestQueue(Testing.this);
            String URL = "http://192.168.1.10:5000";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("type","a");
            jsonBody.put("Espid", "404");
            jsonBody.put("ip",ip);
            final String requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
            Toast.makeText(this, "typea", Toast.LENGTH_SHORT).show();
//        addswitch.setText("Added");
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("switchname", switchnamea.getText().toString());
//        editor.putInt("id", 1);
//        editor.putInt("state",0);
//        editor.commit();
//        alertDialog.dismiss();

//        startActivity(new Intent(NewComponents.this, HomeActivity.class));
//        finish();
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }

    public void typeb() {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(Testing.this);
            String URL = "http://192.168.1.10:5000";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("type","b");
            jsonBody.put("Espid", "404");
            jsonBody.put("ip", ip);
            jsonBody.put("pin",123456);
            final String requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
            Toast.makeText(this, "typeb", Toast.LENGTH_SHORT).show();
//        addswitch.setText("Added");
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("switchname", switchnamea.getText().toString());
//        editor.putInt("id", 1);
//        editor.putInt("state",0);
//        editor.commit();
//        alertDialog.dismiss();

//        startActivity(new Intent(NewComponents.this, HomeActivity.class));
//        finish();
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }
}