package model;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ControlDroid {
    private final static int ARDUINO_ID = 0x2341;
    private final static int BAUDRATE = 9600;
    private final static UsbDevice NULLDEVICE = null;
    private final static UsbSerialDevice NULLSERIALDEVICE = null;

    private UsbSerialDevice arduinoSerial;
    private UsbManager usbManager;
    private UsbDevice usbDevice;

    ControlDroid(UsbManager manager) {
        arduinoSerial = NULLSERIALDEVICE;
        usbManager = manager;
    }

    /*
        private final BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(USBPERMISSION)) {
                if (intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)) {
                    UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(usbDevice);
                    arduinoSerial = UsbSerialDevice.createUsbSerialDevice(usbDevice, usbDeviceConnection);

                    if (arduinoSerial != null) {
                        if (arduinoSerial.open()) {
                            arduinoSerial.setBaudRate(9600);
                            arduinoSerial.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            arduinoSerial.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            arduinoSerial.setParity(UsbSerialInterface.PARITY_NONE);
                            arduinoSerial.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                        }
                    }
                }
            }
        };
    };
     */

    public UsbDevice findDevice() {
        UsbDevice usbDevice = null;
        HashMap<String, UsbDevice> usbDeviceHashMap = usbManager.getDeviceList();

        if (!usbDeviceHashMap.isEmpty()) {
            boolean found = false;

            for (Map.Entry<String, UsbDevice> entry : usbDeviceHashMap.entrySet()) {
                usbDevice = entry.getValue();
                int deviceVID = usbDevice.getVendorId();

                if (deviceVID == ARDUINO_ID) {
                    found = true;
                }

                if (found) break;
            }
        }

        return usbDevice;
    }

    public boolean connectDevice(UsbDevice usbDevice) {
        return connectDevice(usbDevice, BAUDRATE);
    }

    public boolean connectDevice(UsbDevice usbDevice, int baudRate) {
        boolean connected = false;

        // UsbDevice usbDevice = findDevice();

        if (usbDevice != null) {

            UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(usbDevice);
            arduinoSerial = UsbSerialDevice.createUsbSerialDevice(usbDevice, usbDeviceConnection);

            if (arduinoSerial != null) {
                if (arduinoSerial.open()) {
                    arduinoSerial.setBaudRate(baudRate);
                    arduinoSerial.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    arduinoSerial.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    arduinoSerial.setParity(UsbSerialInterface.PARITY_NONE);
                    arduinoSerial.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                    connected = true;
                }
            }
        }


        return connected;
    }

    public void sendOrder(String order) {
        arduinoSerial.write(order.getBytes(StandardCharsets.UTF_8));
    }
}
