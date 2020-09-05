package com.viba.homeautomation;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;



public class TCPcode extends AppCompatActivity {
    ServerSocket serverSocket;
    Thread Thread1 = null;


    SharedPreferences.Editor editor;
    public static String SERVER_IP = "";
    public static final int SERVER_PORT = 8080;
    SharedPreferences prefs;
    String message;

//    @Override
    public void tcpCode() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
      //  super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_main);
        startService(new Intent(this, TCPservice.class));



         editor = prefs.edit();

//        String aname = prefs.getString("namea",namea);
//        String bname = prefs.getString("nameb",nameb);
//        String cname = prefs.getString("namec",namec);


        SERVER_IP = getLocalIpAddress();

        Thread1 = new Thread(new TCPcode.Thread1());
        Thread1.start();

    }

    private String getLocalIpAddress() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

    }


   // private PrintWriter output;
    private BufferedReader input;

    class Thread1 implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                while(true) {
                    try {
                        socket = serverSocket.accept();
                       // output = new PrintWriter(socket.getOutputStream());
                        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        new Thread(new TCPcode.Thread2()).start();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // String[] status = message.split(",");
//                                for(int i =0;i<7;i+=2){
//                                    status[i] = message.charAt(i);

                                editor.putString("status", message);



                             //   Toast.makeText(TCPcode.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Thread1 = new Thread(new TCPcode.Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

//    class Thread3 implements Runnable {
//        private String message;
//        Thread3(String message) {
//            this.message = message;
//        }
//        @Override
//        public void run() {
//            output.write(message);
//            output.flush();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvMessages.append("server: " + message + "\n");
//                    Toast.makeText(MainActivity2.this, message, Toast.LENGTH_SHORT).show();
//                    etMessage.setText("");
//                }
//            });
//        }
//    }
}