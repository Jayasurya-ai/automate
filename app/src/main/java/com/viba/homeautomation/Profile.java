package com.viba.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    EditText switchaedit,switchbedit,switchcedit;
    FirebaseAuth mAuth;
    Button addallswitches;
    String result,ip;
    String swita,switb,switc,switd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        switchaedit=findViewById(R.id.editswitcha);
        switchbedit=findViewById(R.id.editswib);
        switchcedit=findViewById(R.id.editswic);
        addallswitches=findViewById(R.id.addall);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        startService(new Intent(getApplicationContext(),TCPservice.class));

//        Intent intent = getIntent();
//        result = intent.getStringExtra("ip");
//
//        Intent j = new Intent(Profile.this, Switches.class);
//        j.putExtra("ip",result);



         mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();





        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

//        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#47a626")));
//        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#47a626")));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), Testing.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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


        final DatabaseReference switchref=firebaseDatabase.getReference("SwitchesAdded");

       addallswitches.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//                   HashMap<String,Object>parsamap=new HashMap<>();
//                   parsamap.put("switchnamea",String.valueOf(switchaedit.getText().toString()));
//                   parsamap.put("switchnameb",String.valueOf(switchbedit.getText().toString()));
//                   parsamap.put("switchnamec",String.valueOf(switchcedit.getText().toString()));
//                   parsamap.put("uuid",mAuth.getCurrentUser().getUid());
//                   switchref.child(mAuth.getCurrentUser().getUid()).updateChildren(parsamap);


               SharedPreferences.Editor editor = prefs.edit();




               swita=switchaedit.getText().toString();
               switb=switchbedit.getText().toString();
               switc=switchcedit.getText().toString();

               if(!TextUtils.isEmpty(swita)) {
                   editor.putString("namea",swita);
               }
               else{
                   swita="";
                   editor.putString("namea",swita);
               }
               if(!TextUtils.isEmpty(switb)){
                   editor.putString("nameb",switb);

               }
               else {
               switb="";
               editor.putString("nameb",switb);
               }
               if(!TextUtils.isEmpty(switc)){
                   editor.putString("namec",switc);
               }
               else{
                   switc="";
                   editor.putString("namec",switc);
               }
               if(!TextUtils.isEmpty(switd)){
                   editor.putString("named",switd);
               }
               else{
                   switd="";
                   editor.putString("named",switd);
               }
               editor.commit();

               typec();

               startActivity(new Intent(Profile.this,Switches.class));
               finish();


           }
       });



    }



    public void typec(){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
            String URL = "http://192.168.1.10:5000";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("type","c");
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