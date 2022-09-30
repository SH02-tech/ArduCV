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
import android.view.View;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.IOException;

import model.MobileUniverse;

public class MainActivity extends AppCompatActivity {

    private final static String USBPERMISSION = "com.example.mobilecontroller.USB_PERMISSION";

    private MobileUniverse mobileUniverse;

    private TextView debugText;
    private TextView ipText;
    private TextView portText;

    private PendingIntent pendingIntent;

    private UsbManager usbManager;
    private UsbDevice usbDevice;

    private final BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(USBPERMISSION)) {
                mobileUniverse.connectArduinoDevice(usbDevice);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobileUniverse = new MobileUniverse();

        debugText = (TextView) findViewById(R.id.debugText);
        ipText = (TextView) findViewById(R.id.ipTextView);
        portText = (TextView) findViewById(R.id.portTextView);

        usbDevice = null;
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(USBPERMISSION), PendingIntent.FLAG_MUTABLE);

        IntentFilter intentFilter = new IntentFilter(USBPERMISSION);
        registerReceiver(broadCastReceiver, intentFilter);
    }

    public void onClickConnect(View view) {
        String host = ipText.getText().toString();
        int port = Integer.parseInt(portText.getText().toString());

        mobileUniverse.setArduSocket(host, port);
        debugText.setText(debugText.getText() + "\n" + "Connected to Socket");
    }

    public void onClickArduino(View view) {
        mobileUniverse.setControlDroid(usbManager);
        usbDevice = mobileUniverse.findArduinoDevice();

        if (usbDevice != null) {
            usbManager.requestPermission(usbDevice, pendingIntent);
        }

        debugText.setText(debugText.getText() + "\n" + "Connected to Arduino");
    }

    public void onClickStart(View view) throws IOException {
        mobileUniverse.init();
        debugText.setText(debugText.getText() + "\n" + "Starting");
    }
}