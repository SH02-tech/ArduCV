package com.example.mobilecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.ArduSocket;
import model.ArduinoListener;
import model.CommandSocket;

public class MainActivity extends AppCompatActivity implements ArduinoListener {

    private ArduSocket arduSocket;
    private CommandSocket commandSocket;
    private TextView debugText;
    private TextView ipText;
    private TextView portText;

    private ListenThread listenThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arduSocket = new ArduSocket(this);

        listenThread = new ListenThread();

        debugText = (TextView) findViewById(R.id.debugText);
        ipText = (TextView) findViewById(R.id.ipTextView);
        portText = (TextView) findViewById(R.id.portTextView);

        arduSocket = new ArduSocket(this);
        commandSocket = new CommandSocket();
    }

    public void onClickConnect(View view) {

        String port_text = portText.getText().toString();
    /*
        String host = ipText.getText().toString();
        int port = Integer.parseInt(port_text);
    */
        String host = "192.168.1.106";
        int port = 2020;

        boolean status = commandSocket.connect(host, port);

        if (status) {
            display("Host " + host + " connected.");
            display("Port " + port + " connected.");
        } else {
            display("Invalid connection. Please try again.");
        }

    }

    public class ListenThread extends Thread {
        @Override
        public void run() {
            display("Listening...\n");

            String data = commandSocket.getMessage();

            while (data != null) {
                sendCommandToArduino(data);
                data = commandSocket.getMessage();
            }

            display("Terminado.");
        }
    }

    public void onClickStart(View view) {
        display("Entered.\n");
        listenThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        arduSocket.setArduinoListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arduSocket.unsetArduinoListener();
        arduSocket.close();
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
        display("Arduino attached!");
        arduSocket.open(device);
    }

    @Override
    public void onArduinoDetached() {
        display("Arduino detached");
    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        display("> "+new String(bytes));
    }

    @Override
    public void onArduinoOpened() {
        // String str = "Hello World !";
        // arduSocket.send(str.getBytes());
        display("Arduino opened.");
    }

    @Override
    public void onUsbPermissionDenied() {
        display("Permission denied... New attempt in 3 sec");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arduSocket.reopen();
            }
        }, 3000);
    }

    public void display(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                debugText.append(message+"\n");
            }
        });
    }

    public void sendCommandToArduino(String instruction) {
        arduSocket.send(instruction.getBytes());
    }
}