package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
public class CommandSocket {

    private final String DEFAULT_HOST = "127.0.0.1"; // localhost
    private final int DEFAULT_PORT = 1010;

    String host;
    int port;
    Socket socket;
    BufferedReader in;

    public CommandSocket() {
        super();
        host = DEFAULT_HOST;
        port = DEFAULT_PORT;
        socket = null;
        in = null;
    }

    public void setHost(String the_host) {
        this.host = the_host;
    }

    public void setPort(int the_port) {
        this.port = the_port;
    }

    public boolean connect(String the_host, int the_port) {
        boolean status = true;

        setHost(the_host);
        setPort(the_port);

        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            status = false;
        }

        return status;
    }

    public String getMessage() {
        String data = null;

        try {
            data = in.readLine();
        } catch (IOException e) {
            data = "";
        }

        return data;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
