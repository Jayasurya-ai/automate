package com.viba.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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


public class Switches extends AppCompatActivity {
    TextView switchname1,switchname2,switchname3,switchname4;
    String namea,nameb,namec,named,status;
    String newState ;
    CardView carda,cardb,cardc,cardd;
    Switch switcha,swichb,swichc,switchd;

    String ipp;
    //    SharedPreferences prefs;
//    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switches);

      //  overridePendingTransition(0,0);
        sharedPref = getApplicationContext().getSharedPreferences(
                "allInOne", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        switchname1=findViewById(R.id.switchname1);
        switchname2=findViewById(R.id.switchname2);
        switchname3=findViewById(R.id.switchname3);
        switchname4=findViewById(R.id.switchname4);
        carda=findViewById(R.id.carda);
        switcha=findViewById(R.id.switch1);
        swichb=findViewById(R.id.switch2);
        swichc=findViewById(R.id.switch3);
        switchd=findViewById(R.id.switch4);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ipp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        cardb=findViewById(R.id.carb);
        cardc=findViewById(R.id.carc);
        cardd=findViewById(R.id.card);
//         prefs = PreferenceManager.getDefaultSharedPreferences(this);
////        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
////                getString(R.string.filename), Context.MODE_PRIVATE);
//         editor = prefs.edit();

        syncChanges();

//         String aname = prefs.getString("namea",namea);
//         String bname = prefs.getString("nameb",nameb);
//         String cname = prefs.getString("namec",namec);
//         String dname=prefs.getString("named",named);

        String aname = sharedPref.getString("namea","");
        String bname = sharedPref.getString("nameb","");
        String cname =sharedPref.getString("namec","");
        String dname=sharedPref.getString("named","");


//        editor.commit();
        //  editor.clear();
        switchname1.setText(aname);
        switchname2.setText(bname);
        switchname3.setText(cname);
        switchname4.setText(dname);
        if(!aname.equals("")){
            carda.setVisibility(View.VISIBLE);


        }

        switcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switcha.setChecked(true);

                i=1;
                editor.putInt("s1",i).commit();
                typed();


//                        HashMap<String,Object> pushmap=new HashMap<>();
//                        pushmap.put("sstatus","open");
//                        updateref.updateChildren(pushmap);

            } else {
                switcha.setChecked(false);
                i=0;
                editor.putInt("s1",i).commit();
//                    newState[0] = String.valueOf(i);
                typed();

//                        HashMap<String,Object>pushmap=new HashMap<>();
//                        pushmap.put("sstatus","close");
//                        updateref.updateChildren(pushmap);
            }
        }//
        });
        swichb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swichb.setChecked(true);
                    i=1;
//                    newState[1] = String.valueOf(i);
                    editor.putInt("s2",i).commit();
                    typed();
//                        HashMap<String,Object> pushmap=new HashMap<>();
//                        pushmap.put("sstatus","open");
//                        updateref.updateChildren(pushmap);

                } else {
                    swichb.setChecked(false);
                    i=0;
                    //                   newState[1] = String.valueOf(i);
                    editor.putInt("s2",i).commit();
                    typed();

//                        HashMap<String,Object>pushmap=new HashMap<>();
//                        pushmap.put("sstatus","close");
//                        updateref.updateChildren(pushmap);
                }
            }//
        });

        swichc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    swichc.setChecked(true);
                    i=1;
//                    newState[2] = String.valueOf(i);
                    editor.putInt("s3",i).commit();
                    typed();
//                        HashMap<String,Object> pushmap=new HashMap<>();
//                        pushmap.put("sstatus","open");
//                        updateref.updateChildren(pushmap);

                } else {
                    swichc.setChecked(false);
                    i=0;
//                    newState[2] = String.valueOf(i);
                    editor.putInt("s3",i).commit();
                    typed();

//                        HashMap<String,Object>pushmap=new HashMap<>();
//                        pushmap.put("sstatus","close");
//                        updateref.updateChildren(pushmap);
                }
            }//
        });
        switchd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    switchd.setChecked(true);
                    i=1;
//                    newState[2] = String.valueOf(i);
                    editor.putInt("s4",i).commit();
                    typed();
//                        HashMap<String,Object> pushmap=new HashMap<>();
//                        pushmap.put("sstatus","open");
//                        updateref.updateChildren(pushmap);

                } else {
                    switchd.setChecked(false);
                    i=0;
//                    newState[2] = String.valueOf(i);
                    editor.putInt("s4",i).commit();
                    typed();

//                        HashMap<String,Object>pushmap=new HashMap<>();
//                        pushmap.put("sstatus","close");
//                        updateref.updateChildren(pushmap);
                }
            }//
        });


        if(!bname.equals("")){
            cardb.setVisibility(View.VISIBLE);




        }
        if(!cname.equals(""))
        {
            cardc.setVisibility(View.VISIBLE);


        }
        if(!dname.equals(""))
        {
            cardd.setVisibility(View.VISIBLE);


        }




        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_addedswiches);
        bottomNavigationView.setSelectedItemId(R.id.nav_switchesadded);

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
                    case  R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
//        Intent i = getIntent();
//        switchname1.setText(i.getStringExtra("namea"));
//        switchname2.setText(i.getStringExtra("nameb"));
//        switchname3.setText(i.getStringExtra("namec"));

    }

    private void syncChanges() {
//         status = prefs.getString("status","0,0,0,0");
//        status = sharedPref.getString("status","0,0,0,0");
//        String[] states = status.split(",");
        startActivity(new Intent());


        switcha.setChecked(sharedPref.getInt("s1",0)==1);
        swichb.setChecked(sharedPref.getInt("s2",0)==1);
        swichc.setChecked(sharedPref.getInt("s3",0)==1);
        switchd.setChecked(sharedPref.getInt("s4",0)==1);

    }

    public void typed() {
        try {
//            editor.putString("status",newState[0]+","+newState[1]+","+newState[2]+","+newState[3]);
//            editor.commit();
            RequestQueue requestQueue = Volley.newRequestQueue(Switches.this);
            String URL = "http://192.168.1.10:5000";
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("type", "d");
            jsonBody.put("Espid", "404");
            jsonBody.put("ip", ipp);
            String temp = "";
            temp += String.valueOf(sharedPref.getInt("s1", 0));
            temp += ",";
            temp += String.valueOf(sharedPref.getInt("s2", 0));
            temp += ",";
            temp += String.valueOf(sharedPref.getInt("s3", 0));
            temp += ",";
            temp += String.valueOf(sharedPref.getInt("s4", 0));

            jsonBody.put("status", temp);
//            jsonBody.put("status",prefs.getString("status","0,0,0,0"));
//            if(i == 1){
//                jsonBody.put("status",i);
//
//            }
//            else if(i==0){
//                jsonBody.put("status",i);
//
//            }


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
//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        }
    }
}