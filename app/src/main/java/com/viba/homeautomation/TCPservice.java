package com.viba.homeautomation;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPservice extends Service {
    ServerSocket serverSocket;
    Thread Thread1 = null;
    SharedPreferences.Editor editor;

    public static String SERVER_IP = "";
    public static final int SERVER_PORT = 8080;
    SharedPreferences prefs;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        Toast.makeText(TCPservice.this, "hello", Toast.LENGTH_SHORT).show();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        SERVER_IP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        Thread1 = new Thread(new TCPservice.Thread1());
        Thread1.start();

        return super.onStartCommand(intent, flags, startId);
    }

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

                        new Thread(new TCPservice.Thread2()).start();
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
                     String message = input.readLine();
                    if (message != null) {
                                // String[] status = message.split(",");
//                                for(int i =0;i<7;i+=2){
//                                    status[i] = message.charAt(i);
//                        Toast.makeText(TCPservice.this, ""+message, Toast.LENGTH_SHORT).show();
                                editor.putString("status", message);
                                editor.commit();
                              //  Toast.makeText(TCPservice.this, ""+message, Toast.LENGTH_SHORT).show();


                                //   Toast.makeText(TCPcode.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Thread1 = new Thread(new TCPservice.Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


