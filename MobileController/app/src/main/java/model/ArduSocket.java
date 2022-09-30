package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ArduSocket {

    final static String NULLMESSAGE = "NULL";

    private boolean status;
    private InputStream inputStream;

    ArduSocket(String host, int port) {
        Socket socket = new Socket();
        try {
            connectInputStream(socket, host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.status = true;
    }

    private void connectInputStream(Socket socket, String host, int port) throws IOException {
        Socket auxSocket = socket;
        SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);

        try {
            auxSocket.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = socket.getInputStream();
    }

    public void isOn(boolean status) {
        this.status = status;
    }

    String receivedMessage() {
        String msg = NULLMESSAGE;

        if (status) {
            byte[] buffer = new byte[1];

            try {
                if (inputStream.read(buffer) > 0) {
                    msg = "" + new String(buffer, 0, 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return msg;
    }
    /*
    public static void main(String[] args) throws IOException, InterruptedException {
        ArduSocket ad = new ArduSocket("192.168.1.103", 1234);

        while (true) {
            System.out.println(ad.receivedMessage());
            Thread.sleep(1000);
        }
    }
*/
    /*
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Probando");
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(host, 1234);
        socket.connect(socketAddress);

        /*
        DataInputStream dIn = new DataInputStream(socket.getInputStream());

        for (int i = 0; i< 10; ++i) {
            String msg = Integer(dIn.readInt()).toString();
            System.out.println("Message A: " + msg);
        }

        dIn.close();


        InputStream is = socket.getInputStream();
        int read;
        byte[] buffer = new byte[1];

        while((read = is.read(buffer)) != -1) {
            String output = new String(buffer);
            System.out.print(output + "\n");
            System.out.flush();
        };

    }
     */
}
