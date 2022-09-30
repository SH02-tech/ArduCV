package model;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MobileUniverse {
    private ControlDroid controlDroid;
    private ArduSocket arduSocket;

    public MobileUniverse() {
        controlDroid = null;
        arduSocket = null;
    }

    public MobileUniverse(UsbManager usbManager, String host, int port) {
        setControlDroid(usbManager);
        setArduSocket(host, port);
    }

    public void setArduSocket(String host, int port) {
        arduSocket = new ArduSocket(host, port);
    }

    public void setControlDroid(UsbManager usbManager) {
        controlDroid = new ControlDroid(usbManager);
    }

    public UsbDevice findArduinoDevice() {
        return controlDroid.findDevice();
    }

    public boolean connectArduinoDevice(UsbDevice usbDevice) {
        return controlDroid.connectDevice(usbDevice);
    }

    public void init() {
        String msg;

        controlDroid.findDevice();

        while (true) {
            /*
            msg = arduSocket.receivedMessage();

            while (msg == ArduSocket.NULLMESSAGE) {
                msg = arduSocket.receivedMessage();
            }
            */
            controlDroid.sendOrder("f");
        }
    }
}